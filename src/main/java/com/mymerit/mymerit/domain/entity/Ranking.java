package com.mymerit.mymerit.domain.entity;



import com.mymerit.mymerit.domain.models.RankingPair;
import lombok.Data;
import lombok.NoArgsConstructor;

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
        Integer sum = 0;


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());


        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        int daysToLastSunday = (dayOfWeek == Calendar.SUNDAY) ? 0 : dayOfWeek;

        calendar.add(Calendar.DAY_OF_YEAR, -daysToLastSunday);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);


        Date lastSunday = calendar.getTime();


        for (RankingPair pair : rankingHistory) {

            if (pair.getDate().after(lastSunday)) {
                sum += pair.getPoints();
                System.out.println(pair.getPoints());
            }
        }
        return sum;
    }

 public Integer getRanking(){
     Integer rankingSum = 0;
     for(RankingPair pair : rankingHistory){
         rankingSum += pair.getPoints();

     }

     return rankingSum;
 }


}

