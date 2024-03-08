package com.mymerit.mymerit.domain.entity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.jsr310.LocalDateTimeCodec;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


@Data

@Document("tasks")
public class Task {

    @Id        public String id;

    @NotNull   public String topic;

    @NotNull   public String description;

               public LocalDateTime releaseDate;

               public LocalDateTime expiryDate;

    @NotNull  public Integer reward;

    @NotNull   public Company company;



    @NotEmpty  public List<String> allowedTechnologies;

               public List<Solution> solutions;

               public Integer timeLeft;




        public Task(String topic, String description, LocalDateTime expiryDate, Integer reward, Company company,
                    List<String> allowedTechnologies
                 ){

            this.topic = topic;
            this.description = description;
            this.releaseDate = LocalDateTime.now();
            this.expiryDate  = expiryDate;
            this.company = company;

            this.allowedTechnologies = allowedTechnologies;

            this.reward = reward;
            this.solutions = new ArrayList<>();



        }

        public Integer getSolutionCount(){
            return  solutions.size();
        }

}
