package com.server.wupitch.account.kakao;

import com.server.wupitch.account.dto.SignInRes;
import com.server.wupitch.configure.response.DataResponse;
import com.server.wupitch.configure.response.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/app")
public class KakaoOAuthController {

    private final KakaoOAuthService kakaoOAuthService;
    private final ResponseService responseService;

    @GetMapping("/account/kakao/callback")
    public @ResponseBody DataResponse<SignInRes> kakaoLogin(String code) {
        SignInRes signInRes = kakaoOAuthService.kakaoLogin(code);
        return responseService.getDataResponse(signInRes);
    }

}
