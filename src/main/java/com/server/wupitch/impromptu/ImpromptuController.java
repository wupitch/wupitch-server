package com.server.wupitch.impromptu;

import com.server.wupitch.club.dto.ClubIdRes;
import com.server.wupitch.club.dto.CreateClubReq;
import com.server.wupitch.configure.response.DataResponse;
import com.server.wupitch.configure.response.ResponseService;
import com.server.wupitch.configure.security.authentication.CustomUserDetails;
import com.server.wupitch.impromptu.dto.CreateImpromptuReq;
import com.server.wupitch.impromptu.dto.ImpromptuIdRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"Impromptu API"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/app")
public class ImpromptuController {

    private final ImpromptuService impromptuService;
    private final ResponseService responseService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "번개 생성 API", description = "생성 DTO를 기준으로 크루 번개")
    @PostMapping("/impromptus")
    public DataResponse<ImpromptuIdRes> createImpromptu(@RequestBody CreateImpromptuReq dto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long impromptuId = impromptuService.createImpromptu(dto, customUserDetails);
        return responseService.getDataResponse(new ImpromptuIdRes(impromptuId));
    }

}
