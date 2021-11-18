package com.server.wupitch.extra.repository;

import com.server.wupitch.configure.entity.Status;
import com.server.wupitch.extra.dto.ExtraRes;
import com.server.wupitch.extra.entity.Extra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExtraRepository extends JpaRepository<Extra, Long> {

    List<ExtraRes> findAllByStatusOrderByExtraIdAsc(Status status);

    Optional<Extra> findAllByExtraIdAndStatus(Long extraId, Status status);

}
