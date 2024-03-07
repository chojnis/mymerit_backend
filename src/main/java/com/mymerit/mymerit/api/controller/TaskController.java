package com.mymerit.mymerit.api.controller;


import com.mymerit.mymerit.domain.entity.Solution;
import com.mymerit.mymerit.domain.entity.Task;

import com.mymerit.mymerit.domain.service.TaskService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/task")
public class TaskController {
    TaskService taskService;
    TaskController(TaskService taskService){
        this.taskService = taskService;


    }

    @GetMapping("/{id}")
    ResponseEntity<Task> getTaskById(@PathVariable String id){

        return taskService.findById(id)
                .map(task -> ResponseEntity.ok().body(task))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("{taskId}/solution/{solutionId}")
    ResponseEntity<Solution> getSolutionById(@PathVariable String taskId, @PathVariable String solutionId){

        return taskService.findSolutionById(taskId,solutionId).map(solution -> ResponseEntity.ok().body(solution))
                .orElse(ResponseEntity.notFound().build());

    }


   /* @GetMapping("/{id}/solutions")
    public ResponseEntity<List<Solution>> getSolutions(@PathVariable String id) {
        List<Solution> solutions = taskService.getSolutions(id).map(solution -> ResponseEntity.ok().body(solution))
                .orElse(ResponseEntity.notFound().build());
        return ResponseEntity.ok(solutions);
    }

*/

    @GetMapping("/{id}/solutions")
    public ResponseEntity<List<Task>> findSolutions(@PathVariable String taskId) {
        List<Task> tasksWithSolutions = taskService.getSolutions(taskId)
                .stream()
                .map(Solution::getTask)
                .collect(Collectors.toList());

        return ResponseEntity.ok(tasksWithSolutions);
    }


    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteTaskById(@PathVariable String id) {
        if (taskService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }




 /*   @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasks(
            @RequestParam(required = false) List<String> languages,
            @RequestParam(required = false) Integer minCredits,
            @RequestParam(required = false) Integer maxCredits,
            @RequestParam(required = false) Integer timeLeft,
            @RequestParam(required = false) String order,
            @RequestParam(defaultValue = "0") int page) {


          // jak to zrobic ?

        Page<Task> taskPage = taskService.getTasks(languages, minCredits, maxCredits, timeLeft, order, PageRequest.of(page, 10));
        List<Task> tasks = taskPage.getContent();

        return ResponseEntity.ok(tasks);
    }

*/
    @PostMapping("/{id}/solution")
    public ResponseEntity<String> addSolutionToTask(@PathVariable String id, @RequestBody Solution solutionX) {




        Solution solution = new Solution(solutionX.getUser(), solutionX.getTask(), solutionX.getContent());

       // jak dodac solucje do specyficznego taska?


        return ResponseEntity.ok("ok");
    }





}