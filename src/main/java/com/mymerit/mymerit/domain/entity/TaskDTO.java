package com.mymerit.mymerit.domain.entity;


import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


public record TaskDTO(
        String topic,
        String description,
        LocalDateTime releaseDate,
        LocalDateTime expiryDate,
        Integer reward,
        User company,
        Integer solutionIndex,
        String[] allowedTechnologies
) {
}
