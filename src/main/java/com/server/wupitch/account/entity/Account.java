package com.server.wupitch.account.entity;

import com.server.wupitch.account.entity.enumtypes.OAuthType;
import com.server.wupitch.configure.entity.BaseTimeEntity;
import com.server.wupitch.configure.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.server.wupitch.account.entity.enumtypes.RoleType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account extends BaseTimeEntity {

    @Id
    private Long accountId;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String nickname;

    private String email;

    private RoleType role;

    private String profileImage;

    private String password;

    @Enumerated(EnumType.STRING)
    private OAuthType oAuth;

    private String oAuthId;

}
