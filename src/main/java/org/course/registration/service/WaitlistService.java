package org.course.registration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.course.registration.entity.Course;
import org.course.registration.entity.Enroll;
import org.course.registration.exception.AlreadyExistException;
import org.course.registration.repository.EnrollRepository;
import org.course.registration.entity.Student;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitlistService {

    private final EnrollRepository enrollRepository;
    private final CourseService courseService;
    private final RedisTemplate<String, Object> redisTemplate;
    private EnrollService enrollService;

    // 대기열에 학생 추가
    public void addToWaitlist(String studentId, int courseId) {
        String key = "waitlist:course:" + courseId;
        double score = System.currentTimeMillis();
        log.info("----- 키 등록 확인 : " + key);

        redisTemplate.opsForZSet().add(key, studentId, (int) score);
        log.info("대기열에 추가 - {} ({}초)", studentId, score);
    }

    public Long getWaitlistRank(String studentId, int courseId) {
        String key = "waitlist:course:" + courseId;
        Long rank = redisTemplate.opsForZSet().rank(key, studentId);
        return rank != null ? rank + 1 : null;

    }

    // 대기열에서 학생 처리
    public void processWaitlist(Student student, int courseId) {

        Course course = courseService.findCourseById(courseId);
        final int limit = course.getLimited(); // 과목별 정원

        log.info("과목 {} ID = {}의 수강 신청 대기열 처리 시작", course, courseId);
        log.info("limit : " + limit);

        // 과목의 현재 수강 인원 조회
        int currentCount = course.getCount();
        log.info("현재 수강 인원: {}", currentCount);

        while (currentCount < limit) {
            String studentId = getNextStudentIdInWaitlist(courseId);
            if (studentId == null) {
                log.info("대기열에 더 이상 학생이 없습니다.");
                break; // 종료
            }

            // 이미 수강 중인지 확인
            Optional<Enroll> existingEnroll = enrollRepository.findByStudentIdAndCourseId(studentId, courseId);
            if (existingEnroll.isPresent()) {
                log.info("이미 수강 중인 학생입니다: 학생 ID = {}, 과목 ID = {}", studentId, courseId);
                removeFromWaitlist(studentId, courseId); // 대기열에서 학생 제거
                continue; // 다음 학생으로 넘어감
            }

            // 정원 이내라면 수강 신청 처리
            try {
                Enroll newEnroll = new Enroll(student, course);
                enrollRepository.save(newEnroll); // 수강 신청 저장
                currentCount++; // 수강 인원 증가
                log.info("수강 신청 성공: 학생 ID = {}, 과목 ID = {}", studentId, courseId);
            } catch (Exception e) {
                log.error("수강 신청 처리 중 오류 발생: {}", e.getMessage());
                break; // 예외 발생 시 종료
            }

        }
        // 과목의 현재 수강 인원 업데이트
        course.setCount(currentCount);
        courseService.saveOrUpdateCourse(course);
        log.info("과목 ID = {}의 수강 신청 대기열 처리 종료", courseId);

    }

    public String getNextStudentIdInWaitlist(int courseId) {
        String key = "waitlist:course:" + courseId;
        // 대기열에서 가장 먼저 수강 신청한 학생 조회
        Set<Object> studentIds = redisTemplate.opsForZSet().range(key, 0, 0);
        if (studentIds != null && !studentIds.isEmpty()) {
            // Set<Object>에서 첫 번째 항목을 가져와 String으로 캐스팅
            Object firstStudentId = studentIds.iterator().next();
            if (firstStudentId instanceof String) {
                return (String) firstStudentId; // 안전하게 캐스팅
            }
        }
        return null; // 대기열에 학생이 없는 경우
    }



    // 대기열에서 학생 제거
    public void removeFromWaitlist(String studentId, int courseId) {
        String key = "waitlist:course:" + courseId;
        redisTemplate.opsForZSet().remove(key, studentId);
    }

    public void processNextInWaitlist(int courseId) {
        String key = "waitlist:course:" + courseId;
        Set<Object> firstInLine = redisTemplate.opsForZSet().range(key, 0, 0);

        if (!firstInLine.isEmpty()) {
            String studentId = (String) firstInLine.iterator().next();
            // 수강 신청 시도
            try {
                enrollService.enrollCourse(studentId, courseId);
                log.info("대기열에서 수강 신청 처리됨: 학생 ID = {}, 과목 ID = {}", studentId, courseId);
            } catch (AlreadyExistException e) {
                log.error("수강 신청 처리 중 오류: {}", e.getMessage());
            }
        }
    }

    // 대기열 크기 조회 TODO: 추후 수정
    public long getSize(int courseId){
        String key = "waitlist:course:" + courseId;
        return redisTemplate.opsForZSet().size(key);
    }


}