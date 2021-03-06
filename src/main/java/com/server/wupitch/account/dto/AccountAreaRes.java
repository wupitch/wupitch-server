package com.server.wupitch.account.dto;

import com.server.wupitch.area.Area;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountAreaRes {

    private Long areaId;

    private String areaName;

    public AccountAreaRes(Area area) {
        if (area == null) {
            this.areaId = null;
            this.areaName = null;
        }
        else{
            this.areaId = area.getAreaId();
            this.areaName = area.getName();
        }
    }

}
