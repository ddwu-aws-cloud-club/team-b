package org.course.registration.service;

import lombok.RequiredArgsConstructor;

import org.course.registration.domain.Course;
import org.course.registration.repository.CourseRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    // 과목 전체 조회
    public List<Course> findCourses() {
        return courseRepository.findAll();
    }

    // 과목 ID로 과목 조회
    public Course findCourseById(int id) {
        return courseRepository.findById(id);
    }

    // Course 엔티티를 저장 or 업데이트
    public Course saveOrUpdateCourse(Course course) {
        if (course.getId() == 0) {
            courseRepository.persist(course);
        } else { // 업데이트
            course = courseRepository.merge(course);
        }

        return course;
    }
}
