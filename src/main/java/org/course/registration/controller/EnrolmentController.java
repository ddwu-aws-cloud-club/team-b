package org.course.registration.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.course.registration.domain.Course;
import org.course.registration.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class EnrolmentController {

    private final CourseService courseService;

    @GetMapping("/enrolment")
    public String enrolment(Model model, HttpServletRequest request){
        // 세션에서 아이디 가져오기
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("student_id") == null) {
            return "redirect:/";
        }

        List<Course> courses = courseService.findCourses();
        model.addAttribute("courses", courses);

        return "enrolls";
    }

}
