package com.server.wupitch.club;

import com.server.wupitch.account.AccountRepository;
import com.server.wupitch.account.entity.Account;
import com.server.wupitch.area.Area;
import com.server.wupitch.area.AreaRepository;
import com.server.wupitch.club.dto.ClubListRes;
import com.server.wupitch.club.dto.CreateClubReq;
import com.server.wupitch.club.repository.ClubRepository;
import com.server.wupitch.club.repository.ClubRepositoryCustom;
import com.server.wupitch.configure.response.exception.CustomException;
import com.server.wupitch.configure.response.exception.CustomExceptionStatus;
import com.server.wupitch.configure.security.authentication.CustomUserDetails;
import com.server.wupitch.sports.entity.Sports;
import com.server.wupitch.sports.repository.SportsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    public Page<ClubListRes> getAllClubList(
            Integer page, Integer size, String sortBy, Boolean isAsc, Long areaId, Long sportsId,
            List<Integer> days, Integer startTime, Integer endTime, Integer memberCountValue, List<Integer> ageList) {

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);

        Area area = null;
        Sports sports = null;
        if(areaId != null){
            Optional<Area> optionalArea = areaRepository.findByAreaIdAndStatus(areaId, VALID);
            if(optionalArea.isPresent()) area = optionalArea.get();
        }
        if (sportsId != null) {
            Optional<Sports> optionalSports = sportsRepository.findBySportsIdAndStatus(sportsId, VALID);
            if(optionalSports.isPresent()) sports = optionalSports.get();
        }

        Page<Club> allClub = clubRepositoryCustom.findAllClub(pageable, area, sports, days, startTime, endTime, memberCountValue, ageList);

        return allClub.map(ClubListRes::new);

    }

    @Transactional
    public void createClub(CreateClubReq dto, CustomUserDetails customUserDetails) {
        Account account = accountRepository.findByoAuthIdAndStatus(customUserDetails.getOAuthId(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_FOUND));

        Sports sports = sportsRepository.findBySportsIdAndStatus(dto.getSportsId(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.SPORTS_NOT_FOUND));

        Area area = areaRepository.findByAreaIdAndStatus(dto.getAreaId(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AREA_NOT_FOUND));

        Club club = new Club(dto, account, sports, area);

        clubRepository.save(club);

    }
}
