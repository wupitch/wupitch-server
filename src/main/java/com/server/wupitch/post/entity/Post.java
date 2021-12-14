package com.server.wupitch.post.entity;

import com.server.wupitch.account.entity.Account;
import com.server.wupitch.club.Club;
import com.server.wupitch.configure.entity.BaseTimeEntity;
import com.server.wupitch.configure.entity.Status;
import com.server.wupitch.post.dto.CreatePostReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.server.wupitch.configure.entity.Status.VALID;
import static javax.persistence.FetchType.LAZY;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "accountId")
    private Account account;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "clubId")
    private Club club;

    private String contents;

    private Long likeCount;

    private Boolean isNotice;

    private String noticeTitle;

    private Boolean isPhotoPost;

    public Post(Account account, Club club, CreatePostReq dto, Boolean isPhotoPost) {
        this.status = VALID;
        this.account = account;
        this.club = club;
        this.contents = dto.getContents();
        this.likeCount = 0L;
        this.isNotice = dto.getIsNotice();
        if(isNotice) this.noticeTitle = dto.getNoticeTitle();
        this.isPhotoPost = isPhotoPost;
    }

}
