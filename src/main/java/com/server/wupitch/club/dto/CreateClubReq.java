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

    private Double startTime;

    private Double endTime;

    private List<Long> extraInfoList = new ArrayList<>();

    private String location;

    private String inquiries;

    private Integer conference;

    private Integer guestConference;

}
