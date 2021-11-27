package com.server.wupitch.impromptu;

import com.server.wupitch.account.AccountRepository;
import com.server.wupitch.account.entity.Account;
import com.server.wupitch.area.Area;
import com.server.wupitch.area.AreaRepository;
import com.server.wupitch.club.Club;
import com.server.wupitch.club.accountClubRelation.AccountClubRelation;
import com.server.wupitch.configure.response.exception.CustomException;
import com.server.wupitch.configure.response.exception.CustomExceptionStatus;
import com.server.wupitch.configure.s3.S3Uploader;
import com.server.wupitch.configure.security.authentication.CustomUserDetails;
import com.server.wupitch.impromptu.accountImpromptuRelation.AccountImpromptuRelation;
import com.server.wupitch.impromptu.accountImpromptuRelation.AccountImpromptuRelationRepository;
import com.server.wupitch.impromptu.dto.CreateImpromptuReq;
import com.server.wupitch.impromptu.dto.ImpromptuDetailRes;
import com.server.wupitch.impromptu.dto.ImpromptuListRes;
import com.server.wupitch.impromptu.entity.Impromptu;
import com.server.wupitch.impromptu.repository.ImpromptuRepository;
import com.server.wupitch.impromptu.repository.ImpromptuRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

    @Transactional
    public void uploadImpromptusImage(MultipartFile multipartFile, Long impromptuId) throws IOException {
        String impromptusUrl = s3Uploader.upload(multipartFile, "impromptusImage");
        Impromptu impromptu = impromptuRepository.findByImpromptuIdAndStatus(impromptuId, VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.IMPROMPTUS_NOT_FOUND));
        impromptu.setImpromptuImage(impromptusUrl);
    }

    @Transactional
    public Long createImpromptu(CreateImpromptuReq dto, CustomUserDetails customUserDetails) {

        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_FOUND));

        Area area = areaRepository.findByAreaIdAndStatus(dto.getAreaId(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AREA_NOT_FOUND));

        Impromptu impromptu = new Impromptu(account, area, dto);

        Impromptu save = impromptuRepository.save(impromptu);

        return save.getImpromptuId();

    }

    public Page<ImpromptuListRes> getAllImpromptuList
            (Integer page, Integer size, String sortBy, Boolean isAsc,
             Long areaId, Integer scheduleIndex, List<Integer> days, Integer memberCountIndex, CustomUserDetails customUserDetails)
    {

        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));
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
            if(optional.isEmpty() || !optional.get().getIsPinUp()) impromptuListRes.setIsPinUp(false);
            else impromptuListRes.setIsPinUp(true);
        }

        return dtoPage;

    }

    public ImpromptuDetailRes getDetailImpromptusById(Long impromptuId) {
        Impromptu impromptu = impromptuRepository.findByImpromptuIdAndStatus(impromptuId, VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.IMPROMPTUS_NOT_FOUND));

        return new ImpromptuDetailRes(impromptu);
    }

    @Transactional
    public void impromptuPinUpToggleByAuth(Long impromptuId, CustomUserDetails customUserDetails) {
        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));

        Impromptu impromptu = impromptuRepository.findByImpromptuIdAndStatus(impromptuId, VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.IMPROMPTUS_NOT_FOUND));

        Optional<AccountImpromptuRelation> optional
                = accountImpromptuRelationRepository.findByStatusAndAccountAndImpromptu(VALID, account, impromptu);
        if(optional.isPresent()) optional.get().togglePinUp();
        else{
            AccountImpromptuRelation build = AccountImpromptuRelation.builder()
                    .status(VALID)
                    .account(account)
                    .impromptu(impromptu)
                    .isPinUp(true)
                    .build();
            accountImpromptuRelationRepository.save(build);
        }
    }
}
