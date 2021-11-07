package com.server.wupitch.account.oAuth.kakao;

import com.server.wupitch.account.entity.enumtypes.GenderType;
import com.server.wupitch.configure.response.exception.CustomException;
import com.server.wupitch.configure.response.exception.CustomExceptionStatus;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoOAuth2 {

    @Value("${OAuth.CLIENT_ID}")
    private String clientId;

    @Value("${OAuth.CALLBACK_URI}")
    private String callBackUri;

    public KakaoUserInfo getUserInfo(String authorizedCode) {
        String accessToken = getAccessToken(authorizedCode);
        KakaoUserInfo userInfo = getUserInfoByToken(accessToken);
        if (userInfo.getGenderType().equals(GenderType.MALE))
            throw new CustomException(CustomExceptionStatus.INVALID_GENDER_TYPE);

        return userInfo;
    }


    private String getAccessToken(String authorizedCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", callBackUri);
        params.add("code", authorizedCode);

        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        String tokenJson = response.getBody();
        JSONObject rjson = new JSONObject(tokenJson);
        String accessToken = rjson.getString("access_token");

        return accessToken;
    }

    private KakaoUserInfo getUserInfoByToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        JSONObject body = new JSONObject(response.getBody());
        Long id = body.getLong("id");
        String email = body.getJSONObject("kakao_account").getString("email");
        String nickname = body.getJSONObject("properties").getString("nickname");
        String gender = body.getJSONObject("kakao_account").getString("gender");
        String phoneNumber = body.getJSONObject("kakao_account").getString("phone_number");
        if(id == null || email == null || nickname == null || gender == null || phoneNumber == null)
            throw new CustomException(CustomExceptionStatus.OAUTH_EMPTY_INFORM);

        GenderType genderType;
        if(gender.equals("male")) genderType = GenderType.MALE;
        else genderType = GenderType.FEMALE;

        return new KakaoUserInfo(id, email, nickname, genderType, phoneNumber);
    }
}
