package com.escanor.acadperfapi.models;

import java.sql.Date;

public class Student {
    private Integer studentId;
    private Integer userId;
    private String address;
    private Integer year;
    private Date dob;

    public Student(Integer studentId, Integer userId, String address, Integer year, Date dob) {
        this.studentId = studentId;
        this.userId = userId;
        this.address = address;
        this.year = year;
        this.dob = dob;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }
}
