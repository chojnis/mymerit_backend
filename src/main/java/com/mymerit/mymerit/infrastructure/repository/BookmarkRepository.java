package com.mymerit.mymerit.infrastructure.repository;

import com.mymerit.mymerit.domain.entity.Bookmark;
import com.mymerit.mymerit.domain.entity.JobOfferHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends MongoRepository<Bookmark, String> {
    List<Bookmark> findByUserId(String userId);
    Optional<Bookmark> findByUserIdAndJobOfferId(String userId, String jobId);
}
