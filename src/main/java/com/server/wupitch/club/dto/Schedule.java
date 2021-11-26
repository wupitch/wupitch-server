package com.server.wupitch.club.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {

    Integer dayIdx;

    String day;

    Double startTime;

    Double endTime;

}
