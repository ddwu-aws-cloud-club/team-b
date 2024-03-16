package org.course.registration.controller;

import lombok.RequiredArgsConstructor;
import org.course.registration.domain.Course;
import org.course.registration.domain.Enroll;
import org.course.registration.domain.Student;
import org.course.registration.service.EnrollService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/enrollment")
public class EnrollController {

    private final EnrollService enrollService;

    // 신청 조회 기능
    @GetMapping("/enrolls")
    public List<Enroll> getAllEnrolls() {
        return enrollService.getAllEnrolls();
    }

    // 과목 신청 기능
    @PostMapping("/course/enroll")
    public void enrollCourse(@RequestParam int studentId, @RequestParam int courseId) {
        // 예시로 간단하게 학번과 과목 ID를 받아서 과목 신청 기능을 구현했습니다.
        // 실제로는 요청에 맞게 파라미터를 조정하고 예외 처리 등을 추가해야 합니다.
        Student student = new Student();
        student.setId(studentId);
        Course course = new Course();
        course.setId(courseId);
        enrollService.enrollCourse(student, course);
    }




}
