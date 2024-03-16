package org.course.registration.service;

import org.course.registration.domain.Student;
import org.course.registration.repository.StudentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentServiceTest {

    @Autowired StudentService studentService;
    @Autowired StudentRepository studentRepository;

    @Test
    @Transactional
    public void 회원등록() throws Exception{
        // given
        Student student = new Student();
        student.setId(20201052);

        // when
        int saveId = studentService.register(student);

        // then
        assertEquals(student, studentRepository.fineOne(saveId));
    }

    @Test
    public void 중복_회원_예외() throws Exception{
        // given

        // when

        // then
    }
}