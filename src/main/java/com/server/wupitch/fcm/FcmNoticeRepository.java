package com.server.wupitch.fcm;

import com.server.wupitch.account.entity.Account;
import com.server.wupitch.configure.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FcmNoticeRepository extends JpaRepository<FcmNotice, Long> {
    List<FcmNotice> findAllByAccountAndStatusOrderByCreatedAtDesc(Account account, Status status);
    Optional<FcmNotice> findByFcmIdAndStatus(Long fcmId, Status status);
}
