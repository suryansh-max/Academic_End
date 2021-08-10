package com.escanor.acadperfapi.services;

import com.escanor.acadperfapi.exceptions.ApAuthException;
import com.escanor.acadperfapi.exceptions.ApBadRequestException;
import com.escanor.acadperfapi.models.Student;
import com.escanor.acadperfapi.models.Teacher;
import com.escanor.acadperfapi.models.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    User loginUser(String username, String password) throws ApAuthException;
    User createUser(String firstName, String lastName, String username, String password, String kind, Map<String, String> data) throws ApAuthException;
    Student getStudentInfo(Integer userId) throws ApBadRequestException;
    Teacher getTeacherInfo(Integer userId) throws ApBadRequestException;
    List<Integer> getTeacherSubjectInfo(Integer teacherId) throws ApBadRequestException;
    List<Map<String, String>> getTeacherSubjectsInfo(Integer teacherId) throws ApBadRequestException;
    Integer findTeacherByUserId(Integer userId);
    String getKind(Integer userId);
    List<Map<String, String>> getTeachersInfo();
}
