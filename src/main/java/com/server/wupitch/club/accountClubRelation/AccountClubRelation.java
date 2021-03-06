package com.server.wupitch.club.accountClubRelation;

import com.server.wupitch.account.entity.Account;
import com.server.wupitch.club.Club;
import com.server.wupitch.configure.entity.BaseTimeEntity;
import com.server.wupitch.configure.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AccountClubRelation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long relationId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "accountId")
    private Account account;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "clubId")
    private Club club;

    private Boolean isPinUp;

    private Boolean isSelect;

    private Boolean isGuest;

    public void togglePinUp() {
        if (this.isPinUp == null) this.isPinUp = true;
        else this.isPinUp = !this.isPinUp;
    }

    public void toggleSelect() {
        if(this.isSelect == null) this.isSelect = true;
        else this.isSelect = !this.isSelect;
    }

    public void toggleGuest() {
        if(this.isGuest == null) this.isGuest = true;
        else this.isGuest = !this.isGuest;
    }

}
