package com.server.wupitch.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostReq {

    private String contents;

    private Boolean isNotice;

    private String noticeTitle;

}
