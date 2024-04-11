package org.course.registration.service;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.course.registration.entity.Course;
import org.course.registration.entity.Enroll;
import org.course.registration.entity.Student;
import org.course.registration.exception.AlreadyExistException;
import org.course.registration.exception.NotEnoughException;
import org.course.registration.repository.EnrollRepository;

import org.course.registration.repository.LockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class EnrollService {

    private final EnrollRepository enrollRepository;
    private final CourseService courseService;
    private final StudentService studentService;
    private final LockRepository lockRepository;
    private final WaitlistService waitlistService;

    @Transactional
    public void enrollCourse(String studentId, int courseId) {
        // 학생과 과목을 조회
        Student student = studentService.findStudentById(studentId);
        Course course = courseService.findCourseById(courseId);

        // 과목 수강 정원 체크
        if (course.getCount() >= course.getLimited()) {
            throw new NotEnoughException("정원이 다 찬 과목입니다.");
        }

        // 이미 수강 중인 과목인지 체크
        Optional<Enroll> existingEnroll = enrollRepository.findByStudentIdAndCourseId(studentId, courseId);
        if (existingEnroll.isPresent()) {
            throw new AlreadyExistException("이미 수강 중인 과목입니다.");
        }

        // 수강 신청 시도 시 무조건 대기열에 추가
        waitlistService.addToWaitlist(studentId, courseId);

        // 대기열 순번 조회
        Long rank = waitlistService.getWaitlistRank(studentId, courseId);
        log.info("대기열 순번: {}", rank);
        // 과목 수강 인원 증가
        course.increaseCount();
        courseService.saveOrUpdateCourse(course); // 변경된 course 엔티티를 업데이트
    }

        waitlistService.processWaitlist(student, courseId);
    }

    // 수강 취소
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void cancelEnrollment(String studentId, int courseId) {
        lockRepository.getLock(String.valueOf(studentId));
        Course course = courseService.findCourseById(courseId);
        enrollRepository.deleteByStudentIdAndCourseId(studentId, courseId);

        // 수강 인원 감소
        course.decreaseCount();
        courseService.saveOrUpdateCourse(course); // 변경된 course 엔티티를 업데이트

        lockRepository.releaseLock(String.valueOf(studentId));

        // 수강 취소 처리
        enrollRepository.deleteByStudentIdAndCourseId(studentId, courseId);
        log.info("수강 취소 처리됨: 학생 ID = {}, 과목 ID = {}", studentId, courseId);

        // 대기열에서 다음 학생을 자동으로 수강 신청 처리
        waitlistService.processNextInWaitlist(courseId);
    }

    public String checkEnrollmentStatus(String studentId, int courseId) {
        Optional<Enroll> enrollment = enrollRepository.findByStudentIdAndCourseId(studentId, courseId);
        if (enrollment.isPresent()) {
            return "수강 신청됨";
        } else {
            Long rank = waitlistService.getWaitlistRank(studentId, courseId);
            if (rank != null) {
                return "대기열 순번: " + rank;
            } else {
                return "수강 신청 또는 대기열에 없음";
            }
        }
    }


    // 학생 ID로 수강신청한 과목 리스트 조회
    public List<Course> findEnrollmentsByStudentId(String studentId) {
        return enrollRepository.findCoursesByStudentId(studentId);
    }

}