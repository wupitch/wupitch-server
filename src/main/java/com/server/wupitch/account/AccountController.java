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
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;

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

    @Operation(summary = "패스워드 Validation API", description = "비밀번호의 형식 Validation API")
    @PostMapping(value = "/accounts/password/validation")
    public CommonResponse getPasswordValidation(@RequestBody @Valid PasswordReq dto, Errors errors) {
        if (errors.hasErrors()) ValidationExceptionProvider.throwValidError(errors);
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
        if (dto.getNickname() != null && errors.hasErrors()) ValidationExceptionProvider.throwValidError(errors);
        accountService.changeAccountInform(customUserDetails, dto);
        return responseService.getSuccessResponse();
    }


    @Operation(summary = "회원가입 API", description = "형식에 맞는 DTO로 리퀘스트 -> 회원가입")
    @PostMapping(value = "/sign-up")
    public DataResponse<AccountAuthDto> signUp(@RequestBody @Valid AccountAuthDto dto, Errors errors) throws IOException {
        if (errors.hasErrors()) ValidationExceptionProvider.throwValidError(errors);
        return responseService.getDataResponse(accountService.signUp(dto));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "사진등록 API", description = "JWT 토큰을 기준으로 현재 회원의 이미지 등록")
    @PostMapping(value = "/accounts/image")
    public CommonResponse uploadProfileImage(@RequestParam("images") MultipartFile multipartFile,
                                             @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        accountService.uploadProfileImage(multipartFile, customUserDetails);

        return responseService.getSuccessResponse();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "프로필 사진 삭제 api API", description = "JWT 토큰을 기준으로 현재 회원의 프로필 사진 삭제")
    @PatchMapping(value = "/accounts/image/empty")
    public CommonResponse deleteProfileImage(@AuthenticationPrincipal CustomUserDetails customUserDetails)  {
        accountService.deleteProfileImage(customUserDetails);

        return responseService.getSuccessResponse();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "신분증 사진등록 API", description = "JWT 토큰을 기준으로 현재 회원의 신분증 이미지 등록")
    @PostMapping(value = "/accounts/identification")
    public CommonResponse uploadIdentification(@RequestParam("images") MultipartFile multipartFile,
                                             @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException, MessagingException {
        accountService.uploadIdentification(multipartFile, customUserDetails);

        return responseService.getSuccessResponse();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "회원 삭제 API", description = "JWT 토큰을 기준으로 회원의 Status를 변경")
    @PatchMapping(value = "/accounts/toggle-status")
    public CommonResponse toggleAccountValidation(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        accountService.toggleAccountValidation(customUserDetails);
        return responseService.getSuccessResponse();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "회원 알림 동의 변경 API", description = "JWT 토큰을 기준으로 토글 방식으로 동의 변경")
    @PatchMapping(value = "/accounts/toggle-alarm-info")
    public CommonResponse toggleAccountAlarmInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        accountService.toggleAccountAlarmInfo(customUserDetails);
        return responseService.getSuccessResponse();
    }

    @Operation(summary = "회원 복구 API", description = "이메일을 기준으로 회원 복구")
    @PatchMapping(value = "/accounts/invalid-status/{email}")
    public CommonResponse toggleInvalidStatus(@PathVariable String email){
        accountService.toggleInvalidStatus(email);
        return responseService.getSuccessResponse();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "비밀번호 변경 API", description = "JWT 토큰을 기준으로 비밀번호 변경")
    @PatchMapping(value = "/accounts/auth/password")
    public CommonResponse changeAuthPassword(@AuthenticationPrincipal CustomUserDetails customUserDetails,@RequestBody @Valid ChangePwdReq dto, Errors errors){
        if (errors.hasErrors()) ValidationExceptionProvider.throwValidError(errors);
        accountService.changeAuthPassword(customUserDetails, dto);
        return responseService.getSuccessResponse();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "비밀번호 확인 API", description = "JWT 토큰을 기준으로 현재 회원의 비밀번호가 맞는지 확인")
    @PostMapping(value = "/accounts/auth/password/check")
    public DataResponse<Boolean> chkPasswordByAuth(
            @AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody PasswordChkReq dto)
    {
        Boolean result = accountService.chkPasswordByAuth(customUserDetails, dto);
        return responseService.getDataResponse(result);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "인증된 회원의 스포츠 확인 API", description = "JWT 토큰을 기준으로 현재 회원의 스포츠 확인")
    @GetMapping(value = "/accounts/auth/sports")
    public DataResponse<AccountSportsRes> getSportsByAuth(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        AccountSportsRes accountSportsRes = accountService.getSportsByAuth(customUserDetails);
        return responseService.getDataResponse(accountSportsRes);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "인증된 회원의 나이대 확인 API", description = "JWT 토큰을 기준으로 현재 회원의 나이대 확인")
    @GetMapping(value = "/accounts/auth/age")
    public DataResponse<AccountAgeRes> getAgeByAuth(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        AccountAgeRes accountAgeRes = accountService.getAgeByAuth(customUserDetails);
        return responseService.getDataResponse(accountAgeRes);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "인증된 회원의 폰 넘버 확인 API", description = "JWT 토큰을 기준으로 현재 회원의 폰 넘버 확인")
    @GetMapping(value = "/accounts/auth/phoneNumber")
    public DataResponse<AccountPhoneRes> getPhoneNumberByAuth(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        AccountPhoneRes accountPhoneRes = accountService.getPhoneNumberByAuth(customUserDetails);
        return responseService.getDataResponse(accountPhoneRes);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "인증된 회원의 지역 확인 API", description = "JWT 토큰을 기준으로 현재 회원의 지역 확인")
    @GetMapping(value = "/accounts/auth/area")
    public DataResponse<AccountAreaRes> getAreaByAuth(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        AccountAreaRes accountAreaRes = accountService.getAreaByAuth(customUserDetails);
        return responseService.getDataResponse(accountAreaRes);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "로그인 성공 후 토큰", dataTypeClass = String.class, paramType = "header")
    })
    @Operation(summary = "현재 인증된 회원 디바이스 토큰 수정 API", description = "JWT 토큰을 기준으로 인증된 회원 디바이스 정보 수정")
    @PatchMapping(value = "/device-token")
    public CommonResponse changeDeviceTokenByAuth(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody DeviceTokenReq dto) {
        accountService.changeDeviceTokenByAuth(customUserDetails, dto);
        return responseService.getSuccessResponse();
    }

    @Operation(summary = "회원 가입 승인 API", description = "AccountId를 기준으로 인증된 회원 가입 처리 및 FCM 전달")
    @GetMapping(value = "/accounts/auth/{accountId}/enrollment-agree")
    public DataResponse<ResultDto> enrollmentAgree(@PathVariable(name = "accountId") Long accountId) throws IOException {
        accountService.enrollmentAgree(accountId);
        ResultDto resultDto = new ResultDto(accountId + "번의 회원 가입 승인이 완료됐습니다.");
        return responseService.getDataResponse(resultDto);
    }

    @Operation(summary = "회원 가입 보류 API", description = "AccountId를 기준으로 인증된 회원 가입 보류 및 FCM 전달")
    @GetMapping(value = "/accounts/auth/{accountId}/enrollment-deny")
    public DataResponse<ResultDto> enrollmentDeny(@PathVariable(name = "accountId") Long accountId) throws IOException {
        accountService.enrollmentDeny(accountId);
        ResultDto resultDto = new ResultDto(accountId + "번의 회원 가입 보류가 완료됐습니다.");
        return responseService.getDataResponse(resultDto);
    }

}
