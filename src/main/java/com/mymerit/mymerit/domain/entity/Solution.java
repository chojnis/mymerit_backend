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
    public Task taskId;
    public User user;
    public List<SolutionFile> files;


    public Solution(Task taskId, User user, List<SolutionFile> files) {
        this.taskId = taskId;
        this.user = user;
        this.files = files;
    }
}
