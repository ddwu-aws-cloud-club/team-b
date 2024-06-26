package org.course.registration.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;

import org.course.registration.entity.Student;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.course.registration.repository.StudentRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    /***
     * 학생 등록
     */
    @Transactional
    public String register(Student student) {
        validateDuplicateStudent(student); // 중복 학생 검증
        studentRepository.save(student);
        return student.getStudentId();
    }

    public void CreateSession(Student student, HttpServletRequest request) {
        // 세션에 학번 저장
        HttpSession session = request.getSession();
        session.setAttribute("studentId", student.getStudentId());
        session.setAttribute("phoneNum", student.getPhoneNum());
    }

    private void validateDuplicateStudent(Student student) {
        List<Student> findStudents = studentRepository.findById(student.getStudentId());

        if(!findStudents.isEmpty()){
            throw new IllegalStateException("이미 등록된 학번입니다.");
        }
    }

    // 학생 ID로 학생 조회
    public Student findStudentById(String studentId) {
        return studentRepository.findOneById(studentId);
    }
}