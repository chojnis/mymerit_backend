package com.mymerit.mymerit.domain.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Data
@Getter
@Setter

public class Solution {
    @Id
    public String id;
    public User user;
    public Task task;
    public String content;


    public Solution(User user, Task task, String content){
        this.user = user;
        this.task = task;
        this.content = content;
    }


}
