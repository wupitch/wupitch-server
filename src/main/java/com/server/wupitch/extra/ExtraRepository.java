package com.server.wupitch.extra;

import com.server.wupitch.configure.entity.Status;
import com.server.wupitch.extra.dto.ExtraRes;
import com.server.wupitch.extra.entity.Extra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExtraRepository extends JpaRepository<Extra, Long> {

    List<ExtraRes> findAllByStatusOrderByExtraIdAsc(Status status);

}
