package com.server.wupitch.configure.entity;

import com.server.wupitch.account.entity.Account;
import com.server.wupitch.account.entity.enumtypes.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AccountLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountLogId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "accountId")
    private Account account;

    private LocalDateTime dateTime;

    private String userName;

    @Enumerated(EnumType.STRING)
    private RoleType userRole;

    private String methodName;

    public AccountLogEntity(Account account, LocalDateTime localDateTime, String methodName) {
        if (account != null) {
            this.account = account;
            this.userName = account.getNickname();
            this.userRole = account.getRole();
        }
        this.dateTime = localDateTime;
        this.methodName = methodName;
    }

}
