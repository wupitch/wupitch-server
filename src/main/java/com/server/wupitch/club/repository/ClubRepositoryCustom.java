package com.server.wupitch.club.repository;

import com.server.wupitch.area.Area;
import com.server.wupitch.club.Club;
import com.server.wupitch.sports.entity.Sports;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClubRepositoryCustom {

    Page<Club> findAllClub(Pageable pageable, Area area, Sports sports, List<Integer> days, Integer startTime, Integer endTime, Integer minCnt, List<Integer> ageList);

}
