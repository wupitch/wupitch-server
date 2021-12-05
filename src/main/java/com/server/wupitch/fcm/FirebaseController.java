package com.server.wupitch.fcm;

import com.server.wupitch.account.dto.SignInReq;
import com.server.wupitch.account.dto.SignInRes;
import com.server.wupitch.configure.response.CommonResponse;
import com.server.wupitch.configure.response.DataResponse;
import com.server.wupitch.configure.response.ResponseService;
import com.server.wupitch.configure.security.authentication.CustomUserDetails;
import com.server.wupitch.fcm.dto.FcmNoticeRes;
import com.server.wupitch.fcm.dto.FcmSendReq;
import com.server.wupitch.util.ValidationExceptionProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Api(tags = {"FCM API"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/app")
public class FirebaseController {

    private final ResponseService responseService;
    private final FirebaseCloudMessageService firebaseCloudMessageService;
    private final FirebaseService firebaseService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "fcm test API", description = "호출 시 'test'라는 메세지 전송")
    @PostMapping("/fcm/test")
    public CommonResponse fcmTest(@RequestBody FcmSendReq dto, @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        firebaseCloudMessageService.sendMessageTo(customUserDetails.getAccount() ,dto.getTargetToken(), dto.getTitle(), dto.getContents());
        return responseService.getSuccessResponse();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "현재 인증된 회원의 알림 조회", description = "JWT 토큰을 기준으로 인증된 회원 알림 조회")
    @GetMapping("/fcms/accounts/auth")
    public DataResponse<List<FcmNoticeRes>> getFcmListByAuth(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<FcmNoticeRes> list = firebaseService.getFcmListByAuth(customUserDetails);
        return responseService.getDataResponse(list);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "FCM 조회 API", description = "FCM의 번호를 기준으로 조회 상태로 정보 수정")
    @PatchMapping("/fcms/{fcmId}/view")
    public CommonResponse getReviewByFcmId(@PathVariable Long fcmId) {
        firebaseService.getReviewByFcmId(fcmId);
        return responseService.getSuccessResponse();
    }

}
