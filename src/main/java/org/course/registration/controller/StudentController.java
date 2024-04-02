package org.course.registration.controller;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.course.registration.domain.Student;
import org.course.registration.service.StudentService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/student")
    public String register(
        @RequestParam("studentId") int studentId,
        @RequestParam("phoneNum") String phoneNum,
        HttpServletRequest request
    ) {
        Student student = new Student(studentId, phoneNum);

        // 학생 등록 및 세션 저장
        studentService.CreateSession(student, request);
        studentService.register(student);

        return "redirect:/enrollment";
    }
}
