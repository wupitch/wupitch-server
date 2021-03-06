package com.server.wupitch.account.oAuth.kakao;

import com.server.wupitch.account.entity.enumtypes.GenderType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class KakaoUserInfo {

    Long id;
    String email;
    String nickname;
    GenderType genderType;
    String phoneNumber;

}
