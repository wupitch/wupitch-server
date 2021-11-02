package com.server.wupitch.area;

import com.server.wupitch.area.dto.AreaRes;
import com.server.wupitch.configure.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.server.wupitch.configure.entity.Status.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AreaService {

    private final AreaRepository areaRepository;


    public List<AreaRes> getAllAreas() {
        return areaRepository.findAllByStatusOrderByAreaIdAsc(VALID);
    }
}
