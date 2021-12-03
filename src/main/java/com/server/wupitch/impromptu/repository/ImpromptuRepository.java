package com.server.wupitch.impromptu.repository;

import com.server.wupitch.area.Area;
import com.server.wupitch.configure.entity.Status;
import com.server.wupitch.impromptu.entity.Impromptu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ImpromptuRepository extends JpaRepository<Impromptu, Long> {
    Optional<Impromptu> findByImpromptuIdAndStatus(Long impromptuId, Status status);

    Page<Impromptu> findByStatusAndTitleContaining(Pageable pageable, Status valid, String title);
    Page<Impromptu> findByStatusAndTitleContainingAndDateAfter(Pageable pageable, Status valid, String title, LocalDate date);
    Page<Impromptu> findByStatusAndTitleContainingAndAreaAndDateAfter(Pageable pageable, Status valid, String title, Area area, LocalDate date);
}
