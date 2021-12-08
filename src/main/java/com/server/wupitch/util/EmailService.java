package com.server.wupitch.util;

import com.server.wupitch.account.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EmailService {

    @Value("${smtp.email}")
    public String email;

    private final JavaMailSender javaMailSender;

    public void sendCheckEmail(Account account) throws MessagingException, UnsupportedEncodingException {
        String subject = "회원가입 확인 메일";
        StringBuilder body = new StringBuilder();
        body.append("<html> <body><h1>회원가입 인증 </h1>");
        body.append("<h2>"+ account.getNickname()+"</h2>");
        body.append("<div>테스트 입니다. </div> </body></html>");

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");

        mimeMessageHelper.setFrom(email,"wupitch");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body.toString(), true);

        javaMailSender.send(message);

    }


}
