package com.mymerit.mymerit.domain.service;

import com.mymerit.mymerit.domain.entity.Solution;
import com.mymerit.mymerit.domain.entity.Task;
import com.mymerit.mymerit.infrastructure.repository.SolutionRepository;
import com.mymerit.mymerit.infrastructure.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SolutionService {

    TaskRepository taskRepository;
    SolutionRepository solutionRepository;


    SolutionService(SolutionRepository solutionRepository, TaskRepository taskRepository){
        this.solutionRepository = solutionRepository;
        this.taskRepository = taskRepository;
    }

    public List<Solution> getSolutionsForUser(String userId){
        return this.solutionRepository.findAllByUserId(userId);
    }

    public Page<Solution> getSolutionsByTaskId(String taskId, Pageable pageable){
        return solutionRepository.findAllByTaskId(taskId,pageable);
    }

}
