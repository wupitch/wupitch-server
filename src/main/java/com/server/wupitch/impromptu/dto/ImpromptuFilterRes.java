package com.server.wupitch.impromptu.dto;


import com.server.wupitch.account.entity.Account;
import com.server.wupitch.area.Area;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImpromptuFilterRes {

    private Long impromptuPickAreaId;

    private String impromptuPickAreaName;

    private Integer impromptuPickScheduleIndex;

    private List<Integer> impromptuPickDays = new ArrayList<>();

    private Integer impromptuPickMemberCountValue;


    public ImpromptuFilterRes(Account account, Area area) {
        this.impromptuPickAreaId = account.getImpromptuPickAreaId();
        if (area != null) {
            this.impromptuPickAreaName = area.getName();
        }
        this.impromptuPickScheduleIndex = account.getImpromptuPickScheduleIndex();
        String[] tempList;
        if (account.getImpromptuPickDays() != null) {
            this.impromptuPickDays = new ArrayList<>();
            tempList = account.getImpromptuPickDays().split(",");
            for (String s : tempList) {
                this.impromptuPickDays.add(Integer.parseInt(s));
            }
        } else this.impromptuPickDays = null;
        this.impromptuPickMemberCountValue = account.getImpromptuPickMemberCountValue();
    }

}
