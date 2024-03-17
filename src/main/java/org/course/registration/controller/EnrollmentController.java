package org.course.registration.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.course.registration.domain.Course;
import org.course.registration.exception.NotEnoughException;
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
        if (session == null || session.getAttribute("student_id") == null) {
            return "redirect:/";
        }

        // 사용자 ID 가져오기
        int studentId = (int) session.getAttribute("student_id");

        List<Course> courses = courseService.findCourses();
        List<Course> enrolledCourses = enrollService.findEnrollmentsByStudentId(studentId);

        model.addAttribute("courses", courses);
        model.addAttribute("enrolledCourses", enrolledCourses);

        return "enrolls";
    }


    // 수강신청
    @PostMapping("/enrollment/course/enroll")
    public String enrollCourse(@RequestParam("course_id") int courseId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("student_id") == null) {
            return "redirect:/";
        }

        int studentId = (int) session.getAttribute("student_id");

        try {
            // 과목 수강 신청 진행
            enrollService.enrollCourse(studentId, courseId);
            return "redirect:/enrollment?success=true";
        } catch (NotEnoughException e) {
            // 정원 초과
            return "redirect:/enrollment?overError=true";

        } catch (IllegalStateException e) {
            // 이미 수강신청 한 과목
            return "redirect:/enrollment?existError=true";
        } catch (Exception e) {
            // 기타 오류
            return "redirect:/enrollment?error=true";
        }
    }

    // 수강 신청 취소
    @PostMapping("/enrollment/course/cancel")
    public String cancelEnrollment(@RequestParam("course_id") int courseId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("student_id") == null) {
            return "redirect:/";
        }

        int studentId = (int) session.getAttribute("student_id");
        enrollService.cancelEnrollment(studentId, courseId);
        return "redirect:/enrollment?cancelSuccess=true";
    }

}
