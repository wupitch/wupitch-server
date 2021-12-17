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
public class ClubDetailRes {

    private Long clubId;

    private Long sportsId;

    private String sportsName;

    private String areaName;

    private String clubTitle;

    private String crewName;

    private Integer dues;

    private Integer guestDues;

    private Long memberCount;

    private List<String> extraList;

    private List<String> ageTable = new ArrayList<>();

    private String introduction;

    private List<Schedule> schedules = new ArrayList<>();

    private String crewImage;

    private String inquiries;

    private String materials;

    private Boolean isPinUp;

    private Boolean isSelect;

    private Long creatorAccountId;

    private String creatorAccountNickname;

    public ClubDetailRes(Club club) {
        this.clubId = club.getClubId();

        Sports sports = club.getSports();

        this.sportsId = sports.getSportsId();

        this.sportsName = sports.getName();

        this.areaName = club.getLocation();

        this.clubTitle = club.getTitle();

        this.introduction = club.getIntroduction();

        this.crewImage = club.getCrewImage();

        this.crewName = club.getCrewName();

        this.dues = club.getConference();

        this.guestDues = club.getGuestConference();

        this.memberCount = club.getMemberCount();

        this.inquiries = club.getInquiries();

        this.materials = club.getMaterials();

        this.creatorAccountId = club.getAccount().getAccountId();

        this.creatorAccountNickname = club.getAccount().getNickname();


        if (club.getMonday() != null && club.getMonday()) {
            Schedule schedule = new Schedule();
            schedule.dayIdx = 1;
            schedule.day = "월요일";
            if (club.getMondayStartTime() == null) schedule.startTime = 00.00;
            else schedule.startTime = club.getMondayStartTime();
            if (club.getMondayEndTime() == null) schedule.endTime = 24.00;
            else schedule.endTime = club.getMondayEndTime();
            this.schedules.add(schedule);
        }
        if (club.getTuesday() != null && club.getTuesday()) {
            Schedule schedule = new Schedule();
            schedule.dayIdx = 2;
            schedule.day = "화요일";
            if (club.getTuesdayStartTime() == null) schedule.startTime = 00.00;
            else schedule.startTime = club.getTuesdayStartTime();
            if (club.getTuesdayEndTime() == null) schedule.endTime = 24.00;
            else schedule.endTime = club.getTuesdayEndTime();
            this.schedules.add(schedule);
        }
        if (club.getWednesday() != null && club.getWednesday()) {
            Schedule schedule = new Schedule();
            schedule.dayIdx = 3;
            schedule.day = "수요일";
            if (club.getWednesdayStartTime() == null) schedule.startTime = 00.00;
            else schedule.startTime = club.getWednesdayStartTime();
            if (club.getWednesdayEndTime() == null) schedule.endTime = 24.00;
            else schedule.endTime = club.getWednesdayEndTime();
            this.schedules.add(schedule);
        }
        if (club.getThursday() != null && club.getThursday()) {
            Schedule schedule = new Schedule();
            schedule.dayIdx = 4;
            schedule.day = "목요일";
            if (club.getThursdayStartTime() == null) schedule.startTime = 00.00;
            else schedule.startTime = club.getThursdayStartTime();
            if (club.getThursdayEndTime() == null) schedule.endTime = 24.00;
            else schedule.endTime = club.getThursdayEndTime();
            this.schedules.add(schedule);
        }
        if (club.getFriday() != null && club.getFriday()) {
            Schedule schedule = new Schedule();
            schedule.dayIdx = 5;
            schedule.day = "금요일";
            if (club.getFridayStartTime() == null) schedule.startTime = 00.00;
            else schedule.startTime = club.getFridayStartTime();
            if (club.getFridayEndTime() == null) schedule.endTime = 24.00;
            else schedule.endTime = club.getFridayEndTime();
            this.schedules.add(schedule);
        }
        if (club.getSaturday() != null && club.getSaturday()) {
            Schedule schedule = new Schedule();
            schedule.dayIdx = 6;
            schedule.day = "토요일";
            if (club.getSaturdayStartTime() == null) schedule.startTime = 00.00;
            else schedule.startTime = club.getSaturdayStartTime();
            if (club.getSaturdayEndTime() == null) schedule.endTime = 24.00;
            else schedule.endTime = club.getSaturdayEndTime();
            this.schedules.add(schedule);
        }
        if (club.getSunday() != null && club.getSunday()) {
            Schedule schedule = new Schedule();
            schedule.dayIdx = 7;
            schedule.day = "일요일";
            if (club.getSundayStartTime() == null) schedule.startTime = 00.00;
            else schedule.startTime = club.getSundayStartTime();
            if (club.getSundayEndTime() == null) schedule.endTime = 24.00;
            else schedule.endTime = club.getSundayEndTime();
            this.schedules.add(schedule);
        }

        if (club.getTeenager() != null && club.getTeenager()) ageTable.add("10대");
        if (club.getTwenties() != null && club.getTwenties()) ageTable.add("20대");
        if (club.getThirties() != null && club.getThirties()) ageTable.add("30대");
        if (club.getForties() != null && club.getForties()) ageTable.add("40대");
        if (club.getMoreAge() != null && club.getMoreAge()) ageTable.add("50대 이상");


    }

}
