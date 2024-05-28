package com.mymerit.mymerit.domain.service;

import com.mymerit.mymerit.domain.entity.JobOffer;
import com.mymerit.mymerit.domain.entity.Task;
import com.mymerit.mymerit.domain.entity.TemplateFile;
import com.mymerit.mymerit.infrastructure.repository.JobOfferRepository;
import com.mymerit.mymerit.infrastructure.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    private final JobOfferRepository jobOfferRepository;



    TaskService(JobOfferRepository jobOfferRepository){
        this.jobOfferRepository = jobOfferRepository;
    }



}
