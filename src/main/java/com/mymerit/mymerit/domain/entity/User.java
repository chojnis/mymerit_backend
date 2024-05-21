package com.mymerit.mymerit.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mymerit.mymerit.domain.models.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Document("users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    private String imageBase64;
    private AuthProvider provider;
    private String providerId;
    private Integer credits = 0;
    private String description;
    private String role;
    private List<Achievement> achievements = new ArrayList<>();
    private AchievementProgress achievementProgress = new AchievementProgress();
    private Integer logInStreak = 0;
    private Date lastLoginDate = new Date();
    private Ranking ranking = new Ranking();

    public void checkSolutionAchievementStatus(){
        this.achievements = this.achievementProgress.updateUserSolutionAchievements();
    }
    public void checkCreditsAchievementStatus(Integer credits){
        this.achievements = this.achievementProgress.updateCreditAchievements(credits);
    }

    public void setLastLoginDate(){
        this.lastLoginDate = new Date();
    }


    public void calculateRanking(Integer averageRanking, Integer credits) {
        int difference = Math.abs(ranking.getRanking() - averageRanking) + credits / 10;

        if (ranking.getRanking() < averageRanking) {
            getRanking().addRanking((100+difference)/5);
        } else if (ranking.getRanking() > averageRanking) {
            getRanking().addRanking((100 - difference) / 5);
        } else {
            getRanking().addRanking(20);
        }
    }

}


