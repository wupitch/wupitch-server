package com.server.wupitch.area.dto;

import com.server.wupitch.area.Area;
import com.server.wupitch.configure.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AreaRes {

    private Long areaId;

    private String name;

    public AreaRes(Area area) {
        this.areaId = area.getAreaId();
        this.name = area.getName();
    }

}
