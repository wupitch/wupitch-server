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

    private Boolean monday;

    private Boolean tuesday;

    private Boolean wednesday;

    private Boolean thursday;

    private Boolean friday;

    private Boolean saturday;

    private Boolean sunday;

    private Integer startTime;

    private Integer endTime;

    private Long memberCount;

    private Boolean teenager;

    private Boolean twenties;

    private Boolean thirties;

    private Boolean forties;

    private Boolean moreAge;

    public Club(CreateClubReq dto, Account account, Sports sports, Area area) {

        this.sports = sports;
        this.area = area;
        this.status = VALID;
        this.memberCount = 0L;
        this.account = account;

        this.startTime = dto.getStartTime();
        this.endTime = dto.getEndTime();

        if (dto.getDays() != null) {
            for (Integer time : dto.getDays()) {
                if(time == 1) this.monday = true;
                else if (time == 2) this.tuesday = true;
                else if(time == 3) this.wednesday = true;
                else if(time == 4) this.thursday = true;
                else if(time == 5) this.friday = true;
                else if(time == 6) this.saturday = true;
                else this.sunday = true;
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
