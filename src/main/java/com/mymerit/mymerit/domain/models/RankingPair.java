package com.mymerit.mymerit.domain.models;

import lombok.Data;

import java.util.Date;
@Data
public class RankingPair {
    public Integer points;
    public Date date;

    public RankingPair(Integer points){
        this.date = new Date();
        this.points = points;
    }


}
