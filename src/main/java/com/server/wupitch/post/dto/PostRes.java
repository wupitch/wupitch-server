package com.server.wupitch.post.dto;

import com.server.wupitch.account.entity.Account;
import com.server.wupitch.club.Club;
import com.server.wupitch.post.entity.Post;
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

    private Long postId;

    private String nickname;

    private String contents;

    private Long likeCount;

    private LocalDate date;

    private Boolean isCreator;

    private Boolean isNotice;

    private String noticeTitle;

    private String accountProfileImage;

    private Boolean isAccountLike;

    private Boolean isAccountReport;

    private Boolean isCreatorCrewLeader;

    private Boolean isPhotoPost;

    public PostRes(Post post) {
        this.postId = post.getPostId();
        Account account = post.getAccount();
        Club club = post.getClub();

        this.nickname = account.getNickname();
        this.accountProfileImage = account.getProfileImage();
        this.contents = post.getContents();
        this.likeCount = post.getLikeCount();
        this.date = post.getUpdatedAt().toLocalDate();
        if(post.getAccount().equals(account)) this.isCreator = true;
        else this.isCreator = false;
        this.isNotice = post.getIsNotice();
        if(isNotice) this.noticeTitle = post.getNoticeTitle();
        if (club.getAccount().equals(post.getAccount())) this.isCreatorCrewLeader = true;
        this.isCreatorCrewLeader = false;
        this.isPhotoPost = post.getIsPhotoPost();


    }

}
