package com.server.wupitch.impromptu.dto;

import com.server.wupitch.impromptu.entity.Impromptu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImpromptuListRes {

    private Long impromptuId;

    private String impromptuImage;

    private Integer dDay;

    private String title;

    private LocalDate date;

    private String day;

    private String location;

    private Integer nowMemberCount;

    private Integer recruitmentCount;

    private Double startTime;

    private Double endTime;

    public Boolean isPinUp;

    public ImpromptuListRes(Impromptu impromptu) {
        this.impromptuId = impromptu.getImpromptuId();
        this.impromptuImage = impromptu.getImpromptuImage();
        LocalDate validDate = impromptu.getDate();
        LocalDate now = LocalDate.now();
        Period between = Period.between(now, validDate);
        this.dDay = between.getDays();

        this.title = impromptu.getTitle();
        this.date = impromptu.getDate();
        this.location = impromptu.getLocation();
        this.nowMemberCount =  impromptu.getNowMemberCount();
        this.recruitmentCount = impromptu.getRecruitmentCount();
        this.startTime = impromptu.getStartTime();
        this.endTime = impromptu.getEndTime();
        if(impromptu.getDayIdx() == 1) day = "월요일";
        else if(impromptu.getDayIdx() == 2) day = "화요일";
        else if(impromptu.getDayIdx() == 3) day = "수요일";
        else if(impromptu.getDayIdx() == 4) day = "목요일";
        else if(impromptu.getDayIdx() == 5) day = "금요일";
        else if(impromptu.getDayIdx() == 6) day = "토요일";
        else  day = "일요일";


    }

}
