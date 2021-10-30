package com.server.wupitch.account.oAuth;

import com.server.wupitch.account.dto.SignInRes;
import com.server.wupitch.account.oAuth.kakao.KakaoOAuthService;
import com.server.wupitch.account.oAuth.kakao.KakaoUserInfo;
import com.server.wupitch.configure.response.DataResponse;
import com.server.wupitch.configure.response.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"OAuth API"})
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/app")
public class OAuthController {

    private final KakaoOAuthService kakaoOAuthService;
    private final ResponseService responseService;


    @Operation(summary = "카카오 OAuth 인증 요청 API", description = "형식에 맞는 DTO로 리퀘스트 -> JWT 토큰을 포함한 회원 정보 리턴")
    @PostMapping("/account/kakao")
    public @ResponseBody DataResponse<SignInRes> kakaoLoginAccountInfoDto(@RequestBody KakaoUserInfo kakaoUserInfo) {
        SignInRes signInRes = kakaoOAuthService.kakaoLogin(kakaoUserInfo);
        return responseService.getDataResponse(signInRes);
    }



}
