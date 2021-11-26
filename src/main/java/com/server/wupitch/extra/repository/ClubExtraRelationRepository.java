package com.server.wupitch.extra.repository;

import com.server.wupitch.club.Club;
import com.server.wupitch.configure.entity.Status;
import com.server.wupitch.extra.entity.ClubExtraRelation;
import com.server.wupitch.extra.entity.Extra;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubExtraRelationRepository extends JpaRepository<ClubExtraRelation, Long> {
    List<ClubExtraRelation> findAllByClubAndStatus(Club club, Status status);
}
