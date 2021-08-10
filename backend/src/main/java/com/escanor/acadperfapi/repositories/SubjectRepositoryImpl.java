package com.escanor.acadperfapi.repositories;

import com.escanor.acadperfapi.exceptions.ApAuthException;
import com.escanor.acadperfapi.exceptions.ApBadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Repository
public class SubjectRepositoryImpl implements SubjectRepository {
    private static final String SQL_ADD = "INSERT INTO AP_SUBJECTS(SUBJECT_ID, NAME) VALUES(NEXTVAL('AP_SUBJECTS_SEQ'), ?)";
    private static final String SQL_REMOVE = "DELETE FROM AP_SUBJECTS WHERE SUBJECT_ID = ?";
    private static final String SQL_UPDATE = "UPDATE AP_SUBJECTS SET NAME = ? WHERE SUBJECT_ID = ?";
    private static final String SQL_ASSIGN = "INSERT INTO AP_TEACHER_SUBJECT(TEACHER_ID, SUBJECT_ID) VALUES(?, ?)";
    private static final String SQL_GET_STUDENTS_INFO = "SELECT SUBJECT_ID, NAME FROM AP_SUBJECTS";
    final JdbcTemplate jdbcTemplate;

    public SubjectRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer addSubject(String name) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_ADD, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            return ps;
        }, keyHolder);
        return (Integer) keyHolder.getKeys().get("subject_id");
    }

    @Override
    public void removeSubject(Integer subjectId) throws ApBadRequestException {
        int count = jdbcTemplate.update(SQL_REMOVE, new Object[]{subjectId});
        if(count == 0) throw new ApBadRequestException("Subject not found");
    }

    @Override
    public void updateSubject(Integer subjectId, String name) throws ApBadRequestException {
        try {
            jdbcTemplate.update(SQL_UPDATE, new Object[]{name, subjectId});
        }
        catch(Exception e) {
            throw new ApBadRequestException("Invalid Request");
        }
    }

    @Override
    public void assignSubject(Integer subjectId, Integer teacherId) throws ApBadRequestException {
        try {
            jdbcTemplate.update(SQL_ASSIGN, new Object[]{teacherId, subjectId});
        }
        catch(Exception e) {
            throw new ApBadRequestException("Invalid Request");
        }
    }

    @Override
    public List<Map<String, String>> getSubjectsInfo() throws ApBadRequestException {
        try {
            return jdbcTemplate.query(SQL_GET_STUDENTS_INFO, new Object[]{}, studentsRowMapper);
        }
        catch(EmptyResultDataAccessException e) {
            throw new ApAuthException("Bad Request");
        }
    }

    private final RowMapper<Map<String, String>> studentsRowMapper = ((rs, rowNum) -> {
        Map<String, String> hm = new HashMap<>();
        
        hm.put("subjectId", Integer.toString(rs.getInt("SUBJECT_ID")));
        hm.put("name", rs.getString("NAME"));
        return hm;
    });

}
