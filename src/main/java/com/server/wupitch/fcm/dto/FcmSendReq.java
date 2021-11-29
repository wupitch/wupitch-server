package com.server.wupitch.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FcmSendReq {

    private String targetToken;
    private String title;
    private String contents;

}
