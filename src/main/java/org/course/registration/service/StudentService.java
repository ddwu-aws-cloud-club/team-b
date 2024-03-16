package org.course.registration.service;

import lombok.RequiredArgsConstructor;
import org.course.registration.domain.Student;
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
    public int register(Student student){
        validateDuplicateStudent(student); // 중복 학생 검증
        studentRepository.save(student);
        return student.getId();
    }

    private void validateDuplicateStudent(Student student){
        List<Student> findStudents = studentRepository.findById(student.getId());
        if(!findStudents.isEmpty()){
            throw new IllegalStateException("이미 등록된 학번입니다.");
        }
    }

    // 학생 전체 조회
    public List<Student> findStudents(){
        return studentRepository.findAll();
    }

    // 학생 전체 조회

}
