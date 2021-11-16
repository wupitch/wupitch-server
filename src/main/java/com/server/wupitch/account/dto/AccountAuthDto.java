package com.server.wupitch.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountAuthDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long accountId;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Length(min=6, max= 50)
    private String password;

    @Length(min=3, max = 20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]{3,20}$")
    private String nickname;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String jwt;

    private String introduce;

    private Boolean isPushAgree;

}
