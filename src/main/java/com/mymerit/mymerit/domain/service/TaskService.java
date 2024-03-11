package com.mymerit.mymerit.domain.service;

import com.mymerit.mymerit.domain.entity.Solution;
import com.mymerit.mymerit.domain.entity.Task;
import com.mymerit.mymerit.domain.entity.TaskDTO;
import com.mymerit.mymerit.infrastructure.repository.SolutionRepository;
import com.mymerit.mymerit.infrastructure.repository.TaskRepository;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private TaskRepository taskRepository;
    private SolutionRepository solutionRepository;

    TaskService(TaskRepository taskRepository,SolutionRepository solutionRepository){
        this.taskRepository = taskRepository;
        this.solutionRepository = solutionRepository;

    }

    public Optional<Task> findById(String id){
        System.out.println("NIGER");
       return taskRepository.findById(id);

    }

    public Optional<Solution> findSolutionById(String taskId, String solutionId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);

        return taskOptional.flatMap(task -> {
            List<Solution> solutions = task.getSolutions();


            return solutions.stream()
                    .filter(solution -> solution.getTaskId().equals(solutionId))
                    .findFirst();
           }
        );
    }

    public boolean deleteById(String id) {
        Optional<Task> optionalTask = taskRepository.findById(id);

        if (optionalTask.isPresent()) {
            taskRepository.deleteById(id);
            return true;
        }

        return false;
    }

    public List<Solution> getSolutions(String taskId){

        Optional<Task> optionalTask = taskRepository.findById(taskId);

        if(optionalTask.isPresent()){

           Task task = optionalTask.get();
           return task.getSolutions();
        }
       else return null;
    }

    public Page<Task> getTasks(Collection<List<String>> allowedTechnologies,Integer minReward, Integer maxReward , Pageable pageable) {
        return taskRepository.findAllByAllowedTechnologiesInAndRewardBetween(allowedTechnologies,minReward,maxReward,pageable);
    }

    public Task addTask(Task task){
        System.out.println(task);
       return taskRepository.save(task);

    }

    public Page<Task> getTasksByOrderDesc(Collection<@NotEmpty List<String>> allowedTechnologies,
                                          Integer minReward,
                                          Integer maxReward,
                                          Pageable pageable){

        return taskRepository.findAllByAllowedTechnologiesInAndRewardBetweenOrderByRewardDesc(allowedTechnologies,minReward,maxReward,pageable);
    }
    public Page<Task> getTasksByOrderAsc(Collection<@NotEmpty List<String>> allowedTechnologies,
                                          Integer minReward,
                                          Integer maxReward,
                                          Pageable pageable){

        System.out.println(taskRepository.findAllByAllowedTechnologiesInAndRewardBetweenOrderByRewardAsc(allowedTechnologies,minReward,maxReward,pageable));

        return taskRepository.findAllByAllowedTechnologiesInAndRewardBetweenOrderByRewardAsc(allowedTechnologies,minReward,maxReward,pageable);
    }

    public Page<Task> getAll(Pageable pageable){
        return taskRepository.findAll(pageable);
    }


}
