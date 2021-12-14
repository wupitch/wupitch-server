package com.server.wupitch.post;

import com.server.wupitch.club.dto.CrewResultRes;
import com.server.wupitch.configure.response.CommonResponse;
import com.server.wupitch.configure.response.DataResponse;
import com.server.wupitch.configure.response.ResponseService;
import com.server.wupitch.configure.security.authentication.CustomUserDetails;
import com.server.wupitch.post.dto.CreatePostReq;
import com.server.wupitch.post.dto.PostRes;
import com.server.wupitch.post.dto.PostResultRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"Post API"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/app")
public class PostController {

    private final PostService postService;
    private final ResponseService responseService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "크루 게시판 조회 API", description = "크루 ID를 기준으로 게시판 조회")
    @GetMapping("/posts/crew/{crewId}")
    public DataResponse<List<PostRes>> getPostListByCrewId(@AuthenticationPrincipal CustomUserDetails customUserDetails ,@PathVariable(name = "crewId") Long crewId) {
        List<PostRes> list = postService.getPostListByCrewId(customUserDetails, crewId);
        return responseService.getDataResponse(list);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "크루 게시판 게시물 생성 API", description = "크루 ID를 기준으로 게시물 생성")
    @PostMapping("/posts/crew/{crewId}")
    public CommonResponse createPostByCrewId(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable(name = "crewId") Long crewId, @RequestBody CreatePostReq dto) {
        postService.createPostByCrewId(customUserDetails, crewId, dto);
        return responseService.getSuccessResponse();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "게시글 좋아요 토글 API", description = "게시글 ID, JWT토큰을 기준으로 게시글 좋아요 토글")
    @PatchMapping(value = "/posts/{postId}/like-toggle")
    public DataResponse<PostResultRes> postLikeToggleByAuth(@PathVariable Long postId,
                                                            @AuthenticationPrincipal CustomUserDetails customUserDetails)  {
        PostResultRes postResultRes = postService.postLikeToggleByAuth(postId, customUserDetails);
        return responseService.getDataResponse(postResultRes);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "게시글 신고 토글 API", description = "게시글 ID, JWT토큰을 기준으로 게시글 신고 토글")
    @PatchMapping(value = "/posts/{postId}/report-toggle")
    public DataResponse<PostResultRes> postReportToggleByAuth(@PathVariable Long postId,
                                                            @AuthenticationPrincipal CustomUserDetails customUserDetails)  {
        PostResultRes postResultRes = postService.postReportToggleByAuth(postId, customUserDetails);
        return responseService.getDataResponse(postResultRes);
    }

}
