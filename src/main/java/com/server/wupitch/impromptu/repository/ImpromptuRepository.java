package com.server.wupitch.impromptu.repository;

import com.server.wupitch.configure.entity.Status;
import com.server.wupitch.impromptu.entity.Impromptu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImpromptuRepository extends JpaRepository<Impromptu, Long> {
    Optional<Impromptu> findByImpromptuIdAndStatus(Long impromptuId, Status status);
}
