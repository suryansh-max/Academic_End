package com.escanor.acadperfapi.repositories;

import com.escanor.acadperfapi.exceptions.ApBadRequestException;
import com.escanor.acadperfapi.models.Comment;
import com.escanor.acadperfapi.models.Report;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class CommentRepositoryImpl implements CommentRepository {
    private static final String SQL_CREATE = "INSERT INTO AP_COMMENTS(COMMENT_ID, REPORT_ID, DESCRIPTION) VALUES(NEXTVAL('AP_COMMENTS_SEQ'), ?, ?)";
    private static final String SQL_REMOVE = "DELETE FROM AP_COMMENTS WHERE COMMENT_ID = ?";
    private static final String SQL_FIND_BY_ID = "SELECT C.COMMENT_ID, C.REPORT_ID, C.DESCRIPTION FROM AP_COMMENTS as C, AP_REPORTS as R WHERE C.REPORT_ID = R.REPORT_ID AND R.STUDENT_ID = ?";

    final JdbcTemplate jdbcTemplate;

    public CommentRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer createComment(Integer reportId, String description) {
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, reportId);
                ps.setString(2, description);
                return ps;
            }, keyHolder);
            Integer commentId = (Integer) keyHolder.getKeys().get("comment_id");
            return commentId;
        } catch(Exception e) {
            throw new ApBadRequestException("Invalid Data" + " " + reportId + " " + description);
        }
    }

    @Override
    public void removeComment(Integer commentId) {
        int count = jdbcTemplate.update(SQL_REMOVE, new Object[]{commentId});
        if(count == 0) throw new ApBadRequestException("Invalid Request");
    }

    @Override
    public List<Comment> getComments(Integer studentId) {
        try{
            List<Comment> comments = jdbcTemplate.query(SQL_FIND_BY_ID, new Object[]{studentId}, commentRowMapper);
            return comments;
        } catch(Exception e) {
            throw new ApBadRequestException("Invalid Request");
        }
    }

    private final RowMapper<Comment> commentRowMapper = ((rs, rowNum) -> {
        return new Comment(
                rs.getInt("COMMENT_ID"),
                rs.getInt("REPORT_ID"),
                rs.getString("DESCRIPTION")
        );
    });
}
