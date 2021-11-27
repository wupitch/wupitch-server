package com.server.wupitch.sports.repository;

import com.server.wupitch.account.entity.Account;
import com.server.wupitch.configure.entity.Status;
import com.server.wupitch.sports.entity.AccountSportsRelation;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountSportsRelationRepository extends JpaRepository<AccountSportsRelation, Long> {
    List<AccountSportsRelation> findAllByAccountAndStatus(Account account, Status status);
}
