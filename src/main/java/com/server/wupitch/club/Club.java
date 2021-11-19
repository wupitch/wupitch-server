package com.server.wupitch.club;

import com.server.wupitch.account.entity.Account;
import com.server.wupitch.area.Area;
import com.server.wupitch.club.dto.CreateClubReq;
import com.server.wupitch.configure.entity.BaseTimeEntity;
import com.server.wupitch.configure.entity.Status;
import com.server.wupitch.sports.entity.Sports;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static com.server.wupitch.configure.entity.Status.*;
import static javax.persistence.FetchType.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Club extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clubId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "accountId")
    private Account account;

    private String title;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "areaId")
    private Area area;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "sportsId")
    private Sports sports;

    private String introduction;

    private String inquiries;

    private Integer conference;

    private Integer guestConference;

    private Boolean monday;

    private Boolean tuesday;

    private Boolean wednesday;

    private Boolean thursday;

    private Boolean friday;

    private Boolean saturday;

    private Boolean sunday;

    private Double mondayStartTime;

    private Double mondayEndTime;

    private Double tuesdayStartTime;

    private Double tuesdayEndTime;

    private Double wednesdayStartTime;

    private Double wednesdayEndTime;

    private Double thursdayStartTime;

    private Double thursdayEndTime;

    private Double fridayStartTime;

    private Double fridayEndTime;

    private Double saturdayStartTime;

    private Double saturdayEndTime;

    private Double sundayStartTime;

    private Double sundayEndTime;

    private Long memberCount;

    private Boolean teenager;

    private Boolean twenties;

    private Boolean thirties;

    private Boolean forties;

    private Boolean moreAge;

    private String location;

    public Club(CreateClubReq dto, Account account, Sports sports, Area area) {

        this.sports = sports;
        this.area = area;
        this.status = VALID;
        this.memberCount = 0L;
        this.account = account;

        this.location = dto.getLocation();
        this.inquiries = dto.getInquiries();
        this.conference = dto.getConference();
        this.guestConference = dto.getGuestConference();
        this.memberCount = dto.getMemberCount();

        if (dto.getDays() != null) {
            for (Integer time : dto.getDays()) {
                if(time == 1){
                    this.monday = true;
                    this.mondayStartTime = dto.getMondayStartTime();
                    this.mondayEndTime = dto.getMondayEndTime();
                }
                else if (time == 2){
                    this.tuesday = true;
                    this.tuesdayStartTime = dto.getTuesdayStartTime();
                    this.tuesdayEndTime = dto.getTuesdayEndTime();
                }
                else if(time == 3){
                    this.wednesday = true;
                    this.wednesdayStartTime = dto.getWednesdayStartTime();
                    this.wednesdayEndTime = dto.getWednesdayEndTime();
                }
                else if(time == 4){
                    this.thursday = true;
                    this.thursdayStartTime = dto.getThursdayStartTime();
                    this.thursdayEndTime = dto.getThursdayEndTime();
                }
                else if(time == 5) {
                    this.friday = true;
                    this.fridayStartTime = dto.getFridayStartTime();
                    this.fridayEndTime = dto.getFridayEndTime();
                }
                else if(time == 6) {
                    this.saturday = true;
                    this.saturdayStartTime = dto.getSaturdayStartTime();
                    this.saturdayEndTime = dto.getSaturdayEndTime();
                }
                else{
                    this.sunday = true;
                    this.sundayStartTime = dto.getSundayStartTime();
                    this.sundayEndTime = dto.getSundayEndTime();
                }
            }
        }

        this.title = dto.getTitle();
        this.introduction = dto.getIntroduction();

        if (dto.getAgeList() != null) {
            for (Integer age : dto.getAgeList()) {
                if(age == 1) this.teenager = true;
                else if(age == 2) this.twenties = true;
                else if(age == 3) this.thirties = true;
                else if(age == 4) this.forties = true;
                else this.moreAge = true;
            }
        }
    }

}
