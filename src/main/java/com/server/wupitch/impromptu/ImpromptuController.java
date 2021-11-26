package com.server.wupitch.impromptu;

import com.server.wupitch.club.dto.ClubListRes;
import com.server.wupitch.configure.response.CommonResponse;
import com.server.wupitch.configure.response.DataResponse;
import com.server.wupitch.configure.response.ResponseService;
import com.server.wupitch.configure.security.authentication.CustomUserDetails;
import com.server.wupitch.impromptu.dto.CreateImpromptuReq;
import com.server.wupitch.impromptu.dto.ImpromptuDetailRes;
import com.server.wupitch.impromptu.dto.ImpromptuIdRes;
import com.server.wupitch.impromptu.dto.ImpromptuListRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "번개 조회 API", description = "page, size, sortBy, isAsc, RequestParam 설정")
    @GetMapping(value = "/impromptus")
    public DataResponse<Page<ImpromptuListRes>> getAllImpromptuList(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "isAsc", required = false) Boolean isAsc,
            @RequestParam(name = "areaId", required = false) Long areaId,
            @RequestParam(name = "scheduleIndex", required = false) Integer scheduleIndex,
            @RequestParam(name = "days", required = false) List<Integer> days,
            @RequestParam(name = "memberCountIndex", required = false) Integer memberCountIndex,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        if (page == null) page = 1;
        page = page - 1;
        if (size == null) size = 10;
        if (isAsc == null) isAsc = true;
        if (sortBy == null) sortBy = "updatedAt";
        Page<ImpromptuListRes> result = impromptuService.getAllImpromptuList(page, size, sortBy, isAsc, areaId, scheduleIndex, days, memberCountIndex, customUserDetails);
        return responseService.getDataResponse(result);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "번개 이미지 추가 API", description = "번개 ID를 기준으로 번개 이미지 추가")
    @PatchMapping(value = "/impromptu/image")
    public CommonResponse uploadImpromptusImage(@RequestParam("images") MultipartFile multipartFile,
                                          @RequestParam("impromptuId") Long impromptuId) throws IOException {
        impromptuService.uploadImpromptusImage(multipartFile, impromptuId);
        return responseService.getSuccessResponse();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "번개 세부 조회 API", description = "번개 ID를 기준으로 번개 세부 정보 조회")
    @GetMapping(value = "/impromptus/{impromptuId}")
    public DataResponse<ImpromptuDetailRes> getDetailImpromptusById(@PathVariable Long impromptuId) {
        ImpromptuDetailRes impromptuDetailRes = impromptuService.getDetailImpromptusById(impromptuId);
        return responseService.getDataResponse(impromptuDetailRes);
    }

}
