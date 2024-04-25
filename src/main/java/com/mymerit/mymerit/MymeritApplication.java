package com.mymerit.mymerit;

import com.mymerit.mymerit.domain.entity.ConfigFile;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

import static com.mymerit.mymerit.infrastructure.utils.ZipUtility.getSourceFileForLanguage;


@SpringBootApplication
public class MymeritApplication {
	public static void main(String[] args) throws IOException {

		SpringApplication.run(MymeritApplication.class, args);

		getSourceFileForLanguage("cpp","main.cpp");




	}
}