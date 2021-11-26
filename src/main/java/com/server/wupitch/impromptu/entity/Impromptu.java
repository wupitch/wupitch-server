package com.server.wupitch.impromptu.entity;

import com.server.wupitch.account.entity.Account;
import com.server.wupitch.area.Area;
import com.server.wupitch.configure.entity.BaseTimeEntity;
import com.server.wupitch.configure.entity.Status;
import com.server.wupitch.impromptu.dto.CreateImpromptuReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDate;

import static com.server.wupitch.configure.entity.Status.*;
import static javax.persistence.FetchType.LAZY;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Impromptu extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long impromptuId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "accountId")
    private Account account;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "areaId")
    private Area area;

    private String location;

    private LocalDate date;

    private Integer dayIdx;

    private Double startTime;

    private Double endTime;

    private String impromptuImage;

    private String title;

    private String introduction;

    private String materials;

    private String inquiries;

    private Integer recruitmentCount;

    private Integer nowMemberCount;

    private Integer dues;

    public void setImpromptuImage(String imageUrl) {
        this.impromptuImage = imageUrl;
    }

    public Impromptu(Account account, Area area, CreateImpromptuReq dto) {
        this.status = VALID;
        this.account = account;
        this.area = area;
        this.location = dto.getLocation();
        this.date = dto.getDate();
        this.startTime = dto.getStartTime();
        this.endTime = dto.getEndTime();
        this.title = dto.getTitle();
        this.introduction = dto.getIntroduction();
        this.materials = dto.getMaterials();
        this.inquiries = dto.getInquiries();
        this.recruitmentCount = dto.getRecruitmentCount();
        this.dues = dto.getDues();
        this.dayIdx = dto.getDate().getDayOfWeek().getValue();
        this.nowMemberCount = 1;
    }

}
