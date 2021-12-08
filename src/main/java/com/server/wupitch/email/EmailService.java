package com.server.wupitch.email;

import com.server.wupitch.account.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EmailService {

    @Value("${smtp.email}")
    public String email;

    private final JavaMailSender javaMailSender;

    private final MailProperties mailProperties;

    private final TemplateEngine htmlTemplateEngine;

    public void sendRegisterCheckEmail(Map<String, Object> variables) throws MessagingException, UnsupportedEncodingException {
        String subject = "회원가입 확인 메일";
//        StringBuilder body = new StringBuilder();
//        body.append("<html> <body><h1>회원가입 인증 </h1>");
//        body.append("<h2>"+ account.getEmail()+"</h2>");
//        body.append("<h3>"+ account.getNickname()+"</h3>");
//        body.append("<div><img src = "+"\""+account.getIdentification()+"\""+"></div>");

        Context context = new Context();
        context.setVariables(variables);


        String htmlTemplate = htmlTemplateEngine.process("mail/mail", context);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");

        messageHelper.setFrom(email,"wupitch");
        messageHelper.setTo(email);
        messageHelper.setSubject(subject);
        messageHelper.setText(htmlTemplate,true);

        javaMailSender.send(mimeMessage);

    }


}
