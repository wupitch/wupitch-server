package com.server.wupitch.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChkReq {

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Length(min=6, max= 50)
    private String password;

}
