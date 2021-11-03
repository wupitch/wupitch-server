package com.server.wupitch.sports.dto;

import com.server.wupitch.area.Area;
import com.server.wupitch.sports.entity.Sports;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SportsRes {

    private Long sportsId;

    private String name;

    public SportsRes(Sports sports) {
        this.sportsId = sports.getSportsId();
        this.name = sports.getName();
    }

}
