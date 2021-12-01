package com.server.wupitch.club.dto;


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
public class CrewFilterRes {

    private Long crewPickAreaId;

    private String crewPickAreaName;

    private List<Long> crewPickAgeList = new ArrayList<>();

    private List<Integer> crewPickDays  = new ArrayList<>();

    private Integer crewPickMemberCountValue;

    private List<Long> crewPickSportsList = new ArrayList<>();

    public CrewFilterRes(Account account, Area area) {
        this.crewPickAreaId = account.getCrewPickAreaId();
        if (area != null) {
            this.crewPickAreaName = area.getName();
        }
        String[] tempList;
        if (account.getCrewPickAgeList() != null) {
            this.crewPickAgeList = new ArrayList<>();
            tempList = account.getCrewPickAgeList().split(",");
            for (String s : tempList) {
                this.crewPickAgeList.add(Long.parseLong(s));
            }
        } else this.crewPickAgeList = null;
        if (account.getCrewPickDays() != null) {
            this.crewPickDays = new ArrayList<>();
            tempList = account.getCrewPickDays().split(",");
            for (String s : tempList) {
                this.crewPickDays.add(Integer.parseInt(s));
            }
        } else this.crewPickDays = null;
        this.crewPickMemberCountValue = account.getCrewPickMemberCountValue();
        if (account.getCrewPickSportsList() != null) {
            this.crewPickSportsList = new ArrayList<>();
            tempList = account.getCrewPickSportsList().split(",");
            for (String s : tempList) {
                this.crewPickSportsList.add(Long.parseLong(s));
            }
        } else this.crewPickSportsList = null;
    }

}
