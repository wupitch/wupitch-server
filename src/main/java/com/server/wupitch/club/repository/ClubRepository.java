package com.server.wupitch.club.repository;

import com.server.wupitch.area.Area;
import com.server.wupitch.club.Club;
import com.server.wupitch.configure.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {

    Optional<Club> findByClubIdAndStatus(Long id, Status status);
    Page<Club> findByStatusAndTitleContaining(Pageable pageable, Status status, String title);
    Page<Club> findByStatusAndTitleContainingAndArea(Pageable pageable, Status status, String title, Area area);
}
