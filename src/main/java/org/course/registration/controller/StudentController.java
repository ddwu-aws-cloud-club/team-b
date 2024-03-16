package org.course.registration.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.course.registration.domain.Student;
import org.course.registration.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PostMapping("/student")
    public String register(@RequestParam("student_id") int student_id, HttpServletRequest request){
        // 세션에 학번 저장
        HttpSession session = request.getSession();
        session.setAttribute("student_id", student_id);

        // 학번 등록
        Student student = new Student();
        student.setId(student_id);

        studentService.register(student);

        return "redirect:/enrolment";
    }
}
