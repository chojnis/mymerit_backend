package com.mymerit.mymerit.api.payload.response;

import com.mymerit.mymerit.domain.entity.Reward;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class RewardHistoryResponse {
    private Reward reward;
    private LocalDateTime datePurchase;
}