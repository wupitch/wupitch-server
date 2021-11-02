package com.server.wupitch.area;

import com.server.wupitch.area.dto.AreaRes;
import com.server.wupitch.configure.response.DataResponse;
import com.server.wupitch.configure.response.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"Area API"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/app")
public class AreaController {

    private final AreaService areaService;
    private final ResponseService responseService;

    @Operation(summary = "지역 조회 API", description = "전체 지역 조회 API")
    @GetMapping(value = "/areas")
    public DataResponse<List<AreaRes>> getAllAreas() {
        List<AreaRes> list = areaService.getAllAreas();
        return responseService.getDataResponse(list);
    }

}
