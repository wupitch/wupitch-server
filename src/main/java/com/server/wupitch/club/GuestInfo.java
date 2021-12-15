package com.server.wupitch.club;


import com.server.wupitch.account.entity.Account;
import com.server.wupitch.configure.entity.BaseTimeEntity;
import com.server.wupitch.configure.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDate;

import static com.server.wupitch.configure.entity.Status.*;
import static javax.persistence.FetchType.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class GuestInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guestInfoId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "accountId")
    private Account account;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "clubId")
    private Club club;

    private LocalDate selectedDate;

    private Boolean isValid;

    public GuestInfo(Account account, Club club, LocalDate selectedDate) {
        this.status = VALID;
        this.account = account;
        this.club = club;
        this.selectedDate = selectedDate;
        this.isValid = false;
    }

}
