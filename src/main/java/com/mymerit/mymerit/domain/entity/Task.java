package com.mymerit.mymerit.domain.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.jsr310.LocalDateTimeCodec;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Document
@Data
@Getter
@Setter
public class Task {

    @Id        public Long id;

    @NotNull   public String topic;

    @NotNull   public String description;

               public LocalDateTime releaseDate;

               public LocalDateTime expiryDate;

    @NotNull   public Integer reward;

    @NotNull   public Company company;

               public Integer solutionCount;

    @NotEmpty  public List<String> allowedTechnologies;

               public List<Solution> solutions;

               public Integer maxCredits;

               public Integer minCredits;




        public Task(String topic, String description, LocalDateTime expiryDate, Integer reward, Company company,
                    List<String> allowedTechnologies, List<Solution> solutions, Integer maxCredits,
                    Integer minCredits){

            this.topic = topic;
            this.description = description;
            this.releaseDate = LocalDateTime.now();
            this.expiryDate  = expiryDate;
            this.company = company;
            this.solutionCount++;
            this.allowedTechnologies = allowedTechnologies;
            this.maxCredits = maxCredits;
            this.minCredits = minCredits;
            this.reward = reward;
            this.solutions = solutions;



        }


}
