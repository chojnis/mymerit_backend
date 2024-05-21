package com.mymerit.mymerit.api.payload.response;

import com.mymerit.mymerit.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RankingResponse {
    private User user;
    private Integer ranking;
}
