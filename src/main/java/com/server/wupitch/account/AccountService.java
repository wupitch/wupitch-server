package com.server.wupitch.account;

import com.server.wupitch.account.dto.AccountAuthDto;
import com.server.wupitch.account.dto.SignInReq;
import com.server.wupitch.account.dto.SignInRes;
import com.server.wupitch.account.entity.Account;
import com.server.wupitch.configure.response.exception.CustomException;
import com.server.wupitch.configure.response.exception.CustomExceptionStatus;
import com.server.wupitch.configure.security.authentication.CustomUserDetails;
import com.server.wupitch.configure.security.jwt.JwtTokenProvider;
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


    @Transactional
    public SignInRes signIn(SignInReq req) {
        Account account = accountRepository.findByEmailAndStatus(req.getEmail(), VALID)
                .orElseThrow(()-> new CustomException(CustomExceptionStatus.FAILED_TO_LOGIN));
        if(!passwordEncoder.matches(req.getPassword(),account.getPassword())){
            throw new CustomException(CustomExceptionStatus.FAILED_TO_LOGIN);
        }

        SignInRes res = SignInRes.builder()
                .accountId(account.getAccountId())
                .oAuthId(account.getOAuthId())
                .jwt(jwtTokenProvider.createToken(account.getEmail(), account.getRole()))
                .build();

        return res;
    }

    public AccountAuthDto getAuthAccount(CustomUserDetails customUserDetails) {
        Account account = customUserDetails.getAccount();
        AccountAuthDto accountInfoDto = account.getAccountInfoDto();
        accountInfoDto.setJwt(jwtTokenProvider.createToken(account.getEmail(), account.getRole()));
        return accountInfoDto;
    }

    public void getNicknameValidation(String nickname) {
        Optional<Account> accountOptional = accountRepository.findByNicknameAndStatus(nickname, VALID);
        if(accountOptional.isPresent()) throw new CustomException(CustomExceptionStatus.DUPLICATED_NICKNAME);
    }
}
