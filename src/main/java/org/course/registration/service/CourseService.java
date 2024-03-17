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
    public List<Course> findCourses(){
        return courseRepository.findAll();
    }

    // 과목 ID로 과목 조회
    public Course findCourseById(int id){
        return courseRepository.findById(id);
    }

    // CourseService 내에 업데이트 메서드 추가
    @Transactional
    public Course updateCourse(Course course) {
        return courseRepository.save(course); // 변경된 course 엔티티를 저장
    }
}
