package com.escanor.acadperfapi.repositories;

import com.escanor.acadperfapi.exceptions.ApBadRequestException;

import java.util.List;
import java.util.Map;

public interface SubjectRepository {
    Integer addSubject(String name);
    void removeSubject(Integer subjectId) throws ApBadRequestException;
    void updateSubject(Integer subjectId, String name) throws ApBadRequestException;
    void assignSubject(Integer subjectId, Integer teacherId) throws ApBadRequestException;
    List<Map<String, String>> getSubjectsInfo() throws ApBadRequestException;
}
