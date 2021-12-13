package com.server.wupitch.post.dto;

import com.server.wupitch.account.entity.Account;
import com.server.wupitch.club.Club;
import com.server.wupitch.post.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRes {

    private String nickname;

    private String contents;

    private Long likeCount;

    private LocalDate date;

    private Boolean isCreator;

    private Boolean isNotice;

    private String noticeTitle;

    public PostRes(Post post) {
        Account account = post.getAccount();
        Club club = post.getClub();

        this.nickname = account.getNickname();
        this.contents = post.getContents();
        this.likeCount = post.getLikeCount();
        this.date = post.getUpdatedAt().toLocalDate();
        if(club.getAccount().equals(account)) this.isCreator = true;
        else this.isCreator = false;
        this.isNotice = post.getIsNotice();
        if(isNotice) this.noticeTitle = post.getNoticeTitle();
    }

}
