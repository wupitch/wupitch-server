package com.server.wupitch.account.entity;

import com.server.wupitch.account.dto.AccountAuthDto;
import com.server.wupitch.account.entity.enumtypes.OAuthType;
import com.server.wupitch.area.Area;
import com.server.wupitch.configure.entity.BaseTimeEntity;
import com.server.wupitch.configure.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.server.wupitch.account.entity.enumtypes.RoleType;

import javax.persistence.*;

import static com.server.wupitch.account.entity.enumtypes.OAuthType.*;
import static com.server.wupitch.account.entity.enumtypes.RoleType.*;
import static com.server.wupitch.configure.entity.Status.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String nickname;

    private String email;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    private String profileImage;

    private String password;

    @Enumerated(EnumType.STRING)
    private OAuthType oAuth;

    private String oAuthId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "areaId")
    private Area area;

    private String introduction;

    public static Account createAccount(AccountAuthDto dto) {
        return Account.builder()
                .status(VALID)
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .role(ROLE_USER)
                .password(dto.getPassword())
                .oAuth(KAKAO)
                .oAuthId(dto.getOAuthId()).build();
    }

    public AccountAuthDto getAccountInfoDto() {
        return AccountAuthDto.builder()
                .accountId(this.accountId)
                .email(this.email)
                .nickname(this.nickname)
                .oAuthId(this.oAuthId)
                .build();
    }

}
