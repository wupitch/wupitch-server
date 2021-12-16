package com.server.wupitch.club.repository;

import com.server.wupitch.account.entity.Account;
import com.server.wupitch.club.Club;
import com.server.wupitch.club.GuestInfo;
import com.server.wupitch.configure.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GuestInfoRepository extends JpaRepository<GuestInfo, Long> {
    List<GuestInfo> findAllByAccountAndClubAndStatusAndSelectedDateAfter(Account account, Club club, Status status, LocalDate date);
    List<GuestInfo> findAllByClubAndStatusAndSelectedDateAfter(Club club, Status status, LocalDate date);
    Optional<GuestInfo> findByStatusAndAccountAndClubAndSelectedDateAfter(Status status, Account account, Club club, LocalDate localDate);
    Optional<GuestInfo> findByClubAndAccountAndStatusAndSelectedDateAfter(Club club, Account account, Status status, LocalDate date);
}
