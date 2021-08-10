package com.escanor.acadperfapi.services;

import java.util.List;
import java.util.Map;

public interface SubjectService {
    Integer addSubject(String name);
    void removeSubject(Integer subjectId);
    void updateSubject(Integer subjectId, String name);
    void assignSubject(Integer subjectId, Integer teacherId);
    List<Map<String, String>> getSubjectsInfo();
}
