package org.course.registration.service;

import lombok.RequiredArgsConstructor;
import org.course.registration.domain.Course;
import org.course.registration.domain.Enroll;
import org.course.registration.domain.Student;
import org.course.registration.repository.EnrollRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollService {

    private final EnrollRepository enrollRepository;

    // 과목 신청 기능
    @Transactional
    public void enrollCourse(Student student, Course course) {
        Enroll enroll = new Enroll();
        enroll.setStudent(student);
        enroll.setCourse(course);
        enrollRepository.save(enroll);
    }

    // 신청 조회 기능
    public List<Enroll> getAllEnrolls() {
        return enrollRepository.findAll();
    }
}
