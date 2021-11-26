package com.server.wupitch.fcm;

import com.server.wupitch.account.dto.SignInReq;
import com.server.wupitch.account.dto.SignInRes;
import com.server.wupitch.configure.response.CommonResponse;
import com.server.wupitch.configure.response.DataResponse;
import com.server.wupitch.configure.response.ResponseService;
import com.server.wupitch.fcm.dto.FcmSendReq;
import com.server.wupitch.util.ValidationExceptionProvider;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@Api(tags = {"FCM API"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/app")
public class FirebaseController {

    private final ResponseService responseService;
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @Operation(summary = "fcm test API", description = "호출 시 'test'라는 메세지 전송")
    @PostMapping("/app/fcm/test")
    public CommonResponse fcmTest(FcmSendReq dto) throws IOException {
        firebaseCloudMessageService.sendMessageTo(dto.getTargetToken(), dto.getTitle(), dto.getContents());
        return responseService.getSuccessResponse();
    }

}
