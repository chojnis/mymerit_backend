package com.mymerit.mymerit.domain.entity;



import com.mymerit.mymerit.domain.models.RankingPair;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class Ranking {

     public List<RankingPair> rankingHistory = new ArrayList<>();

     public void addRanking(Integer points ){
         rankingHistory.add(new RankingPair(points));
     }

    public Integer getWeeklyRanking() {
        LocalDate today = LocalDate.now();
        LocalDate lastSunday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

        return calculateRanking(lastSunday);
    }

    public Integer getMonthlyRanking() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);

        return calculateRanking(firstDayOfMonth);
    }

    public Integer getYearlyRanking() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfYear = today.withDayOfYear(1);

        return calculateRanking(firstDayOfYear);
    }

    private Integer calculateRanking(LocalDate startDate) {
        return rankingHistory.stream()
                .filter(pair -> pair.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isAfter(startDate))
                .mapToInt(RankingPair::getPoints)
                .sum();
    }

 public Integer getAllTimeRanking(){
     Integer rankingSum = 0;
     for(RankingPair pair : rankingHistory){
         rankingSum += pair.getPoints();

     }

     return rankingSum;
 }


}

