package com.server.wupitch.account;

import com.server.wupitch.account.dto.*;
import com.server.wupitch.account.entity.Account;
import com.server.wupitch.area.Area;
import com.server.wupitch.area.AreaRepository;
import com.server.wupitch.configure.entity.Status;
import com.server.wupitch.configure.response.exception.CustomException;
import com.server.wupitch.configure.response.exception.CustomExceptionStatus;
import com.server.wupitch.configure.s3.S3Uploader;
import com.server.wupitch.configure.security.authentication.CustomUserDetails;
import com.server.wupitch.configure.security.jwt.JwtTokenProvider;
import com.server.wupitch.fcm.FirebaseCloudMessageService;
import com.server.wupitch.sports.entity.AccountSportsRelation;
import com.server.wupitch.sports.entity.Sports;
import com.server.wupitch.sports.repository.AccountSportsRelationRepository;
import com.server.wupitch.sports.repository.SportsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.server.wupitch.configure.entity.Status.DELETED;
import static com.server.wupitch.configure.entity.Status.VALID;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AreaRepository areaRepository;
    private final SportsRepository sportsRepository;
    private final AccountSportsRelationRepository accountSportsRelationRepository;
    private final S3Uploader s3Uploader;
    private final FirebaseCloudMessageService firebaseCloudMessageService;


    @Transactional
    public SignInRes signIn(SignInReq req) {
        Account account = accountRepository.findByEmailAndStatus(req.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.FAILED_TO_LOGIN));
        if (!passwordEncoder.matches(req.getPassword(), account.getPassword())) {
            throw new CustomException(CustomExceptionStatus.FAILED_TO_LOGIN);
        }

        SignInRes res = SignInRes.builder()
                .accountId(account.getAccountId())
                .email(account.getEmail())
                .jwt(jwtTokenProvider.createToken(account.getEmail(), account.getRole()))
                .build();

        return res;
    }

    public AccountAuthDto getAuthAccount(CustomUserDetails customUserDetails) {
        Account account = customUserDetails.getAccount();
        AccountAuthDto accountInfoDto = account.getAccountInfoDto();
        accountInfoDto.setJwt(jwtTokenProvider.createToken(account.getOAuthId(), account.getRole()));
        return accountInfoDto;
    }

    public void getNicknameValidation(String nickname) {
        Optional<Account> accountOptional = accountRepository.findByNicknameAndStatus(nickname, VALID);
        if (accountOptional.isPresent()) throw new CustomException(CustomExceptionStatus.DUPLICATED_NICKNAME);
    }

    @Transactional
    public void changeAccountInform(CustomUserDetails customUserDetails, AccountInformReq dto) {
        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_FOUND));

        account.setAccountInfoByDto(dto);

        Optional<Area> optional = areaRepository.findByAreaIdAndStatus(dto.getAreaId(), VALID);
        if (optional.isPresent()) {
            account.setAccountArea(optional.get());
        }

        if (dto.getSportsList() != null) {
            List<AccountSportsRelation> sportsList = accountSportsRelationRepository.findAllByAccountAndStatus(account, VALID);
            for (AccountSportsRelation accountSportsRelation : sportsList) {
                accountSportsRelation.makeDeleted();
            }
            for (Long sportsId : dto.getSportsList()) {
                Sports sports = sportsRepository.findBySportsIdAndStatus(sportsId, VALID)
                        .orElseThrow(() -> new CustomException(CustomExceptionStatus.SPORTS_NOT_FOUND));
                AccountSportsRelation accountSportsRelation = AccountSportsRelation.builder()
                        .status(VALID)
                        .account(account)
                        .sports(sports)
                        .build();
                accountSportsRelationRepository.save(accountSportsRelation);
            }
        }

    }

    @Transactional
    public AccountAuthDto signUp(AccountAuthDto dto) throws IOException {
        if (accountRepository.findByEmail(dto.getEmail()).isPresent())
            throw new CustomException(CustomExceptionStatus.DUPLICATED_EMAIL);
        if (dto.getNickname() != null) {
            if (accountRepository.findByNickname(dto.getNickname()).isPresent())
                throw new CustomException(CustomExceptionStatus.DUPLICATED_NICKNAME);
        }

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        Account account = Account.createAccount(dto);
        Account save = accountRepository.save(account);
        dto.setAccountId(save.getAccountId());
        dto.setJwt(jwtTokenProvider.createToken(account.getEmail(), account.getRole()));
        firebaseCloudMessageService.sendMessageTo(save, dto.getDeviceToken(), "회원가입 완료", "신분증 인증이 완료됐습니다.\uD83C\uDF89\n우피치와 신나고 건강하게 땀흘려요!");
        return dto;
    }

    public void getEmailValidation(String email) {
        Optional<Account> accountOptional = accountRepository.findByEmailAndStatus(email, VALID);
        if (accountOptional.isPresent()) throw new CustomException(CustomExceptionStatus.DUPLICATED_EMAIL);
    }

    @Transactional
    public void uploadProfileImage(MultipartFile multipartFile, CustomUserDetails customUserDetails) throws IOException {
        String profileImageUrl = s3Uploader.upload(multipartFile, "profileImage");
        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_FOUND));
        account.registerProfileImage(profileImageUrl);
    }

    @Transactional
    public void uploadIdentification(MultipartFile multipartFile, CustomUserDetails customUserDetails) throws IOException {
        String identificationImage = s3Uploader.upload(multipartFile, "identification");
        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_FOUND));
        account.registerIdentification(identificationImage);
    }

    @Transactional
    public void toggleAccountValidation(CustomUserDetails customUserDetails) {
        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_FOUND));
        account.toggleValid();
    }

    @Transactional
    public void toggleAccountAlarmInfo(CustomUserDetails customUserDetails) {
        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_FOUND));
        account.toggleAlarmInfo();
    }

    @Transactional
    public void toggleInvalidStatus(String email) {
        Account account = accountRepository.findByEmailAndStatus(email, DELETED)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));
        account.restoreAccount();
    }

    @Transactional
    public void changeAuthPassword(CustomUserDetails customUserDetails, ChangePwdReq dto) {
        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));
        account.changePassword(passwordEncoder.encode(dto.getPassword()));
    }

    public Boolean chkPasswordByAuth(CustomUserDetails customUserDetails, PasswordChkReq dto) {
        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));

        return passwordEncoder.matches(dto.getPassword(), account.getPassword());
    }

    public AccountSportsRes getSportsByAuth(CustomUserDetails customUserDetails) {
        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));
        AccountSportsRes res = new AccountSportsRes();
        res.setAccountId(account.getAccountId());
        List<AccountSportsRelation> sportsRelationList = accountSportsRelationRepository.findAllByAccountAndStatus(account, VALID);
        for (AccountSportsRelation accountSportsRelation : sportsRelationList) {
            Sports sports = accountSportsRelation.getSports();
            res.getList().add(new AccountSportsRes.SportsInfo(sports.getSportsId(), sports.getName()));
        }
        return res;
    }

    public AccountAgeRes getAgeByAuth(CustomUserDetails customUserDetails) {
        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));
        Integer ageIdx = account.getAgeNum();
        String age;
        if (ageIdx == null){
            age = "나이를 등록하지 않았습니다.";
        }
        else if (ageIdx == 1) age = "10대";
        else if (ageIdx == 2) age = "20대";
        else if (ageIdx == 3) age = "30대";
        else if (ageIdx == 4) age = "40대";
        else age = "50대 이상";
        return new AccountAgeRes(ageIdx, age);
    }

    public AccountPhoneRes getPhoneNumberByAuth(CustomUserDetails customUserDetails) {
        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));
        return new AccountPhoneRes(account.getPhoneNumber());
    }

    public AccountAreaRes getAreaByAuth(CustomUserDetails customUserDetails) {
        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));
        Area area = account.getArea();
        return new AccountAreaRes(area);
    }


    @Transactional
    public void changeDeviceTokenByAuth(CustomUserDetails customUserDetails, DeviceTokenReq deviceTokenReq) {
        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(()-> new CustomException(CustomExceptionStatus.FAILED_TO_LOGIN));
        account.changeDeviceToken(deviceTokenReq.getDeviceToken());
    }

}
