package com.server.wupitch.club;

import com.server.wupitch.area.Area;
import com.server.wupitch.configure.entity.BaseTimeEntity;
import com.server.wupitch.configure.entity.Status;
import com.server.wupitch.sports.entity.Sports;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    private Integer ageValue;

    private Boolean teenager;

    private Boolean twenties;

    private Boolean thirties;

    private Boolean forties;

    private Boolean moreAge;

}
