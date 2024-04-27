package com.mymerit.mymerit.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("socials")
@Data
public class Socials {
    @Id
    String socialId;
    String name;
    String link;
    String userId;
    String image;
}
