package com.escanor.acadperfapi.controllers;

import com.escanor.acadperfapi.exceptions.ApAuthException;
import com.escanor.acadperfapi.exceptions.ApBadRequestException;
import com.escanor.acadperfapi.services.SubjectService;
import com.escanor.acadperfapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/subjects")
public class SubjectController {
    final SubjectService subjectService;
    final UserService userService;

    public SubjectController(SubjectService subjectService, UserService userService) {
        this.subjectService = subjectService;
        this.userService = userService;
    }


    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> addSubject(HttpServletRequest request, @RequestBody Map<String, Object> subjectMap) {
        Integer userId = (Integer) request.getAttribute("userId");
        if(!userService.getKind(userId).equals("admin")) throw new ApAuthException("Not authorized!");
        String name = (String) subjectMap.get("name");
        Integer subjectId = subjectService.addSubject(name);
        Map<String, String> map = new HashMap<>();
        map.put("message", "subject added");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/{subjectId}")
    public ResponseEntity<Map<String, String>> removeSubject(HttpServletRequest request, @PathVariable("subjectId") Integer subjectId) {
        Integer userId = (Integer) request.getAttribute("userId");
        if(!userService.getKind(userId).equals("admin")) throw new ApAuthException("Not authorized!");
        subjectService.removeSubject(subjectId);
        Map<String, String> map = new HashMap<>();
        map.put("message", "subject removed");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PutMapping("/{subjectId}")
    public ResponseEntity<Map<String, String>> updateSubject(HttpServletRequest request, @PathVariable("subjectId") Integer subjectId, @RequestBody Map<String, Object> subjectMap) {
        Integer userId = (Integer) request.getAttribute("userId");
        if(!userService.getKind(userId).equals("admin")) throw new ApAuthException("Not authorized!");
        String name = (String) subjectMap.get("name");
        subjectService.updateSubject(subjectId, name);
        Map<String, String> map = new HashMap<>();
        map.put("message", "subject updated");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/assign")
    public ResponseEntity<Map<String, String>> assignSubject(HttpServletRequest request, @RequestBody Map<String, Object> subjectMap) {
        Integer userId = (Integer) request.getAttribute("userId");
        if(!userService.getKind(userId).equals("admin")) throw new ApAuthException("Not authorized!");
        Integer subjectId = Integer.parseInt((String) subjectMap.get("subjectId"));
        Integer teacherId = Integer.parseInt((String) subjectMap.get("teacherId"));

        System.out.println(subjectId + " " + teacherId);
        subjectService.assignSubject(subjectId, teacherId);
        Map<String, String> map = new HashMap<>();
        map.put("message", "subject assigned");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> viewSubjectInfo(HttpServletRequest request, @RequestBody(required = false) Map<String, Object> userMap) {
        Integer userId = (Integer) request.getAttribute("userId");
        if(!userService.getKind(userId).equals("admin")) throw new ApAuthException("Not authorized!");
        List<Map<String, String>> subjects = subjectService.getSubjectsInfo();
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Info fetched successfully!");
        map.put("payload", subjects);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
