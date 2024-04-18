package com.mymerit.mymerit.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("tests")
@NoArgsConstructor
@AllArgsConstructor
public class TestCase {




    private String name;

    private String input;

    private Boolean status;

    private String expectedOutput;
}
