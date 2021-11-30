package com.server.wupitch.club.dto;


import com.server.wupitch.account.entity.Account;
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

    private List<Long> crewPickAgeList = new ArrayList<>();

    private List<Integer> crewPickDays  = new ArrayList<>();

    private Integer crewPickMemberCountValue;

    private List<Long> crewPickSportsList = new ArrayList<>();

    public CrewFilterRes(Account account) {
        this.crewPickAreaId = account.getCrewPickAreaId();
        String[] tempList = account.getCrewPickAgeList().split(",");
        for (String s : tempList) {
            this.crewPickAgeList.add(Long.parseLong(s));
        }
        tempList = account.getCrewPickDays().split(",");
        for (String s : tempList) {
            this.crewPickDays.add(Integer.parseInt(s));
        }
        this.crewPickMemberCountValue = account.getCrewPickMemberCountValue();
        tempList = account.getCrewPickSportsList().split(",");
        for (String s : tempList) {
            this.crewPickSportsList.add(Long.parseLong(s));
        }
    }

}
