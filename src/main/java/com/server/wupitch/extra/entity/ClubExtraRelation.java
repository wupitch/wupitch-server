package com.server.wupitch.extra.entity;

import com.server.wupitch.club.Club;
import com.server.wupitch.configure.entity.BaseTimeEntity;
import com.server.wupitch.configure.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.server.wupitch.configure.entity.Status.*;
import static javax.persistence.FetchType.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ClubExtraRelation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long relationId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "clubId")
    private Club club;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "extraId")
    private Extra extra;

    public ClubExtraRelation(Club club, Extra extra) {
        this.status = VALID;
        this.club = club;
        this.extra = extra;
    }

}
