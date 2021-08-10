package com.escanor.acadperfapi.services;

import com.escanor.acadperfapi.exceptions.ApAuthException;
import com.escanor.acadperfapi.exceptions.ApBadRequestException;
import com.escanor.acadperfapi.models.Student;
import com.escanor.acadperfapi.models.Teacher;
import com.escanor.acadperfapi.models.User;
import com.escanor.acadperfapi.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User loginUser(String username, String password) throws ApAuthException {
        if(username != null) username = username.toLowerCase();
        return userRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public User createUser(String firstName, String lastName, String username, String password, String kind, Map<String, String> data) throws ApAuthException {
        if(username != null) username = username.toLowerCase();
        Integer count = userRepository.getCountByUsername(username);
        if(count > 0) throw new ApAuthException("Username already exists!");
        Integer userId = null;
        if(kind != null && kind.equals("student")) userId = userRepository.createStudent(firstName, lastName, username, password, (String) data.get("dob"), (String) data.get("address"), (String) data.get("year"));
        else if(kind != null && kind.equals("teacher")) userId = userRepository.createTeacher(firstName, lastName, username, password);
        if(userId == null) throw new ApAuthException("User type not supported!");
        return userRepository.findByUserId(userId);
    }

    @Override
    public Student getStudentInfo(Integer userId) throws ApBadRequestException {
        return userRepository.findStudentByUserId(userId);
    }

    @Override
    public Teacher getTeacherInfo(Integer userId) throws ApBadRequestException {
        return userRepository.findTeacherByUserId(userId);
    }

    @Override
    public List<Integer> getTeacherSubjectInfo(Integer teacherId) throws ApBadRequestException {
        return userRepository.findTeacherSubjectsByTeacherId(teacherId);
    }

    @Override
    public List<Map<String, String>> getTeacherSubjectsInfo(Integer teacherId) throws ApBadRequestException {
        return userRepository.findTeachersSubjectsByTeacherId(teacherId);
    }

    @Override
    public Integer findTeacherByUserId(Integer userId) {
        return userRepository.findTeacherByUserId(userId).getTeacherId();
    }

    @Override
    public List<Map<String, String>> getTeachersInfo() {
        return userRepository.getTeachersInfo();
    }

    @Override
    public String getKind(Integer userId) {
       return userRepository.findByUserId(userId).getKind();
    }
}
