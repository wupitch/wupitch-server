package com.server.wupitch.fcm;

import com.server.wupitch.account.AccountRepository;
import com.server.wupitch.account.entity.Account;
import com.server.wupitch.configure.response.exception.CustomException;
import com.server.wupitch.configure.response.exception.CustomExceptionStatus;
import com.server.wupitch.configure.security.authentication.CustomUserDetails;
import com.server.wupitch.fcm.dto.FcmNoticeRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.server.wupitch.configure.entity.Status.VALID;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FirebaseService {
    private final FcmNoticeRepository fcmNoticeRepository;
    private final AccountRepository accountRepository;

    public List<FcmNoticeRes> getFcmListByAuth(CustomUserDetails customUserDetails) {
        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_FOUND));
        List<FcmNotice> list = fcmNoticeRepository.findAllByAccountAndStatusOrderByCreatedAtDesc(account, VALID);
        return list.stream().map(FcmNoticeRes::new).collect(Collectors.toList());
    }

    @Transactional
    public void getReviewByFcmId(Long fcmId) {
        FcmNotice fcmNotice = fcmNoticeRepository.findByFcmIdAndStatus(fcmId, VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.FCM_NOT_FOUND));
        fcmNotice.getReview();
    }
}
