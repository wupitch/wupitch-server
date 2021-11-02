package com.server.wupitch.sports;

import com.server.wupitch.sports.dto.SportsRes;
import com.server.wupitch.sports.repository.SportsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.server.wupitch.configure.entity.Status.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SportsService {

    private final SportsRepository sportsRepository;

    public List<SportsRes> getAllSports() {
        return sportsRepository.findAllByStatusOrderBySportsIdAsc(VALID);
    }
}

