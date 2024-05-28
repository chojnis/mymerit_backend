package com.mymerit.mymerit.api.payload.response;

import lombok.Data;

@Data
public class TestResponse {
   public String name;
   public String errorMessage;
   public Boolean passed;
}
