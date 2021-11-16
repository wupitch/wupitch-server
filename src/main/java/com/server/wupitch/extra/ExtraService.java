package com.server.wupitch.extra;

import com.server.wupitch.area.dto.AreaRes;
import com.server.wupitch.extra.dto.ExtraRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.server.wupitch.configure.entity.Status.VALID;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ExtraService {

    private final ExtraRepository extraRepository;

    public List<ExtraRes> getAllExtras() {
        return extraRepository.findAllByStatusOrderByExtraIdAsc(VALID);
    }
}
