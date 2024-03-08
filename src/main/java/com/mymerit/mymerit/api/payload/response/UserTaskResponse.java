package com.mymerit.mymerit.api.payload.response;

import com.mymerit.mymerit.domain.entity.Company;
import com.mymerit.mymerit.domain.entity.Solution;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

public class UserTaskResponse {


        @Id
        public String id;

        @NotNull
        public String topic;

        @NotNull   public String description;

        public LocalDateTime releaseDate;

        public LocalDateTime expiryDate;

        @NotNull  public Integer reward;

        @NotNull   public Company company;



        @NotEmpty
        public List<String> allowedTechnologies;

       public Solution solution;

        public Integer timeLeft;

    public UserTaskResponse( String id,@NotNull String topic,
                            @NotNull String description,
                            LocalDateTime releaseDate,
                            LocalDateTime expiryDate,
                            @NotNull Integer reward,
                            @NotNull Company company,
                            List<String> allowedTechnologies,
                            Solution solution,
                            Integer timeLeft) {
        this.id = id;
        this.topic = topic;
        this.description = description;
        this.releaseDate = releaseDate;
        this.expiryDate = expiryDate;
        this.reward = reward;
        this.company = company;
        this.allowedTechnologies = allowedTechnologies;
        this.solution = solution;
        this.timeLeft = timeLeft;
    }
}













