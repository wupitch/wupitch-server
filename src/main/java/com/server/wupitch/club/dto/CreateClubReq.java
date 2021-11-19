package com.server.wupitch.club.dto;

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
public class CreateClubReq {

    private Long sportsId;

    private Long areaId;

    private String title;

    private List<Integer> days = new ArrayList<>();

    private List<Integer> ageList = new ArrayList<>();

    private String introduction;

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

    private List<Long> extraInfoList = new ArrayList<>();

    private String location;

    private String inquiries;

    private Integer conference;

    private Integer guestConference;

    private Long memberCount;

}
