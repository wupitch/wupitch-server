package com.server.wupitch.account.dto;

import com.server.wupitch.account.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRes {

    private Long accountId;

    private String accountNickname;

    private String profileImage;

    private Integer ageNum;

    private String area;

    private String phoneNumber;

    private List<Long> sportsList;

    private String introduction;

    public ProfileRes(Account account, List<Long> sportsList) {
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


}
