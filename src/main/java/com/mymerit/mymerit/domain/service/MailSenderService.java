package com.mymerit.mymerit.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MailSenderService {


    @Autowired
    private JavaMailSender javaMailSender;


    public void sendEmail(String mailDestination){



      if(isEmailCorrect(mailDestination)) {

          SimpleMailMessage mailMessage = new SimpleMailMessage();
          mailMessage.setTo(mailDestination);
          mailMessage.setText("Your verification code is :"  + generateVerificationCode()  );
          mailMessage.setSubject("Verify your MyMerit account");

          javaMailSender.send(mailMessage);


          System.out.println("Mail has been sent succesfully");
      }
      else{
          System.out.println("Incorrect email syntax");
      }

    }

    private boolean isEmailCorrect(String email){

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);

        Matcher matcher = pattern.matcher(email);

        return matcher.matches();

    }

    public Integer generateVerificationCode(){

        int min = 1000; // Minimum value of range
        int max = 9999; // Maximum value of range


        int verificationCode = (int)Math.floor(Math.random() * (max - min + 1) + min);

        return verificationCode;
    }

}
