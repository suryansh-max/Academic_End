package com.escanor.acadperfapi.services;

import com.escanor.acadperfapi.models.Report;

import java.util.List;

public interface ReportService {
    Integer createReport(Integer studentId, Integer subjectId, String title, Integer year, Double obtainedMarks, Double maximumMarks);
    void removeReport(Integer reportId);
    Report getReport(Integer reportId);
    List<Report> getStudentReport(Integer studentId);
}
