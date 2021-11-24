package com.server.wupitch.impromptu;

import com.server.wupitch.account.AccountRepository;
import com.server.wupitch.account.entity.Account;
import com.server.wupitch.area.Area;
import com.server.wupitch.area.AreaRepository;
import com.server.wupitch.configure.response.exception.CustomException;
import com.server.wupitch.configure.response.exception.CustomExceptionStatus;
import com.server.wupitch.configure.security.authentication.CustomUserDetails;
import com.server.wupitch.impromptu.dto.CreateImpromptuReq;
import com.server.wupitch.impromptu.entity.Impromptu;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.server.wupitch.configure.entity.Status.VALID;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ImpromptuService {

    private final AccountRepository accountRepository;
    private final AreaRepository areaRepository;
    private final ImpromptuRepository impromptuRepository;

    @Transactional
    public Long createImpromptu(CreateImpromptuReq dto, CustomUserDetails customUserDetails) {

        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_FOUND));

        Area area = areaRepository.findByAreaIdAndStatus(dto.getAreaId(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AREA_NOT_FOUND));

        Impromptu impromptu = new Impromptu(account, area, dto);

        Impromptu save = impromptuRepository.save(impromptu);

        return save.getImpromptuId();

    }
}
