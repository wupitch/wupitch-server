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

    private List<String> ageTable = new ArrayList<>();

    private String introduction;

    private Boolean monday;

    private Boolean tuesday;

    private Boolean wednesday;

    private Boolean thursday;

    private Boolean friday;

    private Boolean saturday;

    private Boolean sunday;

    private Double mondayStartTime;

    private Double mondayEndTime;

    private Double tuesdayStartTime;

    private Double tuesdayEndTime;

    private Double wednesdayStartTime;

    private Double wednesdayEndTime;

    private Double thursdayStartTime;

    private Double thursdayEndTime;

    private Double fridayStartTime;

    private Double fridayEndTime;

    private Double saturdayStartTime;

    private Double saturdayEndTime;

    private Double sundayStartTime;

    private Double sundayEndTime;

    public ClubListRes(Club club) {
        this.clubId = club.getClubId();

        Sports sports = club.getSports();

        this.sportsId = sports.getSportsId();

        this.sportsName = sports.getName();

        this.areaName = club.getArea().getName();

        this.clubTitle = club.getTitle();

        this.introduction = club.getIntroduction();

        if(club.getMonday() != null && club.getMonday()) {
            this.monday = true;
            this.mondayStartTime = club.getMondayStartTime();
            this.mondayEndTime = club.getMondayEndTime();
        }
        if(club.getTuesday() != null && club.getTuesday()){
            this.tuesday = true;
            this.tuesdayStartTime = club.getTuesdayStartTime();
            this.tuesdayEndTime = club.getTuesdayEndTime();
        }
        if(club.getWednesday() != null && club.getWednesday()){
            this.wednesday = true;
            this.wednesdayStartTime = club.getWednesdayStartTime();
            this.wednesdayEndTime = club.getWednesdayEndTime();
        }
        if(club.getThursday() != null && club.getThursday()){
            this.thursday = true;
            this.thursdayStartTime = club.getThursdayStartTime();
            this.thursdayEndTime = club.getThursdayEndTime();
        }
        if(club.getFriday() != null && club.getFriday()){
            this.friday = true;
            this.fridayStartTime = club.getFridayStartTime();
            this.fridayEndTime = club.getFridayEndTime();
        }
        if(club.getSaturday() != null && club.getSaturday()){
            this.saturday = true;
            this.saturdayStartTime = club.getSaturdayStartTime();
            this.saturdayEndTime = club.getSaturdayEndTime();
        }
        if(club.getSunday() != null && club.getSunday()){
            this.sunday = true;
            this.sundayStartTime = club.getSundayStartTime();
            this.saturdayEndTime = club.getSaturdayEndTime();
        }

        if (club.getTeenager()  != null && club.getTeenager()) ageTable.add("10대");
        if (club.getTwenties() != null && club.getTwenties()) ageTable.add("20대");
        if (club.getThirties() != null && club.getThirties()) ageTable.add("30대");
        if (club.getForties() != null && club.getForties()) ageTable.add("40대");
        if (club.getMoreAge() != null && club.getMoreAge()) ageTable.add("50대 이상");


    }

}
