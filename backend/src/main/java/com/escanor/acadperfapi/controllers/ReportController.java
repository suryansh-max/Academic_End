package com.escanor.acadperfapi.controllers;

import com.escanor.acadperfapi.exceptions.ApAuthException;
import com.escanor.acadperfapi.models.Report;
import com.escanor.acadperfapi.services.ReportService;
import com.escanor.acadperfapi.services.UserService;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/reports")
public class ReportController {
    final ReportService reportService;
    final UserService userService;

    public ReportController(ReportService reportService, UserService userService) {
        this.reportService = reportService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createReport(HttpServletRequest request, @RequestBody Map<String, Object> reportMap) {
        Integer userId = (Integer) request.getAttribute("userId");
        if(!userService.getKind(userId).equals("teacher")) throw new ApAuthException("Not authorized!");
        Integer studentId = Integer.parseInt((String) reportMap.get("studentId"));
        Integer subjectId = Integer.parseInt((String) reportMap.get("subjectId"));
        Integer year = Integer.parseInt((String) reportMap.get("year"));
        String title = (String) reportMap.get("title");
        Double obtainedMarks = Double.parseDouble((String) reportMap.get("obtainedMarks"));
        Double maximumMarks = Double.parseDouble((String) reportMap.get("maximumMarks"));
        Integer reportId = reportService.createReport(studentId, subjectId, title, year, obtainedMarks, maximumMarks);
        Map<String, String> map = new HashMap<>();
        map.put("message", "report created");
        map.put("payload", reportId.toString());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<Map<String, String>> createReport(HttpServletRequest request, @PathVariable("reportId") Integer reportId) {
        Integer userId = (Integer) request.getAttribute("userId");
        if(!userService.getKind(userId).equals("teacher")) throw new ApAuthException("Not authorized!");
        reportService.removeReport(reportId);
        Map<String, String> map = new HashMap<>();
        map.put("message", "report removed");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<Map<String, Object>> getReport(@PathVariable("reportId") Integer reportId) {
        Report report = reportService.getReport(reportId);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Single report fetched");
        map.put("payload", report);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
    @GetMapping("/student/{studentId}")
    public ResponseEntity<Map<String, Object>> getStudentReport(@PathVariable("studentId") Integer studentId) {
        List<Report> reports = reportService.getStudentReport(studentId);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Complete report fetched");
        map.put("payload", reports);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
