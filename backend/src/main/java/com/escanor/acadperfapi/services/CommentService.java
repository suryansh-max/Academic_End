package com.escanor.acadperfapi.services;

import com.escanor.acadperfapi.models.Comment;

import java.util.List;

public interface CommentService {
    Integer createComment(Integer reportId, String description);
    void removeComment(Integer commentId);
    List<Comment> getComments(Integer studentId);
}
