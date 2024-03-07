package com.mymerit.mymerit.domain.entity;


import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document("tasks")
public record TaskDTO(
        String id,
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
