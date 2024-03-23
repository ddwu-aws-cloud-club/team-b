package org.course.registration.service;

import lombok.RequiredArgsConstructor;
import org.course.registration.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.course.registration.repository.StudentRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * 학생 등록
     */
    @Transactional
    public int registerStudent(Student student){
        validateDuplicateStudent(student); // 중복 학생 검증
        Student savedStudent = studentRepository.save(student);
        return savedStudent.getId();
    }

    private void validateDuplicateStudent(Student student){
        // 학생 ID로 학생 조회
        Student existingStudent = studentRepository.findById(student.getId());
        if(existingStudent != null){
            throw new IllegalStateException("이미 등록된 학번입니다.");
        }
    }

    // 학생 ID로 학생 조회
    public Student findStudentById(int id){
        return studentRepository.findById(id);
    }
}
