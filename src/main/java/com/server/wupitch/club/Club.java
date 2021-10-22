package com.server.wupitch.club;

import com.server.entity.Area;
import com.server.wupitch.configure.entity.BaseTimeEntity;
import com.server.wupitch.configure.entity.Status;
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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "areaId")
    private Area area;

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


}
