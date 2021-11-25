package com.server.wupitch.account;

import com.server.wupitch.account.dto.*;
import com.server.wupitch.account.entity.Account;
import com.server.wupitch.area.Area;
import com.server.wupitch.area.AreaRepository;
import com.server.wupitch.configure.response.exception.CustomException;
import com.server.wupitch.configure.response.exception.CustomExceptionStatus;
import com.server.wupitch.configure.s3.S3Uploader;
import com.server.wupitch.configure.security.authentication.CustomUserDetails;
import com.server.wupitch.configure.security.jwt.JwtTokenProvider;
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


    @Transactional
    public SignInRes signIn(SignInReq req) {
        Account account = accountRepository.findByEmailAndStatus(req.getEmail(), VALID)
                .orElseThrow(()-> new CustomException(CustomExceptionStatus.FAILED_TO_LOGIN));
        if(!passwordEncoder.matches(req.getPassword(),account.getPassword())){
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
        if(accountOptional.isPresent()) throw new CustomException(CustomExceptionStatus.DUPLICATED_NICKNAME);
    }

    @Transactional
    public void changeAccountInform(CustomUserDetails customUserDetails, AccountInformReq dto) {
        Account account = accountRepository.findByoAuthIdAndStatus(customUserDetails.getOAuthId(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_FOUND));

        account.setAccountInfoByDto(dto);

        Area area = areaRepository.findByAreaIdAndStatus(dto.getAreaId(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AREA_NOT_FOUND));

        account.setAccountArea(area);

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

    @Transactional
    public AccountAuthDto signUp(AccountAuthDto dto) {
        if (accountRepository.findByEmail(dto.getEmail()).isPresent()) throw new CustomException(CustomExceptionStatus.DUPLICATED_EMAIL);
        if (dto.getNickname() != null){
            if (accountRepository.findByNickname(dto.getNickname()).isPresent()) throw new CustomException(CustomExceptionStatus.DUPLICATED_NICKNAME);
        }

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        Account account = Account.createAccount(dto);
        Account save = accountRepository.save(account);
        dto.setAccountId(save.getAccountId());
        dto.setJwt(jwtTokenProvider.createToken(account.getEmail(),account.getRole()));
        return dto;
    }

    public void getEmailValidation(String email) {
        Optional<Account> accountOptional = accountRepository.findByEmailAndStatus(email, VALID);
        if(accountOptional.isPresent()) throw new CustomException(CustomExceptionStatus.DUPLICATED_EMAIL);
    }

    @Transactional
    public void uploadProfileImage(MultipartFile multipartFile, CustomUserDetails customUserDetails) throws IOException {
        String profileImageUrl = s3Uploader.upload(multipartFile, "profileImage");
        Account account = accountRepository.findByEmailAndStatus(customUserDetails.getEmail(), VALID)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_FOUND));
        account.registerProfileImage(profileImageUrl);
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
}
