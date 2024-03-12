package com.mymerit.mymerit.domain.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@Getter
@Setter

public class Solution {


    @Id
    public String id;
    public Task task;
    public String taskId;
    public User user;
    public List<SolutionFile> files;


    public Solution(Task task,String taskId, User user, List<SolutionFile> files) {
       this.task = task;
       this.taskId = task.getId();
       this.user = user;
       this.files = files;
    }
}
