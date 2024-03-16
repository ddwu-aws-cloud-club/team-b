package org.course.registration.service;

import lombok.RequiredArgsConstructor;
import org.course.registration.domain.Course;
import org.course.registration.domain.Student;
import org.course.registration.repository.CourseRepository;
import org.course.registration.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    // 과목 전체 조회
    public List<Course> findCourses(){
        return courseRepository.findAll();
    }
}
