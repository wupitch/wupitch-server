package com.server.wupitch.fcm.dto;

import com.google.api.client.util.DateTime;
import com.server.wupitch.fcm.FcmNotice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FcmNoticeRes {

    private Long fcmId;

    private Long accountId;

    private String title;

    private String contents;

    private Boolean isChecked;

    private LocalDate dateTime;

    public FcmNoticeRes(FcmNotice fcmNotice) {
        this.fcmId = fcmNotice.getFcmId();
        this.title = fcmNotice.getTitle();
        this.accountId = fcmNotice.getAccount().getAccountId();
        this.contents = fcmNotice.getContents();
        this.isChecked = fcmNotice.getIsChecked();
        this.dateTime = fcmNotice.getCreatedAt().toLocalDate();
    }
}
