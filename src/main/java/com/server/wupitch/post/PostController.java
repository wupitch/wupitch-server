package com.server.wupitch.post;

import com.server.wupitch.configure.response.DataResponse;
import com.server.wupitch.configure.response.ResponseService;
import com.server.wupitch.post.dto.PostRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
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
    public DataResponse<List<PostRes>> getPostListByCrewId(@PathVariable(name = "crewId") Long crewId) {
        List<PostRes> list = postService.getPostListByCrewId(crewId);
        return responseService.getDataResponse(list);
    }

}
