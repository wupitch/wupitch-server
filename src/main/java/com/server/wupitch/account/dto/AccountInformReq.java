package com.server.wupitch.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountInformReq {

    private Long areaId;
    private List<Long> sportsList = new ArrayList<>();
    private String otherSports;
    private Integer ageNum;

    @NotBlank
    @Length(min=1, max = 20)
    @Pattern(regexp = "^[가-힣a-zA-Z0-9_-]{1,20}$")
    private String nickname;
    private String introduce;

}
