package org.course.registration.service;

import org.course.registration.entity.Course;
import org.course.registration.repository.CourseRepository;
import org.course.registration.repository.EnrollRepository;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;


class EnrollServiceTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollRepository enrollRepository;

    @Test
    void cancelEnrollment() {
        // given
        int courseId = 1;
        String studentId = "20220101";

        // when
        Course course = courseService.findCourseById(courseId);
        int courseCount = course.getCount();
        enrollRepository.deleteByStudentIdAndCourseId(studentId, courseId);
        course.decreaseCount();
        courseService.saveOrUpdateCourse(course);

        // then
        assert course.getCount() == courseCount - 1;
    }
}