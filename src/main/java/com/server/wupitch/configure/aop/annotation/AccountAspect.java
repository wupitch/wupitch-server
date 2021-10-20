package com.server.wupitch.configure.aop.annotation;

import com.server.wupitch.account.entity.Account;
import com.server.wupitch.configure.Repository.AccountLogEntityRepository;
import com.server.wupitch.configure.entity.AccountLogEntity;
import com.server.wupitch.configure.security.authentication.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
@Aspect
public class AccountAspect {

    Logger logger = LoggerFactory.getLogger(AccountAspect.class);
    private final AccountLogEntityRepository accountLogEntityRepository;

    @Around("@annotation(com.project.kcookserver.configure.aop.annotation.AccountLog)")
    public Object AccountLogAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed = joinPoint.proceed();

        Account account = null;
        String userName = "anonymousUser";
        String userRole = "null";
        Long accountd = null;

        if (!SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal().equals("anonymousUser")) {
            CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            account = user.getAccount();
            userName = user.getUsername();
            userRole = user.getAccount().getRole().toString();
            accountd = user.getAccount().getAccountId();
        }

        LocalDateTime localDateTime = LocalDateTime.now();
        logger.info("--Authenticated Account Access-- [Date : "+localDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))+"] [AccountId : "+accountd+"] [UserName : "+userName+"] [UserRole : "+userRole+"] [Method : "+joinPoint.getSignature().getName()+"]");
        AccountLogEntity accountLogEntity = new AccountLogEntity(account, localDateTime, joinPoint.getSignature().getName());

        accountLogEntityRepository.save(accountLogEntity);

        return proceed;
    }

}
