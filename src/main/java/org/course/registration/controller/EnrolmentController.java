package org.course.registration.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.course.registration.domain.Course;
import org.course.registration.service.CourseService;
import org.course.registration.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class EnrolmentController {

    private final CourseService courseService;

    @GetMapping("/enrolment")
    public String enrolment(Model model){
        List<Course> courses = courseService.findCourses();
        model.addAttribute("courses", courses);

        return "enrolls";
    }

}
