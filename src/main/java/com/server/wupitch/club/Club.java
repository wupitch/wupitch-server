package com.server.wupitch.club;

import com.server.wupitch.account.entity.Account;
import com.server.wupitch.area.Area;
import com.server.wupitch.club.dto.CreateClubReq;
import com.server.wupitch.club.dto.Schedule;
import com.server.wupitch.configure.entity.BaseTimeEntity;
import com.server.wupitch.configure.entity.Status;
import com.server.wupitch.configure.response.exception.CustomException;
import com.server.wupitch.configure.response.exception.CustomExceptionStatus;
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

    private String materials;

    private String crewImage;

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
        this.materials = dto.getMaterials();

        if (dto.getScheduleList().size() > 0) {
            for (Schedule schedule : dto.getScheduleList()) {
                Integer dayIdx = schedule.getDayIdx();
                Double startTime = schedule.getStartTime();
                Double endTime = schedule.getEndTime();
                if(dayIdx == 1){
                    if(this.monday != null) throw new CustomException(CustomExceptionStatus.SCHEDULE_ALREADY_EXIST);
                    this.monday = true;
                    this.mondayStartTime = startTime;
                    this.mondayEndTime = endTime;
                }
                else if (dayIdx == 2){
                    if(this.tuesday != null) throw new CustomException(CustomExceptionStatus.SCHEDULE_ALREADY_EXIST);
                    this.tuesday = true;
                    this.tuesdayStartTime = startTime;
                    this.tuesdayEndTime = endTime;
                }
                else if(dayIdx == 3){
                    if(this.wednesday != null) throw new CustomException(CustomExceptionStatus.SCHEDULE_ALREADY_EXIST);
                    this.wednesday = true;
                    this.wednesdayStartTime = startTime;
                    this.wednesdayEndTime = endTime;
                }
                else if(dayIdx == 4){
                    if(this.thursday != null) throw new CustomException(CustomExceptionStatus.SCHEDULE_ALREADY_EXIST);
                    this.thursday = true;
                    this.thursdayStartTime = startTime;
                    this.thursdayEndTime = endTime;
                }
                else if(dayIdx == 5) {
                    if(this.friday != null) throw new CustomException(CustomExceptionStatus.SCHEDULE_ALREADY_EXIST);
                    this.friday = true;
                    this.fridayStartTime = startTime;
                    this.fridayEndTime = endTime;
                }
                else if(dayIdx == 6) {
                    if(this.saturday != null) throw new CustomException(CustomExceptionStatus.SCHEDULE_ALREADY_EXIST);
                    this.saturday = true;
                    this.saturdayStartTime = startTime;
                    this.saturdayEndTime = endTime;
                }
                else{
                    if(this.sunday != null) throw new CustomException(CustomExceptionStatus.SCHEDULE_ALREADY_EXIST);
                    this.sunday = true;
                    this.sundayStartTime = startTime;
                    this.sundayEndTime = endTime;
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
