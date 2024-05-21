package com.mymerit.mymerit.api.payload.response;

import com.mymerit.mymerit.domain.entity.Badge;
import com.mymerit.mymerit.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RankingResponse {
    private String id;
    private int rank;
    private int meritPoints;
    private String username;
    private String profileImageBase64;
    private List<String> languages;
    public RankingResponse(User user, int meritPoints, int rank) {
        this.id = user.getId();
        this.rank = rank;
        this.meritPoints = meritPoints;
        this.username = user.getUsername();
        this.profileImageBase64 = user.getImageBase64();
        this.languages = user.getBadges()
                .stream()
                .sorted(Comparator.comparingInt(Badge::getTasksCounter))
                .limit(2)
                .map(badge -> badge.getLanguage().toString()).toList();
        
    }
}