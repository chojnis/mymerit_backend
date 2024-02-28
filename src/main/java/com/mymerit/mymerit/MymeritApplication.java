package com.mymerit.mymerit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mymerit.mymerit.mail.MailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class MymeritApplication {
	@Autowired
	private MailSenderService mailSenderService;

	public static void main(String[] args) {



		SpringApplication.run(MymeritApplication.class, args);


		}

	@EventListener(ApplicationReadyEvent.class)
	public void sendEmail(){

         //w configu trzeba konto dac
		mailSenderService.sendEmail("");



	}
}