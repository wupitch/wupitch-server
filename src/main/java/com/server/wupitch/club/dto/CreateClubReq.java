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

    private String crewName;

    private String title;

    private List<Integer> ageList = new ArrayList<>();

    private List<Schedule> scheduleList = new ArrayList<>();

    private String introduction;

    private List<Long> extraInfoList = new ArrayList<>();

    private String location;

    private String inquiries;

    private String materials;

    private Integer conference;

    private Integer guestConference;

    private Long memberCount;

}
