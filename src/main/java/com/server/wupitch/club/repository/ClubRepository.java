package com.server.wupitch.club.repository;

import com.server.wupitch.club.Club;
import com.server.wupitch.configure.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {

    Optional<Club> findByClubIdAndStatus(Long id, Status status);
}
