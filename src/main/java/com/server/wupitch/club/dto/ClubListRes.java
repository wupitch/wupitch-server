package com.server.wupitch.club.dto;

import com.server.wupitch.club.Club;
import com.server.wupitch.sports.entity.Sports;
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
        String endTime;

        if(club.getMonday() != null && club.getMonday()) {
            if (club.getMondayStartTime() != null) startTime = club.getMondayStartTime()+":00";
            else startTime = "00:00";
            if(club.getMondayEndTime() != null) endTime = club.getMondayEndTime() + ":00";
            else endTime = "24:00";
            timeTable.add("월요일 " + startTime + " " + endTime );
        }
        if(club.getTuesday() != null && club.getTuesday()){
            if (club.getTuesdayStartTime() != null) startTime = club.getTuesdayStartTime()+":00";
            else startTime = "00:00";
            if(club.getTuesdayEndTime() != null) endTime = club.getTuesdayEndTime() + ":00";
            else endTime = "24:00";
            timeTable.add("화요일 " + startTime + " " + endTime );
        }
        if(club.getWednesday() != null && club.getWednesday()){
            if (club.getWednesdayStartTime() != null) startTime = club.getWednesdayStartTime()+":00";
            else startTime = "00:00";
            if(club.getWednesdayEndTime() != null) endTime = club.getWednesdayEndTime() + ":00";
            else endTime = "24:00";
            timeTable.add("수요일 " + startTime + " " + endTime );
        }
        if(club.getThursday() != null && club.getThursday()){
            if (club.getThursdayStartTime() != null) startTime = club.getThursdayStartTime()+":00";
            else startTime = "00:00";
            if(club.getThursdayEndTime() != null) endTime = club.getThursdayEndTime() + ":00";
            else endTime = "24:00";
            timeTable.add("목요일 " + startTime + " " + endTime );
        }
        if(club.getFriday() != null && club.getFriday()){
            if (club.getFridayStartTime() != null) startTime = club.getFridayStartTime()+":00";
            else startTime = "00:00";
            if(club.getFridayEndTime() != null) endTime = club.getFridayEndTime() + ":00";
            else endTime = "24:00";
            timeTable.add("금요일 " + startTime + " " + endTime );
        }
        if(club.getSaturday() != null && club.getSaturday()){
            if (club.getSaturdayStartTime() != null) startTime = club.getSaturdayStartTime()+":00";
            else startTime = "00:00";
            if(club.getSaturdayEndTime() != null) endTime = club.getSaturdayEndTime() + ":00";
            else endTime = "24:00";
            timeTable.add("토요일 " + startTime + " " + endTime );
        }
        if(club.getSunday() != null && club.getSunday()){
            if (club.getSundayStartTime() != null) startTime = club.getSundayStartTime()+":00";
            else startTime = "00:00";
            if(club.getSundayEndTime() != null) endTime = club.getSundayEndTime() + ":00";
            else endTime = "24:00";
            timeTable.add("일요일 " + startTime + " " + endTime );
        }

        if (club.getTeenager()  != null && club.getTeenager()) ageTable.add("10대");
        if (club.getTwenties() != null && club.getTwenties()) ageTable.add("20대");
        if (club.getThirties() != null && club.getThirties()) ageTable.add("30대");
        if (club.getForties() != null && club.getForties()) ageTable.add("40대");
        if (club.getMoreAge() != null && club.getMoreAge()) ageTable.add("50대 이상");


    }

}
