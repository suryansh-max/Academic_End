package com.escanor.acadperfapi.models;

public class Teacher {
    private Integer teacherId;

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Teacher(Integer teacherId, Integer userId) {
        this.teacherId = teacherId;
        this.userId = userId;
    }

    private Integer userId;
}
