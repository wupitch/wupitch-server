package com.server.wupitch.impromptu.dto;

import com.server.wupitch.account.entity.Account;
import com.server.wupitch.impromptu.accountImpromptuRelation.AccountImpromptuRelation;
import com.server.wupitch.impromptu.entity.Impromptu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImpromptuMemberListRes implements Comparable<ImpromptuMemberListRes>{


    private Long accountId;
    private String accountNickname;
    private Boolean isLeader;
    private Boolean isValid;
    private LocalDateTime addedAt;

    @Override
    public int compareTo(@NotNull ImpromptuMemberListRes other) {
        if (!this.isLeader && !other.isLeader) {
            if (this.addedAt.isBefore(other.addedAt)) return -1;
            else return 1;
        }
        else if(this.isLeader) return -1;
        else return 1;
    }

    public ImpromptuMemberListRes(AccountImpromptuRelation accountImpromptuRelation) {
        Account account = accountImpromptuRelation.getAccount();
        Impromptu impromptu = accountImpromptuRelation.getImpromptu();
        this.accountId = account.getAccountId();
        this.accountNickname = account.getNickname();
        if (impromptu.getAccount().equals(account)) this.isLeader = true;
        else this.isLeader = false;
        this.isValid = accountImpromptuRelation.getIsValid();
        this.addedAt = accountImpromptuRelation.getUpdatedAt();
    }
}