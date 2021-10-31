package com.server.wupitch.account.oAuth;

import com.server.wupitch.account.dto.SignInRes;
import com.server.wupitch.account.oAuth.apple.AppleOAuthService;
import com.server.wupitch.account.oAuth.apple.AppleUserInfo;
import com.server.wupitch.account.oAuth.kakao.KakaoOAuthService;
import com.server.wupitch.account.oAuth.kakao.KakaoUserInfo;
import com.server.wupitch.configure.response.DataResponse;
import com.server.wupitch.configure.response.ResponseService;
import com.server.wupitch.configure.response.exception.CustomException;
import com.server.wupitch.configure.response.exception.CustomExceptionStatus;
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
    private final AppleOAuthService appleOAuthService;


    @Operation(summary = "카카오 OAuth 인증 요청 API", description = "형식에 맞는 DTO로 리퀘스트 -> JWT 토큰을 포함한 회원 정보 리턴")
    @PostMapping("/account/kakao")
    public @ResponseBody DataResponse<SignInRes> kakaoLoginAccountInfoDto(@RequestBody KakaoUserInfo kakaoUserInfo) {
        SignInRes signInRes = null;
        try {
            signInRes = kakaoOAuthService.kakaoLogin(kakaoUserInfo);
        } catch (Exception e) {
            throw new CustomException(CustomExceptionStatus.INVALID_OAUTH_ERROR);
        }
        return responseService.getDataResponse(signInRes);
    }

    @Operation(summary = "애플 OAuth 인증 요청 API", description = "형식에 맞는 DTO로 리퀘스트 -> JWT 토큰을 포함한 회원 정보 리턴")
    @PostMapping("/account/apple")
    public @ResponseBody DataResponse<SignInRes> appleLoginAccountInfoDto(@RequestBody AppleUserInfo appleUserInfo) {
        SignInRes signInRes = null;
        try {
            signInRes = appleOAuthService.appleLogin(appleUserInfo);
        } catch (Exception e) {
            throw new CustomException(CustomExceptionStatus.INVALID_OAUTH_ERROR);
        }
        return responseService.getDataResponse(signInRes);
    }

}
