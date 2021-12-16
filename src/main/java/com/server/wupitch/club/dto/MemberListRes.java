package com.server.wupitch.club.dto;

import com.server.wupitch.account.entity.Account;
import com.server.wupitch.club.Club;
import com.server.wupitch.club.GuestInfo;
import com.server.wupitch.club.accountClubRelation.AccountClubRelation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberListRes implements Comparable<MemberListRes>{


    private Long accountId;
    private String accountNickname;
    private Boolean isLeader;
    private Boolean isGuest;
    private Boolean isValid;
    private LocalDateTime addedAt;
    private LocalDate guestReserveTime;
    private String profileImage;

    @Override
    public int compareTo(@NotNull MemberListRes other) {
        if (!this.isLeader && !other.isLeader) {
            if (this.addedAt.isBefore(other.addedAt)) return -1;
            else return 1;
        }
        else if(this.isLeader) return -1;
        else return 1;
    }

    public MemberListRes(AccountClubRelation accountClubRelation) {
        Account account = accountClubRelation.getAccount();
        Club club = accountClubRelation.getClub();
        this.accountId = account.getAccountId();
        this.accountNickname = account.getNickname();
        if (club.getAccount().equals(account)) this.isLeader = true;
        else this.isLeader = false;
        this.isGuest = false;
        this.isValid = accountClubRelation.getIsValid();
        this.addedAt = accountClubRelation.getUpdatedAt();
        this.profileImage = account.getProfileImage();
    }

    public MemberListRes(GuestInfo guestInfo) {
        Account account = guestInfo.getAccount();
        this.accountId = account.getAccountId();
        this.accountNickname = account.getNickname();
        this.isLeader = false;
        this.isGuest = true;
        this.isValid = guestInfo.getIsValid();
        this.addedAt = guestInfo.getUpdatedAt();
        this.guestReserveTime = guestInfo.getSelectedDate();
        this.profileImage = account.getProfileImage();
    }
}