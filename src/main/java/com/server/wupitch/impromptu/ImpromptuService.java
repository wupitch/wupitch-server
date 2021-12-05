package com.server.wupitch.impromptu;

import com.server.wupitch.account.AccountRepository;
import com.server.wupitch.account.entity.Account;
import com.server.wupitch.area.Area;
import com.server.wupitch.area.AreaRepository;
import com.server.wupitch.club.Club;
import com.server.wupitch.club.accountClubRelation.AccountClubRelation;
import com.server.wupitch.club.dto.ClubListRes;
import com.server.wupitch.club.dto.CrewFilterRes;
import com.server.wupitch.configure.response.exception.CustomException;
import com.server.wupitch.configure.response.exception.CustomExceptionStatus;
import com.server.wupitch.configure.s3.S3Uploader;
import com.server.wupitch.configure.security.authentication.CustomUserDetails;
import com.server.wupitch.fcm.FirebaseCloudMessageService;
import com.server.wupitch.impromptu.accountImpromptuRelation.AccountImpromptuRelation;
import com.server.wupitch.impromptu.accountImpromptuRelation.AccountImpromptuRelationRepository;
import com.server.wupitch.impromptu.dto.*;
import com.server.wupitch.impromptu.entity.Impromptu;
import com.server.wupitch.impromptu.repository.ImpromptuRepository;
import com.server.wupitch.impromptu.repository.ImpromptuRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.server.wupitch.configure.entity.Status.VALID;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ImpromptuService {

    private final AccountRepository accountRepository;
    private final AreaRepository areaRepository;
    private final ImpromptuRepository impromptuRepository;
    private final ImpromptuRepositoryCustom impromptuRepositoryCustom;
    private final S3Uploader s3Uploader;
    private final AccountImpromptuRelationRepository accountImpromptuRelationRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @Transactional
    public void uploadImpromptusImage(MultipartFile multipartFile, Long impromptuId) throws IOException {
        String impromptusUrl = s3Uploader.upload(multipartFile, "impromptusImage");
        Impromptu impromptu = impromptuRepository.findByImpromptuIdAndStatus(impromptuId, VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.IMPROMPTUS_NOT_FOUND));
        impromptu.setImpromptuImage(impromptusUrl);
    }

    @Transactional
    public Long createImpromptu(CreateImpromptuReq dto, CustomUserDetails customUserDetails) throws IOException {

        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_FOUND));

        Area area = areaRepository.findByAreaIdAndStatus(dto.getAreaId(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AREA_NOT_FOUND));

        Impromptu impromptu = new Impromptu(account, area, dto);

        Impromptu save = impromptuRepository.save(impromptu);

        firebaseCloudMessageService.sendMessageTo(account, account.getDeviceToken(),"번개 생성", "번개 생성이 완료되었습니다!");

        return save.getImpromptuId();

    }

    @Transactional
    public Page<ImpromptuListRes> getAllImpromptuList
            (Integer page, Integer size, String sortBy, Boolean isAsc,
             Long areaId, Integer scheduleIndex, List<Integer> days, Integer memberCountIndex, CustomUserDetails customUserDetails)
    {

        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));
        account.saveImpromptuFilterInfo(areaId, scheduleIndex, days, memberCountIndex);
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);

        Area area = null;
        if(areaId != null){
            Optional<Area> optionalArea = areaRepository.findByAreaIdAndStatus(areaId, VALID);
            if(optionalArea.isPresent()) area = optionalArea.get();
        }

        Page<Impromptu> allImpromptu = impromptuRepositoryCustom.getAllImpromptuList(pageable, area,scheduleIndex ,days, memberCountIndex);

        Page<ImpromptuListRes> dtoPage = allImpromptu.map(ImpromptuListRes::new);
        for (ImpromptuListRes impromptuListRes : dtoPage) {
            Impromptu impromptu = impromptuRepository.findById(impromptuListRes.getImpromptuId()).get();
            Optional<AccountImpromptuRelation> optional
                    = accountImpromptuRelationRepository.findByStatusAndAccountAndImpromptu(VALID ,account, impromptu);
            if(optional.isEmpty() || optional.get().getIsPinUp() == null || !optional.get().getIsPinUp()) impromptuListRes.isPinUp = false;
            else impromptuListRes.setIsPinUp(true);
        }

        Page<ImpromptuListRes> result = new Page<ImpromptuListRes>() {
            @Override
            public int getTotalPages() {
                return dtoPage.getTotalPages();
            }

            @Override
            public long getTotalElements() {
                return dtoPage.getTotalElements();
            }

            @Override
            public <U> Page<U> map(Function<? super ImpromptuListRes, ? extends U> converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return dtoPage.getNumber();
            }

            @Override
            public int getSize() {
                return dtoPage.getSize();
            }

            @Override
            public int getNumberOfElements() {
                return dtoPage.getNumberOfElements();
            }

            @Override
            public List<ImpromptuListRes> getContent() {
                return dtoPage.getContent().stream().sorted().collect(Collectors.toList());
            }

            @Override
            public boolean hasContent() {
                return dtoPage.hasContent();
            }

            @Override
            public Sort getSort() {
                return dtoPage.getSort();
            }

            @Override
            public boolean isFirst() {
                return dtoPage.isFirst();
            }

            @Override
            public boolean isLast() {
                return dtoPage.isLast();
            }

            @Override
            public boolean hasNext() {
                return dtoPage.hasNext();
            }

            @Override
            public boolean hasPrevious() {
                return dtoPage.hasPrevious();
            }

            @Override
            public Pageable nextPageable() {
                return dtoPage.nextPageable();
            }

            @Override
            public Pageable previousPageable() {
                return dtoPage.previousPageable();
            }

            @NotNull
            @Override
            public Iterator<ImpromptuListRes> iterator() {
                return dtoPage.iterator();
            }
        };
        return result;

    }

    public ImpromptuDetailRes getDetailImpromptusById(Long impromptuId, CustomUserDetails customUserDetails) {
        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));
        Impromptu impromptu = impromptuRepository.findByImpromptuIdAndStatus(impromptuId, VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.IMPROMPTUS_NOT_FOUND));
        Optional<AccountImpromptuRelation> optionalAccountImpromptuRelation
                = accountImpromptuRelationRepository.findByStatusAndAccountAndImpromptu(VALID, account, impromptu);

        ImpromptuDetailRes impromptuDetailRes = new ImpromptuDetailRes(impromptu);
        if (optionalAccountImpromptuRelation.isEmpty()) {
            impromptuDetailRes.setIsPinUp(false);
            impromptuDetailRes.setIsSelect(false);
        }
        else {
            if(optionalAccountImpromptuRelation.get().getIsPinUp() == null || !optionalAccountImpromptuRelation.get().getIsPinUp()) impromptuDetailRes.setIsPinUp(false);
            else impromptuDetailRes.setIsPinUp(true);

            if(optionalAccountImpromptuRelation.get().getIsSelect() == null || !optionalAccountImpromptuRelation.get().getIsSelect()) impromptuDetailRes.setIsSelect(false);
            else impromptuDetailRes.setIsSelect(true);
        }


        return impromptuDetailRes;
    }

    @Transactional
    public ImpromptuResultRes impromptuPinUpToggleByAuth(Long impromptuId, CustomUserDetails customUserDetails) {
        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));

        Impromptu impromptu = impromptuRepository.findByImpromptuIdAndStatus(impromptuId, VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.IMPROMPTUS_NOT_FOUND));

        Optional<AccountImpromptuRelation> optional
                = accountImpromptuRelationRepository.findByStatusAndAccountAndImpromptu(VALID, account, impromptu);
        if(optional.isPresent()){
            optional.get().togglePinUp();
            if(optional.get().getIsPinUp()) return new ImpromptuResultRes(true);
            else return new ImpromptuResultRes(false);
        }

        else{
            AccountImpromptuRelation build = AccountImpromptuRelation.builder()
                    .status(VALID)
                    .account(account)
                    .impromptu(impromptu)
                    .isPinUp(true)
                    .build();
            accountImpromptuRelationRepository.save(build);
            return new ImpromptuResultRes(true);
        }
    }

    @Transactional
    public ImpromptuResultRes impromptuParticipationToggleByAuth(Long impromptuId, CustomUserDetails customUserDetails) throws IOException {

        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));

        Impromptu impromptu = impromptuRepository.findByImpromptuIdAndStatus(impromptuId, VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.IMPROMPTUS_NOT_FOUND));

        Optional<AccountImpromptuRelation> optional
                = accountImpromptuRelationRepository.findByStatusAndAccountAndImpromptu(VALID, account, impromptu);
        if(optional.isPresent()){
            if(optional.get().getIsSelect() == null  || !optional.get().getIsSelect()){
                impromptu.addMemberCount();
                optional.get().toggleSelect();
                firebaseCloudMessageService.sendMessageTo(account, account.getDeviceToken(), "번개 참여 수락", "'"+impromptu.getTitle()+"'"+" 크루에 대한 신청이 수락되었습니다.");
                return new ImpromptuResultRes(true);
            }
            else{
                impromptu.minusMemberCount();
                optional.get().toggleSelect();
                return new ImpromptuResultRes(false);
            }
        }
        else{
            AccountImpromptuRelation build = AccountImpromptuRelation.builder()
                    .status(VALID)
                    .account(account)
                    .impromptu(impromptu)
                    .isSelect(true)
                    .build();
            accountImpromptuRelationRepository.save(build);
            impromptu.addMemberCount();
            return new ImpromptuResultRes(true);
        }
    }

    @Transactional
    public ImpromptuFilterRes getImpromptuFilterRes(CustomUserDetails customUserDetails) {
        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));
        Optional<Area> optional = areaRepository.findByAreaIdAndStatus(account.getImpromptuPickAreaId(), VALID);
        if (optional.isEmpty()) return new ImpromptuFilterRes(account, null);
        else return new ImpromptuFilterRes(account, optional.get());
    }

    public Page<ImpromptuListRes> getAllImpromptuListByTitle
            (Integer page, Integer size, String sortBy, Boolean isAsc, Long areaId, String title, CustomUserDetails customUserDetails) {

        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);

        Area area = null;
        if (areaId != null) {
            Optional<Area> optionalArea = areaRepository.findByAreaIdAndStatus(areaId, VALID);
            if (optionalArea.isPresent()) area = optionalArea.get();
        }
        if(title == null) title = "";
        Page<Impromptu> allImpromptu = null;
        if(area == null) allImpromptu = impromptuRepository.findByStatusAndTitleContainingAndDateAfter(pageable, VALID, title, LocalDate.now().minusDays(1));
        else allImpromptu = impromptuRepository.findByStatusAndTitleContainingAndAreaAndDateAfter(pageable, VALID, title, area, LocalDate.now().minusDays(1));
        Page<ImpromptuListRes> dtoPage = allImpromptu.map(ImpromptuListRes::new);
        for (ImpromptuListRes impromptuListRes : dtoPage) {
            Impromptu impromptu = impromptuRepository.findById(impromptuListRes.getImpromptuId()).get();
            Optional<AccountImpromptuRelation> optional
                    = accountImpromptuRelationRepository.findByStatusAndAccountAndImpromptu(VALID ,account, impromptu);
            if(optional.isEmpty() || optional.get().getIsPinUp() == null || !optional.get().getIsPinUp()) impromptuListRes.isPinUp = false;
            else impromptuListRes.setIsPinUp(true);
        }
        Page<ImpromptuListRes> result = new Page<ImpromptuListRes>() {
            @Override
            public int getTotalPages() {
                return dtoPage.getTotalPages();
            }

            @Override
            public long getTotalElements() {
                return dtoPage.getTotalElements();
            }

            @Override
            public <U> Page<U> map(Function<? super ImpromptuListRes, ? extends U> converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return dtoPage.getNumber();
            }

            @Override
            public int getSize() {
                return dtoPage.getSize();
            }

            @Override
            public int getNumberOfElements() {
                return dtoPage.getNumberOfElements();
            }

            @Override
            public List<ImpromptuListRes> getContent() {
                return dtoPage.getContent().stream().sorted().collect(Collectors.toList());
            }

            @Override
            public boolean hasContent() {
                return dtoPage.hasContent();
            }

            @Override
            public Sort getSort() {
                return dtoPage.getSort();
            }

            @Override
            public boolean isFirst() {
                return dtoPage.isFirst();
            }

            @Override
            public boolean isLast() {
                return dtoPage.isLast();
            }

            @Override
            public boolean hasNext() {
                return dtoPage.hasNext();
            }

            @Override
            public boolean hasPrevious() {
                return dtoPage.hasPrevious();
            }

            @Override
            public Pageable nextPageable() {
                return dtoPage.nextPageable();
            }

            @Override
            public Pageable previousPageable() {
                return dtoPage.previousPageable();
            }

            @NotNull
            @Override
            public Iterator<ImpromptuListRes> iterator() {
                return dtoPage.iterator();
            }
        };
        return result;
    }
}
