package com.server.wupitch.sports;

import com.server.wupitch.area.dto.AreaRes;
import com.server.wupitch.configure.response.DataResponse;
import com.server.wupitch.configure.response.ResponseService;
import com.server.wupitch.sports.dto.SportsRes;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"Sports API"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/app")
public class SportsController {

    private final SportsService sportsService;
    private final ResponseService responseService;

    @Operation(summary = "스포츠 조회 API", description = "전체 스포츠 조회 API")
    @GetMapping(value = "/sports")
    public DataResponse<List<SportsRes>> getAllSports() {
        List<SportsRes> list = sportsService.getAllSports();
        return responseService.getDataResponse(list);
    }

}
