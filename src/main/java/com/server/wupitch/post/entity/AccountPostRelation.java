package com.server.wupitch.post.entity;

import com.server.wupitch.account.entity.Account;
import com.server.wupitch.configure.entity.BaseTimeEntity;
import com.server.wupitch.configure.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AccountPostRelation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long relationId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "accountId")
    private Account account;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "postId")
    private Post post;

    private Boolean isLike = false;

    private Boolean isReport = false;

    public void toggleLike(){
        if(this.isLike == null) this.isLike = true;
        else this.isLike = !this.isLike;
    }

    public void toggleReport(){
        if(this.isReport == null) this.isReport = true;
        else this.isReport = !this.isReport;
    }

}
