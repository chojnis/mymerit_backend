package com.mymerit.mymerit.domain.entity;

import lombok.Data;

@Data
public class Level {
    private Integer level;
    private Integer modifier ;
    private Integer progress ;
    private Integer threshold ;


    public Level() {
        this.level = 1;
        this.modifier = 3;
        this.progress = 0;
        this.threshold = 3;
    }

    public void updateLevelStatus(){
        progress++;
        if(progress == threshold){
            level++;
            threshold = threshold + modifier;
            modifier = modifier + 3;
        }
    }

}
