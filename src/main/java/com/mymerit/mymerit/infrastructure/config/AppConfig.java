package com.mymerit.mymerit.infrastructure.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Getter
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    private final OAuth2 oauth2 = new OAuth2();

    @Getter
    public static class OAuth2 {
        private final List<String> authorizedRedirectUris = new ArrayList<>();
    }
}