package com.escanor.acadperfapi.repositories;

import com.escanor.acadperfapi.exceptions.ApAuthException;
import com.escanor.acadperfapi.exceptions.ApBadRequestException;
import com.escanor.acadperfapi.models.Student;
import com.escanor.acadperfapi.models.Teacher;
import com.escanor.acadperfapi.models.User;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.HashMap;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String SQL_CREATE_USER = "INSERT INTO AP_USERS(USER_ID, FIRST_NAME, LAST_NAME, KIND, USERNAME, PASSWORD) VALUES(NEXTVAL('AP_USERS_SEQ'), ?, ?, ?, ?, ?)";
    private static final String SQL_CREATE_STUDENT = "INSERT INTO AP_STUDENTS(STUDENT_ID, USER_ID, ADDRESS, DOB, YEAR) VALUES(NEXTVAL('AP_STUDENTS_SEQ'), ?, ?, ?, ?)";
    private static final String SQL_CREATE_TEACHER = "INSERT INTO AP_TEACHERS(TEACHER_ID, USER_ID) VALUES(NEXTVAL('AP_TEACHERS_SEQ'), ?)";
    private static final String SQL_COUNT_BY_USERNAME = "SELECT COUNT(*) FROM AP_USERS WHERE USERNAME = ?";
    private static final String SQL_FIND_BY_ID = "SELECT USER_ID, FIRST_NAME, LAST_NAME, KIND, USERNAME, PASSWORD FROM AP_USERS WHERE USER_ID = ?";
    private static final String SQL_FIND_BY_USERNAME = "SELECT USER_ID, FIRST_NAME, LAST_NAME, KIND, USERNAME, PASSWORD FROM AP_USERS WHERE USERNAME = ?";
    private static final String SQL_FIND_STUDENT_BY_ID = "SELECT STUDENT_ID, USER_ID, ADDRESS, DOB, YEAR FROM AP_STUDENTS WHERE USER_ID = ?";
    private static final String SQL_FIND_TEACHER_BY_ID = "SELECT TEACHER_ID, USER_ID FROM AP_TEACHERS WHERE USER_ID = ?";
    private static final String SQL_FIND_SUBJECTS_BY_ID = "SELECT SUBJECT_ID FROM AP_TEACHER_SUBJECT WHERE TEACHER_ID = ?";
    private static final String SQL_FIND_SUBJECTS_BY_TEACHER_ID = "SELECT SUBJECT_ID, NAME FROM AP_SUBJECTS WHERE SUBJECT_ID IN (SELECT SUBJECT_ID FROM AP_TEACHER_SUBJECT WHERE TEACHER_ID = ?)";
    private static final String SQL_GET_TEACHERS_INFO = "SELECT AP_USERS.USER_ID, TEACHER_ID, FIRST_NAME, LAST_NAME FROM AP_USERS, AP_TEACHERS WHERE AP_USERS.USER_ID = AP_TEACHERS.USER_ID";
    final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer createTeacher(String firstName, String lastName, String username, String password) throws ApAuthException {
        try{
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE_USER, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, "teacher");
                ps.setString(4, username);
                ps.setString(5, hashedPassword);
                return ps;
            }, keyHolder);
            Integer userId = (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("USER_ID");
            jdbcTemplate.update(SQL_CREATE_TEACHER, new Object[]{userId});
            return userId;
        } catch(Exception e) {
            throw new ApAuthException("Invalid Details!");
        }
    }

    @Override
    public Integer createStudent(String firstName, String lastName, String username, String password, String dob, String address, String year) throws ApAuthException {
        try{
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE_USER, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, "student");
                ps.setString(4, username);
                ps.setString(5, hashedPassword);
                return ps;
            }, keyHolder);
            Integer userId = (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("USER_ID");
            jdbcTemplate.update(SQL_CREATE_STUDENT, new Object[]{userId, address, java.sql.Date.valueOf(dob), Integer.parseInt(year)});
            return userId;
        } catch(Exception e) {
            throw new ApAuthException("Invalid Details!"+ firstName + " " + lastName + " " + username + " " + password + " " + address + " " + dob + " " + year);
        }
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) throws ApAuthException {
        try{
            User user = jdbcTemplate.queryForObject(SQL_FIND_BY_USERNAME, new Object[]{username}, userRowMapper);
            if(!BCrypt.checkpw(password, user.getPassword())) throw new ApAuthException("Invalid Credentials!");
            return user;
        } catch (EmptyResultDataAccessException e) {
            throw new ApAuthException("Invalid Credentials!");
        }
    }

    @Override
    public Integer getCountByUsername(String username) {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_USERNAME, new Object[]{username}, Integer.class);
    }

    @Override
    public User findByUserId(Integer userId) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{userId}, userRowMapper);
    }

    @Override
    public Student findStudentByUserId(Integer userId) throws ApBadRequestException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_STUDENT_BY_ID, new Object[]{userId}, studentRowMapper);
        }
        catch(EmptyResultDataAccessException e) {
            throw new ApAuthException("Bad Request");
        }
    }

    @Override
    public Teacher findTeacherByUserId(Integer userId) throws ApBadRequestException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_TEACHER_BY_ID, new Object[]{userId}, teacherRowMapper);
        }
        catch(EmptyResultDataAccessException e) {
            throw new ApAuthException("Bad Request");
        }
    }

    @Override
    public List<Integer> findTeacherSubjectsByTeacherId(Integer teacherId) throws ApBadRequestException {
        try {
            return jdbcTemplate.query(SQL_FIND_SUBJECTS_BY_ID, new Object[]{teacherId}, subjectRowMapper);
        }
        catch(EmptyResultDataAccessException e) {
            throw new ApAuthException("Bad Request");
        }
    }

    @Override
    public List<Map<String, String>> findTeachersSubjectsByTeacherId(Integer teacherId) throws ApBadRequestException {
        try {
            return jdbcTemplate.query(SQL_FIND_SUBJECTS_BY_TEACHER_ID, new Object[]{teacherId}, subjectsRowMapper);
        }
        catch(EmptyResultDataAccessException e) {
            throw new ApAuthException("Bad Request");
        }
    }

    @Override
    public List<Map<String, String>> getTeachersInfo() throws ApBadRequestException {
        try {
            return jdbcTemplate.query(SQL_GET_TEACHERS_INFO, new Object[]{}, teachersRowMapper);
        }
        catch(EmptyResultDataAccessException e) {
            throw new ApAuthException("Bad Request");
        }
    }

    private final RowMapper<User> userRowMapper = ((rs, rowNum) -> {
        return new User(
                rs.getInt("USER_ID"),
                rs.getString("FIRST_NAME"),
                rs.getString("LAST_NAME"),
                rs.getString("KIND"),
                rs.getString("USERNAME"),
                rs.getString("PASSWORD")
        );
    });

private final RowMapper<Map<String, String>> teachersRowMapper = ((rs, rowNum) -> {
        Map<String, String> hm = new HashMap<>();
        
        hm.put("userId", Integer.toString(rs.getInt("USER_ID")));
        hm.put("name", rs.getString("FIRST_NAME") + " " + rs.getString("LAST_NAME"));
        hm.put("teacherId", Integer.toString(rs.getInt("TEACHER_ID")));
        return hm;
    });

    private final RowMapper<Student> studentRowMapper = ((rs, rowNum) -> {
        return new Student(
                rs.getInt("STUDENT_ID"),
                rs.getInt("USER_ID"),
                rs.getString("ADDRESS"),
                rs.getInt("YEAR"),
                rs.getDate("DOB")
        );
    });

    private final RowMapper<Teacher> teacherRowMapper = ((rs, rowNum) -> {
        return new Teacher(
                rs.getInt("TEACHER_ID"),
                rs.getInt("USER_ID")
        );
    });
    private final RowMapper<Integer> subjectRowMapper = ((rs, rowNum) -> {
           return rs.getInt("SUBJECT_ID");
    });
    private final RowMapper<Map<String, String>> subjectsRowMapper = ((rs, rowNum) -> {
           Map<String, String> hm = new HashMap<>();
           hm.put("subjectId", Integer.toString(rs.getInt("SUBJECT_ID")));
           hm.put("name", rs.getString("NAME"));
           return hm;
    });
}
