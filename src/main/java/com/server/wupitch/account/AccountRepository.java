package com.server.wupitch.account;

import com.server.wupitch.account.entity.Account;
import com.server.wupitch.configure.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByOAuthIdAndStatus(String oAuathId, Status valid);
    Optional<Account> findByEmailAndStatus(String email, Status valid);
}
