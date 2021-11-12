package com.server.wupitch.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInRes {

    private Long accountId;

    private String jwt;

    private String email;

}
