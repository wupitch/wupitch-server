package com.server.wupitch.post.repository;

import com.server.wupitch.account.entity.Account;
import com.server.wupitch.configure.entity.Status;
import com.server.wupitch.post.entity.AccountPostRelation;
import com.server.wupitch.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountPostRelationRepository extends JpaRepository<AccountPostRelation, Long> {

    Optional<AccountPostRelation> findByAccountAndPostAndStatus(Account account, Post post, Status status);

}
