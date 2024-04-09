package org.course.registration.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;

import org.course.registration.entity.Course;
import org.course.registration.service.CourseService;
import org.course.registration.service.EnrollService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class EnrollmentController {

    private final CourseService courseService;
    private final EnrollService enrollService;

    @GetMapping("/enrollment")
    public String enrolment(Model model, HttpServletRequest request){
        // 세션에서 아이디 가져오기
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("studentId") == null) {
            return "redirect:/";
        }

        // 사용자 ID 가져오기
        String studentId = (String) session.getAttribute("studentId");

        List<Course> courses = courseService.findCourses();
        List<Course> enrolledCourses = enrollService.findEnrollmentsByStudentId(studentId);

        model.addAttribute("courses", courses);
        model.addAttribute("enrolledCourses", enrolledCourses);

        return "enrolls";
    }

    // 수강신청
    @PostMapping("/enrollment/course/enroll")
    public String enrollCourse(@RequestParam("courseId") int courseId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("studentId") == null) {
            return "redirect:/";
        }

        String studentId = (String) session.getAttribute("studentId");
        // 과목 수강 신청 진행
        enrollService.enrollCourse(studentId, courseId);
        return "redirect:/enrollment?success=true";
    }

    // 수강 신청 취소
    @PostMapping("/enrollment/course/cancel")
    public String cancelEnrollment(@RequestParam("courseId") int courseId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("studentId") == null) {
            return "redirect:/";
        }

        String studentId = (String) session.getAttribute("studentId");
        enrollService.cancelEnrollment(studentId, courseId);
        return "redirect:/enrollment?cancelSuccess=true";
    }
}
