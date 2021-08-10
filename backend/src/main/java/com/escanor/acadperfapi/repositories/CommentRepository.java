package com.escanor.acadperfapi.repositories;
import com.escanor.acadperfapi.models.Comment;

import java.util.List;

public interface CommentRepository {
    Integer createComment(Integer reportId, String description);
    void removeComment(Integer commentId);
    List<Comment> getComments(Integer reportId);
}
