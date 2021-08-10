package com.escanor.acadperfapi.repositories;

import com.escanor.acadperfapi.exceptions.ApAuthException;
import com.escanor.acadperfapi.exceptions.ApBadRequestException;
import com.escanor.acadperfapi.models.Student;
import com.escanor.acadperfapi.models.Teacher;
import com.escanor.acadperfapi.models.User;

import java.util.List;
import java.util.Map;

public interface UserRepository {
    Integer createTeacher(String firstName, String lastName, String userName, String password) throws ApAuthException;
    Integer createStudent(String firstName, String lastName, String userName, String password, String dob, String address, String year) throws ApAuthException;
    User findByUsernameAndPassword(String username, String password) throws ApAuthException;
    Integer getCountByUsername(String username);
    User findByUserId(Integer userId);
    Student findStudentByUserId(Integer userId) throws ApBadRequestException;
    Teacher findTeacherByUserId(Integer userId) throws ApBadRequestException;
    List<Integer> findTeacherSubjectsByTeacherId(Integer teacherId) throws ApBadRequestException;
    List<Map<String, String>> findTeachersSubjectsByTeacherId(Integer teacherId) throws ApBadRequestException;
    List<Map<String, String>> getTeachersInfo() throws ApBadRequestException;
}
