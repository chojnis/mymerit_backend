package com.mymerit.mymerit.domain.service;

import com.mymerit.mymerit.domain.entity.Reward;
import com.mymerit.mymerit.domain.entity.User;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MailSenderService {
    private final Logger logger = LoggerFactory.getLogger(MailSenderService.class);
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final JavaMailSenderImpl mailSender;

    public MailSenderService(JavaMailSender javaMailSender, TemplateEngine templateEngine, JavaMailSenderImpl mailSender) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.mailSender = mailSender;
    }

    public void sendVerificationCode(String mailDestination, Integer verificationCode) {
        if (isEmailCorrect(mailDestination)) {
            MimeMessage mimeMailMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage, "utf-8");

            try {
                helper.setFrom("no-reply@mymerit.pl", "MyMerit");
                helper.setTo(mailDestination);
                helper.setSubject("Verify your email");

                Context context = new Context();
                context.setVariable("verificationCode", verificationCode);

                String htmlContent = templateEngine.process("email-verification", context);
                helper.setText(htmlContent, true);

                mimeMailMessage.setDescription("Your verification code: " + verificationCode);
            } catch (Exception e) {
                logger.info("Error while building email with verification code", e);
            }

            try {
                javaMailSender.send(mimeMailMessage);

                logger.info("Mail with verification code sent successfully");
            } catch (Exception e) {
                logger.info("Error while sending verification code message", e);
            }
        } else {
            logger.info("Incorrect email syntax for sending verification code");
        }
    }

    public void sendReward(User user, Reward reward, String code) {
        if (isEmailCorrect(user.getEmail())) {
            MimeMessage mimeMailMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage, "utf-8");

            try {
                helper.setFrom("no-reply@mymerit.pl", "MyMerit");
                helper.setTo(user.getEmail());
                helper.setSubject("Claim your reward");

                Map<String, Object> variables = new HashMap<>();
                variables.put("username", user.getUsername());
                variables.put("rewardName", reward.getName());
                variables.put("code", code);

                Context context = new Context();
                context.setVariables(variables);

                String htmlContent = templateEngine.process("reward", context);
                helper.setText(htmlContent, true);

                mimeMailMessage.setDescription("We have sent the activation code to receive the reward");
            } catch (Exception e) {
                logger.info("Error while building reward email", e);
            }

            try {
                javaMailSender.send(mimeMailMessage);

                logger.info("The reward email was sent successfully");
            } catch (Exception e) {
                logger.info("Error while sending reward emails", e);
            }
        } else {
            logger.info("Incorrect email syntax for sending reward code");
        }
    }

    private boolean isEmailCorrect(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);

        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public Integer generateVerificationCode() {
        int min = 1000;
        int max = 9999;

        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }
}
