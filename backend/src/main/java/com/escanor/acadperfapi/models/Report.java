package com.escanor.acadperfapi.models;

public class Report {
    private Integer reportId;
    private Integer studentId;
    private Integer subjectId;
    private Integer year;
    private String title;
    private Double obtainedMarks;
    private Double maximumMarks;

    public Report(Integer reportId, Integer studentId, Integer subjectId, Integer year, String title, Double obtainedMarks, Double maximumMarks) {
        this.reportId = reportId;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.year = year;
        this.title = title;
        this.obtainedMarks = obtainedMarks;
        this.maximumMarks = maximumMarks;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getObtainedMarks() {
        return obtainedMarks;
    }

    public void setObtainedMarks(Double obtainedMarks) {
        this.obtainedMarks = obtainedMarks;
    }

    public Double getMaximumMarks() {
        return maximumMarks;
    }

    public void setMaximumMarks(Double maximumMarks) {
        this.maximumMarks = maximumMarks;
    }
}
