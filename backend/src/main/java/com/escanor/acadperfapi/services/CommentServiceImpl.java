package com.escanor.acadperfapi.services;

import com.escanor.acadperfapi.models.Comment;
import com.escanor.acadperfapi.repositories.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Integer createComment(Integer reportId, String description) {
        return commentRepository.createComment(reportId, description);
    }

    @Override
    public void removeComment(Integer commentId) {
        commentRepository.removeComment(commentId);
    }

    @Override
    public List<Comment> getComments(Integer studentId) {
        return commentRepository.getComments(studentId);
    }
}
