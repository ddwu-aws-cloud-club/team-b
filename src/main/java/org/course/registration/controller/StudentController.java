package org.course.registration.controller;

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
    public String register(@RequestParam("student_id") int student_id){
        Student student = new Student();
        student.setId(student_id);

        studentService.register(student);

        return "redirect:enrolls";
    }
}
