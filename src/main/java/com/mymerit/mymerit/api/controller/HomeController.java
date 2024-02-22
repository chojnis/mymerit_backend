package com.mymerit.mymerit.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping()
    public String home() {
        return "home page";
    }

    @GetMapping("/secured-resource")
    public String secured() {
        return "logged in";
    }
}