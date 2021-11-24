package com.server.wupitch.impromptu.dto;

import com.server.wupitch.impromptu.entity.Impromptu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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

    private Integer dues;

    private Integer nowMemberCount;

    private Integer recruitmentCount;

    private String introduction;

    private String materials;

    private String inquiries;

    public ImpromptuListRes(Impromptu impromptu) {
        this.impromptuId = impromptu.getImpromptuId();
        this.impromptuImage = impromptu.getImpromptuImage();
        LocalDate validDateTime = impromptu.getDate();
        LocalDate now = LocalDate.now();
        long diffTime = validDateTime.until(now, ChronoUnit.SECONDS);
        diffTime = diffTime / 24;
        this.dDay = (int)diffTime;

        this.title = impromptu.getTitle();
        this.date = impromptu.getDate();
        this.location = impromptu.getLocation();
        this.dues = impromptu.getDues();
        this.nowMemberCount =  impromptu.getNowMemberCount();
        this.recruitmentCount = impromptu.getRecruitmentCount();
        this.introduction = impromptu.getIntroduction();
        this.materials = impromptu.getMaterials();
        this.inquiries = impromptu.getInquiries();


    }

}
