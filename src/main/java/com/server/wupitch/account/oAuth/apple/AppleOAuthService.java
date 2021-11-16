package com.server.wupitch.account.oAuth.apple;

import com.server.wupitch.account.AccountRepository;
import com.server.wupitch.account.AccountService;
import com.server.wupitch.account.dto.AccountAuthDto;
import com.server.wupitch.account.dto.SignInReq;
import com.server.wupitch.account.dto.SignInRes;
import com.server.wupitch.account.entity.Account;
import com.server.wupitch.account.entity.enumtypes.OAuthType;
import com.server.wupitch.configure.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Transactional
@RequiredArgsConstructor
@Service
public class AppleOAuthService {

    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${OAuth.APPLE_TOKEN}")
    private String APPLE_TOKEN;

    public SignInRes appleLogin(AppleUserInfo appleUserInfo) {
        String oAuthId = "apple_" + appleUserInfo.getEmail();

        String nickname = appleUserInfo.getNickname();
        String email = appleUserInfo.getEmail();

        Account account = null;
        Optional<Account> byKakaoIdAndStatus = accountRepository.findByoAuthIdAndStatus(oAuthId, Status.VALID);
        if (byKakaoIdAndStatus.isPresent()) account = byKakaoIdAndStatus.get();
        else {
            AccountAuthDto accountReq = AccountAuthDto.builder()
                    .email(email)
                    .nickname(nickname)
                    .password(passwordEncoder.encode(oAuthId + APPLE_TOKEN))
                    .build();

            Account newAccount = Account.createAccount(accountReq, OAuthType.APPLE);
            Account save = accountRepository.save(newAccount);
            account = save;

        }

        SignInReq signInreq = SignInReq.builder()
                .oAuthId(oAuthId)
                .email(account.getEmail())
                .password(oAuthId+APPLE_TOKEN)
                .build();


        return accountService.signIn(signInreq);

    }
}
