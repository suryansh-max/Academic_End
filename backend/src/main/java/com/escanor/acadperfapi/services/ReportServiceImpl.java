package com.escanor.acadperfapi.services;

import com.escanor.acadperfapi.models.Report;
import com.escanor.acadperfapi.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {
    final ReportRepository reportRepository;

    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public Integer createReport(Integer studentId, Integer subjectId, String title, Integer year, Double obtainedMarks, Double maximumMarks) {
        return reportRepository.createReport(studentId, subjectId, title, year, obtainedMarks, maximumMarks);
    }

    @Override
    public void removeReport(Integer reportId) {
        reportRepository.removeReport(reportId);
    }

    @Override
    public Report getReport(Integer reportId) {
        return reportRepository.getReport(reportId);
    }

    @Override
    public List<Report> getStudentReport(Integer studentId) {
        return reportRepository.getStudentReport(studentId);
    }
}
