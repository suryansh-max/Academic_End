package com.escanor.acadperfapi.controllers;

import com.escanor.acadperfapi.exceptions.ApAuthException;
import com.escanor.acadperfapi.models.Comment;
import com.escanor.acadperfapi.services.CommentService;
import com.escanor.acadperfapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/comments")
public class CommentController {
    final CommentService commentService;
    final UserService userService;

    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }


    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createComment(HttpServletRequest request, @RequestBody Map<String, Object> commentMap) {
        Integer userId = (Integer) request.getAttribute("userId");
        if(!userService.getKind(userId).equals("teacher")) throw new ApAuthException("Not authorized!");
        Integer reportId = Integer.parseInt((String) commentMap.get("reportId"));
        String description = (String) commentMap.get("description");
        Integer commentId = commentService.createComment(reportId, description);
        Map<String, String> map = new HashMap<>();
        map.put("message", "comment created");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Map<String, String>> deleteComment(HttpServletRequest request, @PathVariable("commentId") Integer commentId) {
        Integer userId = (Integer) request.getAttribute("userId");
        if(!userService.getKind(userId).equals("teacher")) throw new ApAuthException("Not authorized!");
        commentService.removeComment(commentId);
        Map<String, String> map = new HashMap<>();
        map.put("message", "report removed");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Map<String, Object>> getComments(@PathVariable("studentId") Integer studentId) {
        List<Comment> comment = commentService.getComments(studentId);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Comment fetched");
        map.put("payload", comment);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
