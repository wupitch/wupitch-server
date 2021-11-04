package com.server.wupitch.club;

import com.server.wupitch.area.Area;
import com.server.wupitch.club.dto.ClubListRes;
import com.server.wupitch.configure.response.DataResponse;
import com.server.wupitch.configure.response.ResponseService;
import com.server.wupitch.sports.entity.Sports;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"Club API"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/app")
public class ClubController {

    private final ClubService clubService;
    private final ResponseService responseService;

    @Operation(summary = "크루 조회 API", description = "page, size, sortBy, isAsc RequestParam 설정")
    @GetMapping(value = "/clubs")
    public DataResponse<Page<ClubListRes>> getAllClubList(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "isAsc", required = false) Boolean isAsc,
            @RequestParam(name = "areaId", required = false) Long areaId,
            @RequestParam(name = "sportsId", required = false) Long sportsId,
            @RequestParam(name = "days", required = false) List<Integer> days,
            @RequestParam(name = "startTime", required = false) Integer startTime,
            @RequestParam(name = "endTime", required = false) Integer endTime,
            @RequestParam(name = "memberCountValue", required = false) Integer memberCountValue,
            @RequestParam(name = "ageList", required = false) List<Integer> ageList
    ) {
        if (page == null) page = 1;
        page = page - 1;
        if (size == null) size = 10;
        if (isAsc == null) isAsc = true;
        if (sortBy == null) sortBy = "updatedAt";
        Page<ClubListRes> result =  clubService.getAllClubList(page, size, sortBy, isAsc, areaId, sportsId, days, startTime, endTime, memberCountValue, ageList);
        return responseService.getDataResponse(result);
    }

}
