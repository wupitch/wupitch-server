package com.server.wupitch.account.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.wupitch.account.dto.AccountAuthDto;
import com.server.wupitch.account.dto.AccountInformReq;
import com.server.wupitch.account.entity.enumtypes.OAuthType;
import com.server.wupitch.area.Area;
import com.server.wupitch.configure.entity.BaseTimeEntity;
import com.server.wupitch.configure.entity.Status;
import com.server.wupitch.configure.response.exception.CustomException;
import com.server.wupitch.configure.response.exception.CustomExceptionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.server.wupitch.account.entity.enumtypes.RoleType;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.List;

import static com.server.wupitch.account.entity.enumtypes.RoleType.*;
import static com.server.wupitch.configure.entity.Status.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String nickname;

    private String email;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    private String profileImage;

    private String password;

    @Enumerated(EnumType.STRING)
    private OAuthType oAuth;

    private String oAuthId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "areaId")
    private Area area;

    private String introduction;

    private Integer ageNum;

    private String phoneNumber;

    private Boolean isPushAgree;

    private String identification;

    private String deviceToken;

    private Long crewPickAreaId;

    private String crewPickAgeList;

    private String crewPickDays;

    private Integer crewPickMemberCountValue;

    private String crewPickSportsList;

    private Long impromptuPickAreaId;

    private String impromptuPickAreaName;

    private Integer impromptuPickScheduleIndex;

    private String impromptuPickDays;

    private Integer impromptuPickMemberCountValue;

    private Boolean isChecked;

    public void setCheckInfo(Boolean chk){
        this.isChecked = chk;
    }

    public void saveFilterInfo(
            List<Integer> ageList, Long crewPickAreaId, List<Integer> days, Integer crewPickMemberCountValue, List<Long> sportsList
    ) {
        if (ageList != null) {
            StringBuilder tempAge = new StringBuilder();
            for (int i = 0; i < ageList.size(); i++) {
                if(ageList.size() -1 == i){ tempAge.append(ageList.get(i));}
                else{
                    tempAge.append(ageList.get(i));
                    tempAge.append(",");
                }
            }
            this.crewPickAgeList = tempAge.toString();
        } else this.crewPickAgeList = null;
        this.crewPickAreaId = crewPickAreaId;
        if (days != null) {
            StringBuilder tempDays = new StringBuilder();
            for (int i = 0; i < days.size(); i++) {
                if(days.size() -1 == i){ tempDays.append(days.get(i));}
                else{
                    tempDays.append(days.get(i));
                    tempDays.append(",");
                }
            }
            this.crewPickDays = tempDays.toString();
        } else this.crewPickDays = null;
        this.crewPickMemberCountValue = crewPickMemberCountValue;
        if (sportsList != null) {
            StringBuilder tempSports = new StringBuilder();
            for (int i = 0; i < sportsList.size(); i++) {
                if(sportsList.size() -1 == i){ tempSports.append(sportsList.get(i));}
                else{
                    tempSports.append(sportsList.get(i));
                    tempSports.append(",");
                }
            }
            this.crewPickSportsList = tempSports.toString();
        } else this.crewPickSportsList = null;
    }

    public void saveImpromptuFilterInfo(
            Long areaId, Integer scheduleIndex, List<Integer> days, Integer memberCountIndex
    ) {
        this.impromptuPickAreaId = areaId;
        if (days != null) {
            StringBuilder tempDays = new StringBuilder();
            for (int i = 0; i < days.size(); i++) {
                if(days.size() -1 == i){ tempDays.append(days.get(i));}
                else{
                    tempDays.append(days.get(i));
                    tempDays.append(",");
                }
            }
            this.impromptuPickDays = tempDays.toString();
        } else this.impromptuPickDays = null;
        this.impromptuPickScheduleIndex = scheduleIndex;
        this.impromptuPickMemberCountValue = memberCountIndex;
    }

    public void registerProfileImage(String filePath){
        this.profileImage = filePath;
    }

    public void registerIdentification(String filePath){
        this.identification = filePath;
    }

    public void restoreAccount() {
        this.status = VALID;
    }

    public static Account createAccount(AccountAuthDto dto, OAuthType oAuth) {
        return Account.builder()
                .status(VALID)
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .role(ROLE_USER)
                .password(dto.getPassword())
                .oAuth(oAuth)
                .isChecked(false)
                .build();
    }

    public AccountAuthDto getAccountInfoDto() {
        return AccountAuthDto.builder()
                .accountId(this.accountId)
                .email(this.email)
                .nickname(this.nickname)
                .profileImageUrl(this.profileImage)
                .introduce(this.introduction)
                .isPushAgree(this.isPushAgree)
                .profileImageUrl(this.profileImage)
                .isChecked(this.isChecked)
                .build();
    }

    public void setAccountInfoByDto(AccountInformReq dto) {
        if (dto.getNickname() != null) this.nickname = dto.getNickname();

        if(dto.getAgeNum() != null) this.ageNum = dto.getAgeNum();

        if(dto.getIntroduce() != null) this.introduction = dto.getIntroduce();

        if(dto.getPhoneNumber() != null) this.phoneNumber = dto.getPhoneNumber();

        if(dto.getIsPushAgree() != null)this.isPushAgree = dto.getIsPushAgree();
    }

    public void setAccountArea(Area area) {
        this.area = area;
    }

    public static Account createAccount(AccountAuthDto dto) {

        return Account.builder()
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .password(dto.getPassword())
                .status(VALID)
                .role(ROLE_USER)
                .introduction(dto.getIntroduce())
                .isPushAgree(dto.getIsPushAgree())
                .deviceToken(dto.getDeviceToken())
                .isChecked(false)
                .build();
    }

    public void toggleValid() {
        if(this.status.equals(VALID)) this.status = DELETED;
        else this.status = VALID;
    }

    public void toggleAlarmInfo() {
        if(this.isPushAgree == null || !this.isPushAgree) this.isPushAgree = true;
        else this.isPushAgree = false;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeDeviceToken(String deviceToken) { this.deviceToken = deviceToken; }
}
