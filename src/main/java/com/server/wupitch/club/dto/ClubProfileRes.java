package com.server.wupitch.club.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.wupitch.account.entity.Account;
import com.server.wupitch.club.Club;
import com.server.wupitch.club.GuestInfo;
import com.server.wupitch.club.accountClubRelation.AccountClubRelation;
import com.server.wupitch.impromptu.accountImpromptuRelation.AccountImpromptuRelation;
import com.server.wupitch.impromptu.entity.Impromptu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClubProfileRes {

    private Long accountId;

    private String accountNickname;

    private String profileImage;

    private Integer ageNum;

    private String area;

    private String phoneNumber;

    private List<Long> sportsList;

    private String introduction;

    private Boolean isLeader;

    private Boolean isGuest;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isValid;

    private LocalDateTime addedAt;

    private LocalDate guestReserveTime;

    private Long clubId;

    private Long impromptuId;

    private Boolean isAuthAccountLeader;

    public void isLeader(Boolean isAuthAccountLeader) {
        this.isAuthAccountLeader = isAuthAccountLeader;
    }

    public ClubProfileRes(Account account, List<Long> sportsList) {
        this.accountId = account.getAccountId();
        this.accountNickname = account.getNickname();
        this.profileImage = account.getProfileImage();
        this.ageNum = account.getAgeNum();
        if (account.getArea() != null) this.area = account.getArea().getName();
        this.phoneNumber = account.getPhoneNumber();
        this.introduction = account.getIntroduction();
        this.sportsList = sportsList;
        this.introduction = account.getIntroduction();
    }

    public void addInfo(AccountClubRelation accountClubRelation){
        Account account = accountClubRelation.getAccount();
        Club club = accountClubRelation.getClub();
        if (club.getAccount().equals(account)) this.isLeader = true;
        else this.isLeader = false;
        this.isGuest = false;
        this.isValid = accountClubRelation.getIsValid();
        this.addedAt = accountClubRelation.getUpdatedAt();
        this.clubId = club.getClubId();
    }

    public void addInfo(GuestInfo guestInfo){
        this.isLeader = false;
        this.isGuest = true;
        this.isValid = guestInfo.getIsValid();
        this.addedAt = guestInfo.getUpdatedAt();
        this.guestReserveTime = guestInfo.getSelectedDate();
        this.clubId = guestInfo.getClub().getClubId();
    }

    public void addInfo(AccountImpromptuRelation accountImpromptuRelation) {
        Account account = accountImpromptuRelation.getAccount();
        Impromptu impromptu = accountImpromptuRelation.getImpromptu();
        if (impromptu.getAccount().equals(account)) this.isLeader = true;
        else this.isLeader = false;
        this.addedAt = accountImpromptuRelation.getUpdatedAt();
        this.impromptuId = impromptu.getImpromptuId();
    }


}
