package com.server.wupitch.account;

import com.server.wupitch.account.entity.Account;
import com.server.wupitch.configure.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByoAuthIdAndStatus(String oAuthId, Status valid);
    Optional<Account> findByEmailAndStatus(String email, Status valid);
    Optional<Account> findByNicknameAndStatus(String nickname, Status valid);
    Optional<Account> findByEmail(String email);
    Optional<Account> findByNickname(String nickname);
}
