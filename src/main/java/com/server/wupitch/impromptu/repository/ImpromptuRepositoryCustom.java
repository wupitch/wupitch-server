package com.server.wupitch.impromptu.repository;

import com.server.wupitch.area.Area;
import com.server.wupitch.impromptu.entity.Impromptu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ImpromptuRepositoryCustom {
    Page<Impromptu> getAllImpromptuList(Pageable pageable, Area area, Integer scheduleIndex, List<Integer> days, Integer memberCountIndex);
}
