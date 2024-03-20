package org.course.registration.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.course.registration.domain.Student;
import org.course.registration.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
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

        try{
            studentService.register(student);
        } catch (IllegalStateException e){
            return "redirect:/enrollment";
        }

        return "redirect:/enrollment";
    }
}
