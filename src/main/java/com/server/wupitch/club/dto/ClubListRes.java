package com.server.wupitch.club.dto;

import com.server.wupitch.club.Club;
import com.server.wupitch.sports.entity.Sports;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClubListRes {

    private Long clubId;

    private Long sportsId;

    private String sportsName;

    private String areaName;

    private String clubTitle;

    private List<String> timeTable = new ArrayList<>();

    private List<String> ageTable = new ArrayList<>();

    private String introduction;

    public ClubListRes(Club club) {
        this.clubId = club.getClubId();

        Sports sports = club.getSports();

        this.sportsId = sports.getSportsId();

        this.sportsName = sports.getName();

        this.areaName = club.getArea().getName();

        this.clubTitle = club.getTitle();

        this.introduction = club.getIntroduction();

        String startTime;
        if (club.getStartTime() != null) startTime = club.getStartTime()+":00";
        else startTime = "00:00";

        String endTime;
        if(club.getEndTime() != null) endTime = club.getEndTime() + ":00";
        else endTime = "24:00";

        if(club.getMonday() != null && club.getMonday()) timeTable.add("월요일 " + startTime + " " + endTime );
        if(club.getTuesday() != null && club.getTuesday()) timeTable.add("화요일 " + startTime + " " + endTime );
        if(club.getWednesday() != null && club.getWednesday()) timeTable.add("수요일 " + startTime + " " + endTime );
        if(club.getThursday() != null && club.getThursday()) timeTable.add("목요일 " + startTime + " " + endTime );
        if(club.getFriday() != null && club.getFriday()) timeTable.add("금요일 " + startTime + " " + endTime );
        if(club.getSaturday() != null && club.getSaturday()) timeTable.add("토요일 " + startTime + " " + endTime );
        if(club.getSunday() != null && club.getSunday()) timeTable.add("일요일 " + startTime + " " + endTime );

        if (club.getTeenager()  != null && club.getTeenager()) ageTable.add("10대");
        if (club.getTwenties() != null && club.getTwenties()) ageTable.add("20대");
        if (club.getThirties() != null && club.getThirties()) ageTable.add("30대");
        if (club.getForties() != null && club.getForties()) ageTable.add("40대");
        if (club.getMoreAge() != null && club.getMoreAge()) ageTable.add("50대 이상");


    }

}
