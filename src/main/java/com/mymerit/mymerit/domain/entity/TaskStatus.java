package com.mymerit.mymerit.domain.entity;

public enum TaskStatus {
    OPEN(3),
    NOT_YET_OPEN(2),
    EXPIRED(1);

    private final int id;

    TaskStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}