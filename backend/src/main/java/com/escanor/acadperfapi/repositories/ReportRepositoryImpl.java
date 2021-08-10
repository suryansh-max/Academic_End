package com.escanor.acadperfapi.repositories;

import com.escanor.acadperfapi.exceptions.ApBadRequestException;
import com.escanor.acadperfapi.models.Report;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.security.Key;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class ReportRepositoryImpl implements ReportRepository {
    private static final String SQL_CREATE = "INSERT INTO AP_REPORTS(REPORT_ID, STUDENT_ID, SUBJECT_ID, TITLE, YEAR, OBTAINED_MARKS, MAXIMUM_MARKS) VALUES(NEXTVAL('AP_REPORTS_SEQ'), ?, ?, ?, ?, ?, ?)";
    private static final String SQL_REMOVE = "DELETE FROM AP_REPORTS WHERE REPORT_ID = ?";
    private static final String SQL_FIND_BY_ID = "SELECT REPORT_ID, SUBJECT_ID, STUDENT_ID, TITLE, YEAR, OBTAINED_MARKS, MAXIMUM_MARKS FROM AP_REPORTS WHERE REPORT_ID = ?";
    private static final String SQL_FIND_REPORTS_BY_ID = "SELECT REPORT_ID, SUBJECT_ID, STUDENT_ID, TITLE, YEAR, OBTAINED_MARKS, MAXIMUM_MARKS FROM AP_REPORTS WHERE STUDENT_ID = ?";
    final JdbcTemplate jdbcTemplate;

    public ReportRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer createReport(Integer studentId, Integer subjectId, String title, Integer year, Double obtainedMarks, Double maximumMarks) {
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, studentId);
                ps.setInt(2, subjectId);
                ps.setString(3, title);
                ps.setInt(4, year);
                ps.setDouble(5, obtainedMarks);
                ps.setDouble(6, maximumMarks);
                return ps;
            }, keyHolder);
            Integer reportId = (Integer) keyHolder.getKeys().get("report_id");
            return reportId;
        } catch(Exception e) {
            throw new ApBadRequestException("Invalid Data" + subjectId + " " + studentId + " " + title + " " + year + " " + obtainedMarks + " " + maximumMarks);
        }
    }

    @Override
    public void removeReport(Integer reportId) {
        int count = jdbcTemplate.update(SQL_REMOVE, new Object[]{reportId});
        if(count == 0) throw new ApBadRequestException("Invalid Request");
    }

    @Override
    public Report getReport(Integer reportId) {
        try{
            Report report = jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{reportId}, reportRowMapper);
            return report;
        } catch(Exception e) {
            throw new ApBadRequestException("Invalid Request");
        }
    }

    @Override
    public List<Report> getStudentReport(Integer studentId) {
        try{
            List<Report> reports = jdbcTemplate.query(SQL_FIND_REPORTS_BY_ID, new Object[]{studentId}, reportRowMapper);
            return reports;
        } catch(Exception e) {
            throw new ApBadRequestException("Invalid Request");
        }
    }

    private final RowMapper<Report> reportRowMapper = ((rs, rowNum) -> {
        return new Report(
                rs.getInt("REPORT_ID"),
                rs.getInt("STUDENT_ID"),
                rs.getInt("SUBJECT_ID"),
                rs.getInt("YEAR"),
                rs.getString("TITLE"),
                rs.getDouble("OBTAINED_MARKS"),
                rs.getDouble("MAXIMUM_MARKS")
        );
    });
}
