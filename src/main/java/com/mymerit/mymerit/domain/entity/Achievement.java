package com.mymerit.mymerit.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Achievement {
    private String name;
    private String description;
    private String base64image;
}
