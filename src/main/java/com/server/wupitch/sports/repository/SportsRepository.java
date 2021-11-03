package com.server.wupitch.sports.repository;

import com.server.wupitch.configure.entity.Status;
import com.server.wupitch.sports.dto.SportsRes;
import com.server.wupitch.sports.entity.Sports;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SportsRepository extends JpaRepository<Sports, Long> {

    Optional<Sports> findBySportsIdAndStatus(Long sportsId, Status status);
    List<SportsRes> findAllByStatusOrderBySportsIdAsc(Status status);
}
