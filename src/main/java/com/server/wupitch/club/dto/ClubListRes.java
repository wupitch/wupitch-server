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
            if(club.getMondayStartTime() == null) this.mondayStartTime = 00.00;
            else this.mondayStartTime = club.getMondayStartTime();
            if(club.getMondayEndTime() == null) this.mondayEndTime = 24.00;
            else this.mondayEndTime = club.getMondayEndTime();
        }
        if(club.getTuesday() != null && club.getTuesday()){
            this.tuesday = true;
            if(club.getTuesdayStartTime() == null) this.tuesdayStartTime = 00.00;
            else this.tuesdayStartTime = club.getTuesdayStartTime();
            if(club.getTuesdayEndTime() == null) this.tuesdayEndTime = 24.00;
            else this.tuesdayEndTime = club.getTuesdayEndTime();
        }
        if(club.getWednesday() != null && club.getWednesday()){
            this.wednesday = true;
            if(club.getWednesdayStartTime() == null) this.wednesdayStartTime = 00.00;
            else this.wednesdayStartTime = club.getWednesdayStartTime();
            if(club.getWednesdayEndTime() == null) this.wednesdayEndTime = 24.00;
            else this.wednesdayEndTime = club.getWednesdayEndTime();
        }
        if(club.getThursday() != null && club.getThursday()){
            this.thursday = true;
            if(club.getThursdayStartTime() == null) this.thursdayStartTime = 00.00;
            else this.thursdayStartTime = club.getThursdayStartTime();
            if(club.getThursdayEndTime() == null) this.thursdayEndTime = 24.00;
            else this.thursdayEndTime = club.getThursdayEndTime();
        }
        if(club.getFriday() != null && club.getFriday()){
            this.friday = true;
            if(club.getFridayStartTime() == null) this.fridayStartTime = 00.00;
            else this.fridayStartTime = club.getFridayStartTime();
            if(club.getFridayEndTime() == null) this.fridayEndTime = 24.00;
            else this.fridayEndTime = club.getFridayEndTime();
        }
        if(club.getSaturday() != null && club.getSaturday()){
            this.saturday = true;
            if(club.getSaturdayStartTime() == null) this.saturdayStartTime = 00.00;
            else this.saturdayStartTime = club.getSaturdayStartTime();
            if(club.getSaturdayEndTime() == null) this.saturdayEndTime = 24.00;
            else this.saturdayEndTime = club.getSaturdayEndTime();
        }
        if(club.getSunday() != null && club.getSunday()){
            this.sunday = true;
            if(club.getSundayStartTime() == null) this.sundayStartTime = 00.00;
            else this.sundayStartTime = club.getSundayStartTime();
            if(club.getSundayEndTime() == null) this.sundayEndTime = 24.00;
            else this.saturdayEndTime = club.getSaturdayEndTime();
        }

        if (club.getTeenager()  != null && club.getTeenager()) ageTable.add("10대");
        if (club.getTwenties() != null && club.getTwenties()) ageTable.add("20대");
        if (club.getThirties() != null && club.getThirties()) ageTable.add("30대");
        if (club.getForties() != null && club.getForties()) ageTable.add("40대");
        if (club.getMoreAge() != null && club.getMoreAge()) ageTable.add("50대 이상");


    }

}
