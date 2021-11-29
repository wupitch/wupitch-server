package com.server.wupitch.club;

import com.server.wupitch.account.AccountRepository;
import com.server.wupitch.account.entity.Account;
import com.server.wupitch.area.Area;
import com.server.wupitch.area.AreaRepository;
import com.server.wupitch.club.accountClubRelation.AccountClubRelation;
import com.server.wupitch.club.accountClubRelation.AccountClubRelationRepository;
import com.server.wupitch.club.dto.ClubDetailRes;
import com.server.wupitch.club.dto.ClubListRes;
import com.server.wupitch.club.dto.CreateClubReq;
import com.server.wupitch.club.repository.ClubRepository;
import com.server.wupitch.club.repository.ClubRepositoryCustom;
import com.server.wupitch.configure.response.exception.CustomException;
import com.server.wupitch.configure.response.exception.CustomExceptionStatus;
import com.server.wupitch.configure.s3.S3Uploader;
import com.server.wupitch.configure.security.authentication.CustomUserDetails;
import com.server.wupitch.extra.entity.ClubExtraRelation;
import com.server.wupitch.extra.repository.ClubExtraRelationRepository;
import com.server.wupitch.extra.repository.ExtraRepository;
import com.server.wupitch.extra.entity.Extra;
import com.server.wupitch.sports.entity.Sports;
import com.server.wupitch.sports.repository.SportsRepository;
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

    public Page<ClubListRes> getAllClubList(
            Integer page, Integer size, String sortBy, Boolean isAsc, Long areaId, List<Long> sportsList,
            List<Integer> days, Integer memberCountValue, List<Integer> ageList, CustomUserDetails customUserDetails) {

        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);

        Area area = null;
        Sports sports = null;
        if (areaId != null) {
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
        }

        return dtoPage;

    }

    @Transactional
    public Long createClub(CreateClubReq dto, CustomUserDetails customUserDetails) {
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
        return save.getClubId();
    }

    @Transactional
    public void uploadCrewImage(MultipartFile multipartFile, Long crewId) throws IOException {
        String crewImageUrl = s3Uploader.upload(multipartFile, "crewImage");
        Club club = clubRepository.findByClubIdAndStatus(crewId, VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.CREW_NOT_FOUND));
        club.setImageUrl(crewImageUrl);
    }

    public ClubDetailRes getDetailClubById(Long clubId) {
        Club club = clubRepository.findByClubIdAndStatus(clubId, VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.CREW_NOT_FOUND));
        ClubDetailRes clubDetailRes = new ClubDetailRes(club);
        List<ClubExtraRelation> extraRelationList = clubExtraRelationRepository.findAllByClubAndStatus(club, VALID);
        clubDetailRes.setExtraList(extraRelationList.stream().map(e -> e.getExtra().getInfo()).collect(Collectors.toList()));
        return clubDetailRes;
    }

    @Transactional
    public void clubPinUpToggleByAuth(Long clubId, CustomUserDetails customUserDetails) {
        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));

        Club club = clubRepository.findByClubIdAndStatus(clubId, VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.CREW_NOT_FOUND));

        Optional<AccountClubRelation> optional
                = accountClubRelationRepository.findByStatusAndAccountAndClub(VALID, account, club);
        if(optional.isPresent()) optional.get().togglePinUp();
        else{
            AccountClubRelation build = AccountClubRelation.builder()
                    .status(VALID)
                    .account(account)
                    .club(club)
                    .isPinUp(true)
                    .build();
            accountClubRelationRepository.save(build);
        }
    }

    @Transactional
    public void clubParticipationToggleByAuth(Long clubId, CustomUserDetails customUserDetails) {

        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));

        Club club = clubRepository.findByClubIdAndStatus(clubId, VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.CREW_NOT_FOUND));

        Optional<AccountClubRelation> optional
                = accountClubRelationRepository.findByStatusAndAccountAndClub(VALID, account, club);
        if(optional.isPresent()){
            if(optional.get().getIsSelect() == null || !optional.get().getIsSelect()){
                club.addMemberCount();
            } else club.minusMemberCount();
            optional.get().toggleSelect();
        }
        else{
            AccountClubRelation build = AccountClubRelation.builder()
                    .status(VALID)
                    .account(account)
                    .club(club)
                    .isSelect(true)
                    .build();
            accountClubRelationRepository.save(build);
            club.addMemberCount();
        }

    }
}
