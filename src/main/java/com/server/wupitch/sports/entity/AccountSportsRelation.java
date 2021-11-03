package com.server.wupitch.sports.entity;

import com.server.wupitch.account.entity.Account;
import com.server.wupitch.configure.entity.BaseTimeEntity;
import com.server.wupitch.configure.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class AccountSportsRelation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long relationId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "accountId")
    private Account account;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "sportsId")
    private Sports sports;

}
