package com.server.wupitch.club.accountClubRelation;

import com.server.wupitch.account.entity.Account;
import com.server.wupitch.club.Club;
import com.server.wupitch.configure.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountClubRelationRepository extends JpaRepository<AccountClubRelation, Long> {
    Optional<AccountClubRelation> findByStatusAndAccountAndClub(Status status, Account account, Club club);
    List<AccountClubRelation> findAllByStatusAndAccountAndIsSelect(Status status, Account account, Boolean isSelect);
    List<AccountClubRelation> findAllByStatusAndClubAndIsSelect(Status status, Club club, Boolean isSelect);
}
