package com.server.wupitch.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NicknameReq {

    @NotBlank
    @Length(min=3, max = 20)
    @Pattern(regexp = "^[가-힣a-zA-Z0-9_-]{1,20}$")
    private String nickname;

}
