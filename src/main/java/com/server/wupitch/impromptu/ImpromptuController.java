package com.server.wupitch.impromptu;

import com.server.wupitch.club.dto.ClubListRes;
import com.server.wupitch.club.dto.CrewFilterRes;
import com.server.wupitch.configure.response.CommonResponse;
import com.server.wupitch.configure.response.DataResponse;
import com.server.wupitch.configure.response.ResponseService;
import com.server.wupitch.configure.security.authentication.CustomUserDetails;
import com.server.wupitch.impromptu.dto.*;
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
    public DataResponse<ImpromptuIdRes> createImpromptu(@RequestBody CreateImpromptuReq dto, @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
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
    @Operation(summary = "번개 타이틀로 조회 API", description = "문자열을 기준으로 번개 조회")
    @GetMapping(value = "/impromptus/title")
    public DataResponse<Page<ImpromptuListRes>> getAllImpromptuListByTitle(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "isAsc", required = false) Boolean isAsc,
            @RequestParam(name = "areaId", required = false) Long areaId,
            @RequestParam(name = "title", required = false) String title,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        if (page == null) page = 1;
        page = page - 1;
        if (size == null) size = 10;
        if (isAsc == null) isAsc = true;
        if (sortBy == null) sortBy = "updatedAt";
        Page<ImpromptuListRes> result = impromptuService.getAllImpromptuListByTitle(page, size, sortBy, isAsc, areaId, title, customUserDetails);
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
    public DataResponse<ImpromptuDetailRes> getDetailImpromptusById(@PathVariable Long impromptuId,
                                                                    @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ImpromptuDetailRes impromptuDetailRes = impromptuService.getDetailImpromptusById(impromptuId, customUserDetails);
        return responseService.getDataResponse(impromptuDetailRes);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "번개 핀업 토글 API", description = "번개 ID, JWT토큰을 기준으로 크루 핀업 토글")
    @PatchMapping(value = "/impromptus/{impromptuId}/pinUp-toggle")
    public DataResponse<ImpromptuResultRes> clubPinUpToggleByAuth(@PathVariable Long impromptuId,
                                                @AuthenticationPrincipal CustomUserDetails customUserDetails)  {
        ImpromptuResultRes impromptuResultRes = impromptuService.impromptuPinUpToggleByAuth(impromptuId, customUserDetails);
        return responseService.getDataResponse(impromptuResultRes);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "번개 참여 토글 API", description = "번개 ID, JWT토큰을 기준으로 번개 참여 토글")
    @PostMapping(value = "/impromptus/{impromptuId}/participation-toggle")
    public DataResponse<ImpromptuResultRes> impromptuParticipationToggleByAuth(@PathVariable Long impromptuId,
                                                                               @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        ImpromptuResultRes impromptuResultRes = impromptuService.impromptuParticipationToggleByAuth(impromptuId, customUserDetails);
        return responseService.getDataResponse(impromptuResultRes);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "번개 필터 조회 API", description = "JWT를 기준으로 선택했던 번개 필터 조회")
    @GetMapping("/accounts/auth/impromptu-filter")
    public DataResponse<ImpromptuFilterRes> getImpromptuFilterRes(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ImpromptuFilterRes dto = impromptuService.getImpromptuFilterRes(customUserDetails);
        return responseService.getDataResponse(dto);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "참여한 번개 조회 API", description = "JWT를 기준으로 참여한 번개 조회")
    @GetMapping(value = "/impromptus/accounts/auth")
    public DataResponse<List<ProfileImpromptuListRes>> getImpromptuListByAuth(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        List<ProfileImpromptuListRes> list = impromptuService.getImpromptuListByAuth(customUserDetails);
        return responseService.getDataResponse(list);
    }

}
