package com.server.wupitch.account;

import com.server.wupitch.account.dto.AccountAuthDto;
import com.server.wupitch.account.dto.AccountInformReq;
import com.server.wupitch.account.dto.SignInReq;
import com.server.wupitch.account.dto.SignInRes;
import com.server.wupitch.account.entity.Account;
import com.server.wupitch.area.Area;
import com.server.wupitch.area.AreaRepository;
import com.server.wupitch.configure.response.exception.CustomException;
import com.server.wupitch.configure.response.exception.CustomExceptionStatus;
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

import java.util.Optional;

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
        if (accountRepository.findByEmailAndStatus(dto.getEmail(), VALID).isPresent()) throw new CustomException(CustomExceptionStatus.DUPLICATED_EMAIL);
        if (dto.getNickname() != null){
            if (accountRepository.findByNicknameAndStatus(dto.getNickname(), VALID).isPresent()) throw new CustomException(CustomExceptionStatus.DUPLICATED_NICKNAME);
        }

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        Account account = Account.createAccount(dto);
        Account save = accountRepository.save(account);
        dto.setAccountId(save.getAccountId());
        dto.setJwt(jwtTokenProvider.createToken(account.getEmail(),account.getRole()));
        return dto;
    }
}
