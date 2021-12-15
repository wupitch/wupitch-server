package com.server.wupitch.impromptu.accountImpromptuRelation;

import com.server.wupitch.account.entity.Account;
import com.server.wupitch.configure.entity.BaseTimeEntity;
import com.server.wupitch.configure.entity.Status;
import com.server.wupitch.impromptu.entity.Impromptu;
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
public class AccountImpromptuRelation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long relationId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "accountId")
    private Account account;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "impromptuId")
    private Impromptu impromptu;

    private Boolean isPinUp;

    private Boolean isSelect;

    private Boolean isValid = false;

    public void togglePinUp() {
        if (this.isPinUp == null) this.isPinUp = true;
        else this.isPinUp = !this.isPinUp;
    }

    public void toggleSelect() {
        if(this.isSelect == null) this.isSelect = true;
        else this.isSelect = !this.isSelect;
    }

    public void enroll() {
        this.isValid = true;
    }
}
