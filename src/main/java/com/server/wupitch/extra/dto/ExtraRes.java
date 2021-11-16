package com.server.wupitch.extra.dto;

import com.server.wupitch.area.Area;
import com.server.wupitch.extra.entity.Extra;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExtraRes {

    private Long extraId;

    private String info;

    public ExtraRes(Extra extra) {
        this.extraId = extra.getExtraId();
        this.info = extra.getInfo();
    }

}
