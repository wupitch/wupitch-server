package com.server.wupitch.club;

import com.server.wupitch.club.dto.*;
import com.server.wupitch.configure.response.CommonResponse;
import com.server.wupitch.configure.response.DataResponse;
import com.server.wupitch.configure.response.ResponseService;
import com.server.wupitch.configure.security.authentication.CustomUserDetails;
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

@Api(tags = {"Club API"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/app")
public class ClubController {

    private final ClubService clubService;
    private final ResponseService responseService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "크루 조회 API", description = "page, size, sortBy, isAsc RequestParam 설정")
    @GetMapping(value = "/clubs")
    public DataResponse<Page<ClubListRes>> getAllClubList(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "isAsc", required = false) Boolean isAsc,
            @RequestParam(name = "areaId", required = false) Long areaId,
            @RequestParam(name = "sportsList", required = false) List<Long> sportsList,
            @RequestParam(name = "days", required = false) List<Integer> days,
            @RequestParam(name = "memberCountValue", required = false) Integer memberCountValue,
            @RequestParam(name = "ageList", required = false) List<Integer> ageList,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {

        if (page == null) page = 1;
        page = page - 1;
        if (size == null) size = 10;
        if (isAsc == null) isAsc = true;
        if (sortBy == null) sortBy = "updatedAt";
        Page<ClubListRes> result = clubService.getAllClubList(page, size, sortBy, isAsc, areaId, sportsList, days, memberCountValue, ageList, customUserDetails);
        return responseService.getDataResponse(result);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "크루 타이틀로 조회 API", description = "문자열을 기준으로 크루 조회")
    @GetMapping(value = "/clubs/title")
    public DataResponse<Page<ClubListRes>> getAllClubListByClubTitle(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "isAsc", required = false) Boolean isAsc,
            @RequestParam(name = "areaId", required = false) Long areaId,
            @RequestParam(name = "crewTitle", required = false) String crewTitle,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {

        if (page == null) page = 1;
        page = page - 1;
        if (size == null) size = 10;
        if (isAsc == null) isAsc = true;
        if (sortBy == null) sortBy = "updatedAt";
        Page<ClubListRes> result = clubService.getAllClubListByClubTitle(page, size, sortBy, isAsc, areaId, crewTitle, customUserDetails);
        return responseService.getDataResponse(result);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "크루 생성 API", description = "생성 DTO를 기준으로 크루 생성")
    @PostMapping("/clubs")
    public DataResponse<ClubIdRes> createClub(@RequestBody CreateClubReq dto, @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        Long clubId = clubService.createClub(dto, customUserDetails);
        return responseService.getDataResponse(new ClubIdRes(clubId));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "크루 이미지 추가 API", description = "크루 ID를 기준으로 크루 이미지 추가")
    @PatchMapping(value = "/clubs/image")
    public CommonResponse uploadCrewImage(@RequestParam("images") MultipartFile multipartFile,
                                          @RequestParam("crewId") Long crewId) throws IOException {
        clubService.uploadCrewImage(multipartFile, crewId);
        return responseService.getSuccessResponse();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "크루 세부 조회 API", description = "크루 ID를 기준으로 크루 세부 조회")
    @GetMapping(value = "/clubs/{clubId}")
    public DataResponse<ClubDetailRes> getDetailClubById(@PathVariable Long clubId,
                                                         @AuthenticationPrincipal CustomUserDetails customUserDetails)  {
        ClubDetailRes clubDetailRes = clubService.getDetailClubById(clubId, customUserDetails);
        return responseService.getDataResponse(clubDetailRes);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "크루 핀업 토글 API", description = "크루 ID, JWT토큰을 기준으로 크루 핀업 토글")
    @PatchMapping(value = "/clubs/{clubId}/pinUp-toggle")
    public DataResponse<CrewResultRes> clubPinUpToggleByAuth(@PathVariable Long clubId,
                                                @AuthenticationPrincipal CustomUserDetails customUserDetails)  {
        CrewResultRes crewResultRes = clubService.clubPinUpToggleByAuth(clubId, customUserDetails);
        return responseService.getDataResponse(crewResultRes);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "크루 참여 토글 API", description = "크루 ID, JWT토큰을 기준으로 크루 참여 토글")
    @PostMapping(value = "/clubs/{clubId}/participation-toggle")
    public DataResponse<CrewResultRes> clubParticipationToggleByAuth(@PathVariable Long clubId,
                                                @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        CrewResultRes crewResultRes = clubService.clubParticipationToggleByAuth(clubId, customUserDetails);
        return responseService.getDataResponse(crewResultRes);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "크루 필터 조회 API", description = "JWT를 기준으로 선택했던 크루 필터 조회")
    @GetMapping("/accounts/auth/crew-filter")
    public DataResponse<CrewFilterRes> getCrewFilterByAuth(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        CrewFilterRes dto = clubService.getCrewFilterByAuth(customUserDetails);
        return responseService.getDataResponse(dto);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "크루 게스트로 참여 토글 API", description = "크루 ID, JWT토큰을 기준으로 크루 게스트로 참여 토글")
    @PostMapping(value = "/clubs/{clubId}/guest-toggle")
    public DataResponse<CrewResultRes> clubGuestToggleByAuth(@PathVariable Long clubId,
                                                                     @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        CrewResultRes crewResultRes = clubService.clubGuestToggleByAuth(clubId, customUserDetails);
        return responseService.getDataResponse(crewResultRes);
    }


}
