package com.server.wupitch.club;

import com.server.wupitch.account.AccountRepository;
import com.server.wupitch.account.dto.ProfileRes;
import com.server.wupitch.account.entity.Account;
import com.server.wupitch.area.Area;
import com.server.wupitch.area.AreaRepository;
import com.server.wupitch.club.accountClubRelation.AccountClubRelation;
import com.server.wupitch.club.accountClubRelation.AccountClubRelationRepository;
import com.server.wupitch.club.dto.*;
import com.server.wupitch.club.repository.ClubRepository;
import com.server.wupitch.club.repository.ClubRepositoryCustom;
import com.server.wupitch.club.repository.GuestInfoRepository;
import com.server.wupitch.configure.response.exception.CustomException;
import com.server.wupitch.configure.response.exception.CustomExceptionStatus;
import com.server.wupitch.configure.s3.S3Uploader;
import com.server.wupitch.configure.security.authentication.CustomUserDetails;
import com.server.wupitch.extra.entity.ClubExtraRelation;
import com.server.wupitch.extra.repository.ClubExtraRelationRepository;
import com.server.wupitch.extra.repository.ExtraRepository;
import com.server.wupitch.extra.entity.Extra;
import com.server.wupitch.fcm.FirebaseCloudMessageService;
import com.server.wupitch.sports.entity.AccountSportsRelation;
import com.server.wupitch.sports.entity.Sports;
import com.server.wupitch.sports.repository.AccountSportsRelationRepository;
import com.server.wupitch.sports.repository.SportsRepository;
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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.server.wupitch.configure.entity.Status.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ClubService {

    private final ClubRepositoryCustom clubRepositoryCustom;
    private final AreaRepository areaRepository;
    private final SportsRepository sportsRepository;
    private final AccountRepository accountRepository;
    private final ClubRepository clubRepository;
    private final ExtraRepository extraRepository;
    private final ClubExtraRelationRepository clubExtraRelationRepository;
    private final S3Uploader s3Uploader;
    private final AccountClubRelationRepository accountClubRelationRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;
    private final GuestInfoRepository guestInfoRepository;
    private final AccountSportsRelationRepository accountSportsRelationRepository;

    @Transactional
    public Page<ClubListRes> getAllClubList(
            Integer page, Integer size, String sortBy, Boolean isAsc, Long areaId, List<Long> sportsList,
            List<Integer> days, Integer memberCountValue, List<Integer> ageList, CustomUserDetails customUserDetails) {

        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));

        account.saveFilterInfo(ageList, areaId, days, memberCountValue, sportsList);

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);

        Area area = null;
        Sports sports = null;
        if (areaId != null && areaId != 1) {
            Optional<Area> optionalArea = areaRepository.findByAreaIdAndStatus(areaId, VALID);
            if (optionalArea.isPresent()) area = optionalArea.get();
        }

        Page<Club> allClub = clubRepositoryCustom.findAllClub(pageable, area, sportsList, days, memberCountValue, ageList);
        Page<ClubListRes> dtoPage = allClub.map(ClubListRes::new);
        for (ClubListRes clubListRes : dtoPage) {
            Club club = clubRepository.findById(clubListRes.getClubId()).get();
            Optional<AccountClubRelation> optional = accountClubRelationRepository.findByStatusAndAccountAndClub(VALID, account, club);
            if (optional.isEmpty() || optional.get().getIsPinUp() == null || !optional.get().getIsPinUp()){
                if (clubListRes.getIsPinUp() == null) clubListRes.setIsPinUp(Boolean.FALSE);
                else clubListRes.setIsPinUp(false);
            }
            else{
                if (clubListRes.getIsPinUp() == null) clubListRes.setIsPinUp(Boolean.TRUE);
                clubListRes.setIsPinUp(true);
            }
            if(account.equals(club.getAccount())) clubListRes.setIsCreate(true);
        }


        Page<ClubListRes> result = new Page<ClubListRes>() {
            @Override
            public int getTotalPages() {
                return dtoPage.getTotalPages();
            }

            @Override
            public long getTotalElements() {
                return dtoPage.getTotalElements();
            }

            @Override
            public <U> Page<U> map(Function<? super ClubListRes, ? extends U> converter) {
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
            public List<ClubListRes> getContent() {
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
            public Iterator<ClubListRes> iterator() {
                return dtoPage.iterator();
            }
        };
        return result;

    }

    @Transactional
    public Long createClub(CreateClubReq dto, CustomUserDetails customUserDetails) throws IOException {
        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_FOUND));

        Sports sports = sportsRepository.findBySportsIdAndStatus(dto.getSportsId(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.SPORTS_NOT_FOUND));

        Area area = areaRepository.findByAreaIdAndStatus(dto.getAreaId(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AREA_NOT_FOUND));

        Club club = new Club(dto, account, sports, area);

        Club save = clubRepository.save(club);

        if (dto.getExtraInfoList() != null) {
            for (Long extraNum : dto.getExtraInfoList()) {
                Extra extra = extraRepository.findAllByExtraIdAndStatus(extraNum, VALID)
                        .orElseThrow(() -> new CustomException(CustomExceptionStatus.EXTRA_NOT_FOUND));

                ClubExtraRelation clubExtraRelation = new ClubExtraRelation(save, extra);
                clubExtraRelationRepository.save(clubExtraRelation);
            }
        }
        firebaseCloudMessageService.sendMessageTo(account, account.getDeviceToken(),"크루 생성", "크루 생성이 완료되었습니다!");
        AccountClubRelation build = AccountClubRelation.builder()
                .status(VALID)
                .account(account)
                .club(club)
                .isSelect(true)
                .isValid(true)
                .build();
        accountClubRelationRepository.save(build);
        return save.getClubId();
    }

    @Transactional
    public void uploadCrewImage(MultipartFile multipartFile, Long crewId) throws IOException {
        String crewImageUrl = s3Uploader.upload(multipartFile, "crewImage");
        Club club = clubRepository.findByClubIdAndStatus(crewId, VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.CREW_NOT_FOUND));
        club.setImageUrl(crewImageUrl);
    }

    public ClubDetailRes getDetailClubById(Long clubId, CustomUserDetails customUserDetails) {

        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));
        Club club = clubRepository.findByClubIdAndStatus(clubId, VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.CREW_NOT_FOUND));
        Optional<AccountClubRelation> optionalAccountClubRelation =
                accountClubRelationRepository.findByStatusAndAccountAndClub(VALID, account, club);
        ClubDetailRes clubDetailRes = new ClubDetailRes(club);
        if (optionalAccountClubRelation.isEmpty()) {
            clubDetailRes.setIsPinUp(false);
            clubDetailRes.setIsSelect(false);
        }
        else {
            if (optionalAccountClubRelation.get().getIsPinUp() == null || !optionalAccountClubRelation.get().getIsPinUp()) clubDetailRes.setIsPinUp(false);
            else clubDetailRes.setIsPinUp(true);

            if (optionalAccountClubRelation.get().getIsSelect() == null || !optionalAccountClubRelation.get().getIsSelect()) clubDetailRes.setIsSelect(false);
            else clubDetailRes.setIsSelect(true);
        }
        List<ClubExtraRelation> extraRelationList = clubExtraRelationRepository.findAllByClubAndStatus(club, VALID);
        clubDetailRes.setExtraList(extraRelationList.stream().map(e -> e.getExtra().getInfo()).collect(Collectors.toList()));
        return clubDetailRes;
    }

    @Transactional
    public CrewResultRes clubPinUpToggleByAuth(Long clubId, CustomUserDetails customUserDetails) {
        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));

        Club club = clubRepository.findByClubIdAndStatus(clubId, VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.CREW_NOT_FOUND));

        Optional<AccountClubRelation> optional
                = accountClubRelationRepository.findByStatusAndAccountAndClub(VALID, account, club);
        if(optional.isPresent()){
            optional.get().togglePinUp();
            if (optional.get().getIsPinUp()) return new CrewResultRes(true);
            else return new CrewResultRes(false);
        }
        else{
            AccountClubRelation build = AccountClubRelation.builder()
                    .status(VALID)
                    .account(account)
                    .club(club)
                    .isPinUp(true)
                    .isValid(false)
                    .build();
            accountClubRelationRepository.save(build);
            return new CrewResultRes(true);
        }
    }

    @Transactional
    public CrewResultRes clubParticipationToggleByAuth(Long clubId, CustomUserDetails customUserDetails){

        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));

        List<AccountSportsRelation> sportsList = accountSportsRelationRepository.findAllByAccountAndStatus(account, VALID);
        if (sportsList.isEmpty() || account.getAgeNum() == null || account.getNickname() == null || account.getPhoneNumber() == null) {
            throw new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID_INFORM);
        }

        Club club = clubRepository.findByClubIdAndStatus(clubId, VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.CREW_NOT_FOUND));

        Optional<AccountClubRelation> optional
                = accountClubRelationRepository.findByStatusAndAccountAndClub(VALID, account, club);
        if(optional.isPresent()){
            if(optional.get().getIsSelect() == null || !optional.get().getIsSelect()){
                club.addMemberCount();
                optional.get().toggleSelect();
                return new CrewResultRes(true);
            } else{
                throw new CustomException(CustomExceptionStatus.CREW_ALREADY_BELONG);
//                club.minusMemberCount();
//                optional.get().toggleSelect();
//                return new CrewResultRes(false);
            }
        }
        else{
            AccountClubRelation build = AccountClubRelation.builder()
                    .status(VALID)
                    .account(account)
                    .club(club)
                    .isSelect(true)
                    .isValid(false)
                    .build();
            accountClubRelationRepository.save(build);
            club.addMemberCount();
            return new CrewResultRes(true);
        }

    }

    public CrewFilterRes getCrewFilterByAuth(CustomUserDetails customUserDetails) {
        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));
        Optional<Area> optional = areaRepository.findByAreaIdAndStatus(account.getCrewPickAreaId(), VALID);
        if (optional.isEmpty()) return new CrewFilterRes(account, null);
        else return new CrewFilterRes(account, optional.get());
    }

    public Page<ClubListRes> getAllClubListByClubTitle
            (Integer page, Integer size, String sortBy, Boolean isAsc, Long areaId, String crewTitle, CustomUserDetails customUserDetails) {
        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);

        Area area = null;
        if (areaId != null && areaId != 1) {
            Optional<Area> optionalArea = areaRepository.findByAreaIdAndStatus(areaId, VALID);
            if (optionalArea.isPresent()) area = optionalArea.get();
        }
        if(crewTitle == null) crewTitle = "";
        Page<Club> allClub = null;
        if(area == null) allClub = clubRepository.findByStatusAndTitleContaining(pageable, VALID, crewTitle);
        else allClub = clubRepository.findByStatusAndTitleContainingAndArea(pageable, VALID, crewTitle, area);
        Page<ClubListRes> dtoPage = allClub.map(ClubListRes::new);
        for (ClubListRes clubListRes : dtoPage) {
            Club club = clubRepository.findById(clubListRes.getClubId()).get();
            Optional<AccountClubRelation> optional = accountClubRelationRepository.findByStatusAndAccountAndClub(VALID, account, club);
            if (optional.isEmpty() || optional.get().getIsPinUp() == null || !optional.get().getIsPinUp()){
                if (clubListRes.getIsPinUp() == null) clubListRes.setIsPinUp(Boolean.FALSE);
                else clubListRes.setIsPinUp(false);
            }
            else{
                if (clubListRes.getIsPinUp() == null) clubListRes.setIsPinUp(Boolean.TRUE);
                clubListRes.setIsPinUp(true);
            }
            if(account.equals(club.getAccount())) clubListRes.setIsCreate(true);
        }
        Page<ClubListRes> result = new Page<ClubListRes>() {
            @Override
            public int getTotalPages() {
                return dtoPage.getTotalPages();
            }

            @Override
            public long getTotalElements() {
                return dtoPage.getTotalElements();
            }

            @Override
            public <U> Page<U> map(Function<? super ClubListRes, ? extends U> converter) {
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
            public List<ClubListRes> getContent() {
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
            public Iterator<ClubListRes> iterator() {
                return dtoPage.iterator();
            }
        };
        return result;
    }


    public GuestInfoRes getClubGuestInfo(Long clubId) {
        Club club = clubRepository.findByClubIdAndStatus(clubId, VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.CREW_NOT_FOUND));
        return new GuestInfoRes(club);
    }

    @Transactional
    public void createGuestInfo(CrewGuestJoinReq dto, CustomUserDetails customUserDetails) {

        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));

        List<AccountSportsRelation> sportsList = accountSportsRelationRepository.findAllByAccountAndStatus(account, VALID);
        if (sportsList.isEmpty() || account.getAgeNum() == null || account.getNickname() == null || account.getPhoneNumber() == null) {
            throw new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID_INFORM);
        }

        Club club = clubRepository.findByClubIdAndStatus(dto.getCrewId(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.CREW_NOT_FOUND));

        List<GuestInfo> data
                = guestInfoRepository.findAllByAccountAndClubAndStatusAndSelectedDateAfter(account, club, VALID, LocalDate.now().minusDays(1));
        if(data == null || data.size() >0) throw new CustomException(CustomExceptionStatus.CREW_ALREADY_BELONG);
        Optional<AccountClubRelation> optional = accountClubRelationRepository.findByStatusAndAccountAndClub(VALID, account, club);
        if (optional.isPresent()) {
            if (optional.get().getIsSelect()) throw new CustomException(CustomExceptionStatus.CREW_ALREADY_BELONG);
        }

        GuestInfo guestInfo = new GuestInfo(account, club, dto.getDate());


        guestInfoRepository.save(guestInfo);
    }

    public List<ClubListRes> getClubListByAuth(CustomUserDetails customUserDetails) {
        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));
        List<AccountClubRelation> allByStatusAndAccount = accountClubRelationRepository.findAllByStatusAndAccountAndIsSelect(VALID, account, true);
        List<ClubListRes> list = new ArrayList<>();
        for (AccountClubRelation accountClubRelation : allByStatusAndAccount) {
            Optional<Club> optionalClub = clubRepository.findByClubIdAndStatus(accountClubRelation.getClub().getClubId(), VALID);
            if (optionalClub.isPresent()) {
                list.add(new ClubListRes(optionalClub.get()));
            }
        }
        return list;
    }

    @Transactional
    public void uploadCrewImageEmpty(Long crewId) {
        Club club = clubRepository.findByClubIdAndStatus(crewId, VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.CREW_NOT_FOUND));
        club.setImageUrl(null);
    }

    public List<MemberListRes> getClubMemberList(Long clubId) {

        Club club = clubRepository.findByClubIdAndStatus(clubId, VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.CREW_NOT_FOUND));

        List<MemberListRes> result = new ArrayList<>();

        List<AccountClubRelation> memberList
                = accountClubRelationRepository.findAllByStatusAndClubAndIsSelect(VALID, club, true);

        for (AccountClubRelation accountClubRelation : memberList) {
            result.add(new MemberListRes(accountClubRelation));
        }

        List<GuestInfo> guestInfos
                = guestInfoRepository.findAllByClubAndStatusAndSelectedDateAfter(club, VALID, LocalDate.now().minusDays(1));

        for (GuestInfo guestInfo : guestInfos) {
            result.add(new MemberListRes(guestInfo));
        }

        Collections.sort(result);

        return result;

    }

    @Transactional
    public void enrollCrewMember(EnrollMemberReq dto) throws IOException {

        Account account = accountRepository.findByAccountIdAndStatus(dto.getAccountId(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));

        Club club = clubRepository.findByClubIdAndStatus(dto.getClubId(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.CREW_NOT_FOUND));

        if (!dto.getIsGuest()) {
            AccountClubRelation accountClubRelation = accountClubRelationRepository.findByStatusAndAccountAndClub(VALID, account, club)
                    .orElseThrow(() -> new CustomException(CustomExceptionStatus.CREW_RELATION_INVALID));
            accountClubRelation.enroll();
            firebaseCloudMessageService.sendMessageTo(account, account.getDeviceToken(),"크루 참여 수락", "'"+club.getTitle()+"'"+" 크루에 대한 신청이 수락되었습니다.");
        }
        else {
            GuestInfo guestInfo = guestInfoRepository.findByStatusAndAccountAndClubAndSelectedDateAfter(VALID, account, club, LocalDate.now().minusDays(1))
                    .orElseThrow(() -> new CustomException(CustomExceptionStatus.CREW_RELATION_INVALID));
            guestInfo.enroll();
            firebaseCloudMessageService.sendMessageTo(account, account.getDeviceToken(),"크루 게스트 참여 수락", "'"+club.getTitle()+"'"+" 크루에 대한 게스트 신청이 수락되었습니다.");
        }

    }

    @Transactional
    public void disagreeEnrollCrewMember(EnrollMemberReq dto) {

        Account account = accountRepository.findByAccountIdAndStatus(dto.getAccountId(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));

        Club club = clubRepository.findByClubIdAndStatus(dto.getClubId(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.CREW_NOT_FOUND));

        if (!dto.getIsGuest()) {
            AccountClubRelation accountClubRelation = accountClubRelationRepository.findByStatusAndAccountAndClub(VALID, account, club)
                    .orElseThrow(() -> new CustomException(CustomExceptionStatus.CREW_RELATION_INVALID));
            accountClubRelation.disagreeEnroll();
        }
        else {
            GuestInfo guestInfo = guestInfoRepository.findByStatusAndAccountAndClubAndSelectedDateAfter(VALID, account, club, LocalDate.now().minusDays(1))
                    .orElseThrow(() -> new CustomException(CustomExceptionStatus.CREW_RELATION_INVALID));
            guestInfo.disagreeEnroll();
        }
    }

    public ClubProfileRes getClubAccountProfile(CustomUserDetails customUserDetails, Long accountId, Long clubId) {
        Account account = accountRepository.findByAccountIdAndStatus(accountId, VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_FOUND));

        Club club = clubRepository.findByClubIdAndStatus(clubId, VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.CREW_NOT_FOUND));

        List<AccountSportsRelation> entityList = accountSportsRelationRepository.findAllByAccountAndStatus(account, VALID);
        List<Long> stringList = new ArrayList<>();
        for (AccountSportsRelation accountSportsRelation : entityList) {
            stringList.add(accountSportsRelation.getSports().getSportsId());
        }

        ClubProfileRes result = new ClubProfileRes(account, stringList);

        result.isLeader(club.getAccount().getEmail().equals(customUserDetails.getEmail()));

        Optional<AccountClubRelation> optional
                = accountClubRelationRepository.findAllByStatusAndClubAndAccountAndIsSelect(VALID, club, account, true);

        if (optional.isPresent()) {
            result.addInfo(optional.get());
        }

        else{
            Optional<GuestInfo> guestInfoOptional
                    = guestInfoRepository.findByClubAndAccountAndStatusAndSelectedDateAfter(club, account, VALID, LocalDate.now().minusDays(1));
            if (guestInfoOptional.isEmpty()) throw new CustomException(CustomExceptionStatus.CREW_RELATION_INVALID);
            else result.addInfo(guestInfoOptional.get());
        }

        return result;
    }
}
