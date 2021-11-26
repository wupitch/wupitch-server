package com.server.wupitch.impromptu.accountImpromptuRelation;

import com.server.wupitch.account.entity.Account;
import com.server.wupitch.configure.entity.Status;
import com.server.wupitch.impromptu.entity.Impromptu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountImpromptuRelationRepository extends JpaRepository<AccountImpromptuRelation, Long> {
    Optional<AccountImpromptuRelation> findByStatusAndAccountAndImpromptu(Status status, Account account, Impromptu impromptu);
}
