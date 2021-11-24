package com.server.wupitch.impromptu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateImpromptuReq {

    private Long areaId;

    private String location;

    private LocalDate date;

    private Double startTime;

    private Double endTime;

    private String title;

    private String introduction;

    private String materials;

    private String inquiries;

    private Integer recruitmentCount;

    private Integer dues;

}
