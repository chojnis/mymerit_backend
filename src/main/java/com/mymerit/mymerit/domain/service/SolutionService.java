package com.mymerit.mymerit.domain.service;

import com.mymerit.mymerit.domain.entity.Solution;
import com.mymerit.mymerit.domain.entity.Task;
import com.mymerit.mymerit.infrastructure.repository.SolutionRepository;
import com.mymerit.mymerit.infrastructure.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

public class SolutionService {

    TaskRepository taskRepository;
    SolutionRepository solutionRepository;


    SolutionService(SolutionRepository solutionRepository, TaskRepository taskRepository){
        this.solutionRepository = solutionRepository;
        this.taskRepository = taskRepository;
    }





}
