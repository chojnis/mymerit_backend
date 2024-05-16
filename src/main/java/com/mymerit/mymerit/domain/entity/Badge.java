package com.mymerit.mymerit.domain.entity;

import com.mymerit.mymerit.domain.models.ProgrammingLanguage;
import lombok.Data;

@Data
public class Badge {

   private ProgrammingLanguage language;
   private String description;
   private Integer tasksCounter = 0;
   private Level level = new Level();




    public Badge(ProgrammingLanguage language){
        this.language = language;
        this.description = "Solve task using " + language;

    }

   public void incrementTasksCounter(){
        tasksCounter++;
        level.updateLevelStatus();
   }



}

