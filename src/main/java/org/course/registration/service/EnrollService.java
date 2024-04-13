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
        // 수강 신청 시도 시 무조건 대기열에 추가
        waitlistService.addToWaitlist(studentId, courseId);

        // 대기열 순번 조회
        Long rank = waitlistService.getWaitlistRank(studentId, courseId);
        log.info("대기열 순번: {}", rank);

        waitlistService.processWaitList(studentId, courseId);
    }



    // 수강 취소
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void cancelEnrollment(String studentId, int courseId) {
        /* Named Lock */
        lockRepository.getLock(String.valueOf(studentId));

        /* 해당 값이 존재하는지 확인 */
        Course course = courseService.findCourseById(courseId);
        Enroll enroll = enrollRepository.findByStudentIdAndCourseId(studentId, courseId).orElseThrow();

        /* 위 과정에서 오류가 없다면 수강 취소를 진행한다. */
        enrollRepository.deleteByStudentIdAndCourseId(studentId, courseId);
        /* 현재 수강 인원 감소 */
        course.decreaseCount();
        courseService.saveOrUpdateCourse(course);

        lockRepository.releaseLock(String.valueOf(studentId));

        log.info("수강 취소가 성공적으로 완료됨: 학생 ID = {}, 과목 ID = {}", studentId, courseId);

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