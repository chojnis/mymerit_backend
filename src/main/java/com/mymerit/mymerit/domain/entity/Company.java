package com.mymerit.mymerit.domain.entity;

import lombok.Data;

import java.util.List;

@Data
public class Company {

    public  String id;
    public  String name;
    public  String location;
    public  String description;
    public  List<User> employees;
    private Integer creditsAmount;
    public  List<String> technologies;
    public List<Task> tasks;

}
