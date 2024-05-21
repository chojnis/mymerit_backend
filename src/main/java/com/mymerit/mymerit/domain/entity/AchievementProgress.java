package com.mymerit.mymerit.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data

public class AchievementProgress {

    private Integer  solutionsCount ;
    private Integer  creditsCount ;
    private List<Achievement> achievements ;

    private boolean creditsAchieved      = false;
    private boolean creditsAchieved1000  = false;
    private boolean creditsAchieved5000  = false;
    private boolean creditsAchieved10000 = false;


    public AchievementProgress(){
        this.solutionsCount = 0;
        this.creditsCount = 0;
        this.achievements = new ArrayList<>();
    }



    public List<Achievement> updateUserSolutionAchievements(){
        updateSolutionsStatus();
        return this.achievements;
    }

    public List<Achievement> updateCreditAchievements(Integer amount){
        updateCreditsStatus(amount);
        return achievements;
    }

    public void updateSolutionsStatus(){
        this.solutionsCount++;

        if(solutionsCount == 1){
            Achievement firstTaskAchievement = new Achievement("First solution","Submit your first solution","");
            achievements.add(firstTaskAchievement);
        }

        if(solutionsCount == 5){
            Achievement fifthSolution = new Achievement("Fifth solution","Submit your fifth solution","");
            achievements.add(fifthSolution);
        }

        if(solutionsCount == 10){
            Achievement tenthSolution = new Achievement("Tenth solution","Submit your tenth solution","");
            achievements.add(tenthSolution);
        }

        if(solutionsCount == 25){
            Achievement twentyFifthSolution = new Achievement("Twenty-Fifth solution","Submit your twenty-fifth solution", "");
            achievements.add(twentyFifthSolution);
        }

        if(solutionsCount == 50){
            Achievement fiftiethSolution = new Achievement("Fiftieth solution","Submit your fiftieth solution","");
            achievements.add(fiftiethSolution);
        }

        if(solutionsCount == 100){
            Achievement hundredthSolution = new Achievement("Sisyphus","Submit your hundredth solution","");
            achievements.add(hundredthSolution);
        }
    }



    public void updateCreditsStatus(Integer creditsCount){
        this.creditsCount += creditsCount;

        if(!creditsAchieved){
            Achievement firstCredits = new Achievement("First points","Earn your first merit points","");
            achievements.add(firstCredits);
            creditsAchieved = true;
        }
        if(!creditsAchieved1000 && this.creditsCount > 1000){
            Achievement secondCredits = new Achievement("Earn1000 points","Earn 1000 merit points","");
            achievements.add(secondCredits);
            creditsAchieved1000 = true;

        }
        if(!creditsAchieved5000 && this.creditsCount > 5000){
            Achievement thirdCredits = new Achievement("Earn 5000 points","Earn 5000 merit points","");
            achievements.add(thirdCredits);
            creditsAchieved5000 = true;
        }
        if(!creditsAchieved10000 && this.creditsCount > 10000){
            Achievement fourthCredits = new Achievement("Earn 10000 points","Earn 10000 merit points","");
            achievements.add(fourthCredits);
            creditsAchieved10000 = true;
        }

    }

    @Override
    public String toString() {
        return "AchievementProgress{" +
                "solutionsCount=" + solutionsCount +
                ", creditsCount=" + creditsCount +
                ", achievements=" + achievements +
                '}';
    }
}
