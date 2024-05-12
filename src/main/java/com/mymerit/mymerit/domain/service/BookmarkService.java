package com.mymerit.mymerit.domain.service;

import com.mymerit.mymerit.domain.entity.Bookmark;
import com.mymerit.mymerit.domain.entity.JobOffer;
import com.mymerit.mymerit.domain.entity.User;
import com.mymerit.mymerit.infrastructure.repository.BookmarkRepository;
import com.mymerit.mymerit.infrastructure.repository.JobOfferRepository;
import com.mymerit.mymerit.infrastructure.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final JobOfferRepository jobOfferRepository;

    BookmarkService(BookmarkRepository bookmarkRepository, UserRepository userRepository, JobOfferRepository jobOfferRepository){
        this.bookmarkRepository = bookmarkRepository;
        this.userRepository = userRepository;
        this.jobOfferRepository = jobOfferRepository;
    }

    public Bookmark addBookmark(String userId, String jobId){
        if(bookmarkRepository.findByUserIdAndJobOfferId(userId, jobId).isPresent()){
            throw new RuntimeException("This job offer is already bookmarked");
        }
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found " + userId));
        JobOffer jobOffer = jobOfferRepository.findById(jobId).orElseThrow(()->new RuntimeException("Job not found " + userId));
        return this.bookmarkRepository.save(new Bookmark(user, jobOffer));
    }


    public List<Bookmark> getBookmarksForUser(String id) {
        return this.bookmarkRepository.findByUserId(id);
    }

    public void removeBookmark(String userId, String jobId) {
        if(bookmarkRepository.findByUserIdAndJobOfferId(userId, jobId).isPresent()){
             this.bookmarkRepository.delete(bookmarkRepository.findByUserIdAndJobOfferId(userId, jobId).get());
        }else{
            throw new RuntimeException("This job offer is not bookmarked");
        }
    }
}
