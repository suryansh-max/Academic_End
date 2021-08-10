package com.escanor.acadperfapi.controllers;

import com.escanor.acadperfapi.Constants;
import com.escanor.acadperfapi.exceptions.ApAuthException;
import com.escanor.acadperfapi.models.Student;
import com.escanor.acadperfapi.models.Teacher;
import com.escanor.acadperfapi.models.User;
import com.escanor.acadperfapi.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createUser(HttpServletRequest request, @RequestBody Map<String, Object> userMap) {
        Integer userId = (Integer) request.getAttribute("userId");
        if(!userService.getKind(userId).equals("admin")) throw new ApAuthException("Not authorized!");
        String firstName = (String) userMap.get("firstName");
        String lastName = (String) userMap.get("lastName");
        String kind = (String) userMap.get("kind");
        String username = (String) userMap.get("username");
        String password = (String) userMap.get("password");
        Map<String, String> data = new HashMap<>();
        if(kind.equals("student")) {
            data.put("address", (String) userMap.get("address"));
            data.put("dob", (String) userMap.get("dob"));
            data.put("year", (String) userMap.get("year"));
        }
        User user = userService.createUser(firstName, lastName, username, password, kind, data);
        return new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, Object> userMap) {
        String username = (String) userMap.get("username");
        String password = (String) userMap.get("password");
        User user = userService.loginUser(username, password);
        return new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK);
    }

    @GetMapping("/info/student")
    public ResponseEntity<Map<String, Object>> viewStudentInfo(HttpServletRequest request, @RequestBody(required = false) Map<String, Object> userMap) {

        Integer userId = (Integer) request.getAttribute("userId");
        Student student = userService.getStudentInfo(userId);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Info fetched successfully!");
        map.put("payload", student);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/info/teacher")
    public ResponseEntity<Map<String, Object>> viewTeacherInfo(HttpServletRequest request, @RequestBody(required = false) Map<String, Object> userMap) {
        Integer userId = (Integer) request.getAttribute("userId");
        Teacher teacher = userService.getTeacherInfo(userId);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Info fetched successfully!");
        map.put("payload", teacher);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/info/teacher-subject")
    public ResponseEntity<Map<String, Object>> viewTeacherSubjectInfo(HttpServletRequest request, @RequestBody(required = false) Map<String, Object> userMap) {
        Integer userId =  (Integer) request.getAttribute("userId");
        Integer teacherId = (Integer) userService.findTeacherByUserId(userId);
        List<Integer> subjects = userService.getTeacherSubjectInfo(teacherId);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Info fetched successfully!");
        map.put("payload", subjects);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/info/teacher-subject")
    public ResponseEntity<Map<String, Object>>viewTeacherSubjectsInfo(HttpServletRequest request, @RequestBody(required = true) Map<String, Object> dataMap) {
        Integer userId = (Integer) request.getAttribute("userId");
        if(!userService.getKind(userId).equals("admin")) throw new ApAuthException("Not authorized!");
        Integer teacherId = Integer.parseInt((String)dataMap.get("teacherId"));
        List<Map<String, String>> subjects = userService.getTeacherSubjectsInfo(teacherId);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Info fetched successfully!");
        map.put("payload", subjects);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/info/teachers")
    public ResponseEntity<Map<String, Object>> viewTeachersInfo(HttpServletRequest request, @RequestBody(required = false) Map<String, Object> userMap) {
        Integer userId = (Integer) request.getAttribute("userId");
        if(!userService.getKind(userId).equals("admin")) throw new ApAuthException("Not authorized!");
        List<Map<String, String>> teachers = userService.getTeachersInfo();
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Info fetched successfully!");
        map.put("payload", teachers);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    private Map<String, String> generateJWTToken(User user) {
        long timestamp = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + Constants.TOKEN_VALIDITY))
                .claim("userId", user.getUserId())
                .claim("username", user.getUsername())
                .claim("kind", user.getKind())
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .compact();
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("kind", user.getKind());
        map.put("username", user.getUsername());
        map.put("firstName", user.getFirstName());
        map.put("lastName",user.getLastName());
        return map;
    }
}
