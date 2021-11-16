package com.server.wupitch.extra;

import com.server.wupitch.area.dto.AreaRes;
import com.server.wupitch.configure.response.DataResponse;
import com.server.wupitch.configure.response.ResponseService;
import com.server.wupitch.extra.dto.ExtraRes;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"Extra API"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/app")
public class ExtraController {

    private final ExtraService extraService;
    private final ResponseService responseService;

    @Operation(summary = "추가정보 조회 API", description = "전체 추가정보 조회 API")
    @GetMapping(value = "/extras")
    public DataResponse<List<ExtraRes>> getAllExtras() {
        List<ExtraRes> list = extraService.getAllExtras();
        return responseService.getDataResponse(list);
    }

}
