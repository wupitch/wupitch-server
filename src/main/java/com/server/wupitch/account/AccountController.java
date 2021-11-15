package com.server.wupitch.account;

import com.server.wupitch.account.dto.*;
import com.server.wupitch.configure.response.CommonResponse;
import com.server.wupitch.configure.response.DataResponse;
import com.server.wupitch.configure.response.ResponseService;
import com.server.wupitch.configure.security.authentication.CustomUserDetails;
import com.server.wupitch.util.ValidationExceptionProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = {"Account API"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/app")
public class AccountController {

    private final AccountService accountService;
    private final ResponseService responseService;

    @Operation(summary = "로그인 인증 API", description = "형식에 맞는 DTO로 리퀘스트 -> JWT 토큰을 포함한 회원 정보 리턴")
    @PostMapping(value = "/sign-in")
    public DataResponse<SignInRes> signIn(@RequestBody @Valid SignInReq req, Errors errors) {
        if (errors.hasErrors()) ValidationExceptionProvider.throwValidError(errors);
        return responseService.getDataResponse(accountService.signIn(req));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "현재 인증된 회원 정보 요청 API", description = "JWT 토큰을 기준으로 인증된 회원 정보 리턴")
    @GetMapping(value = "/accounts/auth")
    public DataResponse<AccountAuthDto> getAuthAccount(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return responseService.getDataResponse(accountService.getAuthAccount(customUserDetails));
    }


    @Operation(summary = "닉네임 Validation API", description = "닉네임의 형식, 중복 Validation API")
    @PostMapping(value = "/accounts/nickname/validation")
    public CommonResponse getNicknameValidation(@RequestBody @Valid NicknameReq dto, Errors errors) {
        if (errors.hasErrors()) ValidationExceptionProvider.throwValidError(errors);
        accountService.getNicknameValidation(dto.getNickname());
        return responseService.getSuccessResponse();
    }

    @Operation(summary = "이메일 Validation API", description = "이메일의 형식, 중복 Validation API")
    @PostMapping(value = "/accounts/email/validation")
    public CommonResponse getEmailValidation(@RequestBody @Valid EmailReq dto, Errors errors) {
        if (errors.hasErrors()) ValidationExceptionProvider.throwValidError(errors);
        accountService.getEmailValidation(dto.getEmail());
        return responseService.getSuccessResponse();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "회원 정보 수정 API", description = "JWT 토큰을 기준으로 요청하는 회원 정보 수정(NULL일 땐 변경 x)")
    @PatchMapping(value = "/accounts/information")
    public CommonResponse changeAccountInform(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                              @RequestBody @Valid AccountInformReq dto ,
                                              Errors errors) {
        if (errors.hasErrors()) ValidationExceptionProvider.throwValidError(errors);
        accountService.changeAccountInform(customUserDetails, dto);
        return responseService.getSuccessResponse();
    }

    @Operation(summary = "회원가입 API", description = "형식에 맞는 DTO로 리퀘스트 -> 회원가입")
    @PostMapping(value = "/sign-up")
    public DataResponse<AccountAuthDto> signUp(@RequestBody @Valid AccountAuthDto dto, Errors errors){
        if (errors.hasErrors()) ValidationExceptionProvider.throwValidError(errors);
        return responseService.getDataResponse(accountService.signUp(dto));
    }

}
