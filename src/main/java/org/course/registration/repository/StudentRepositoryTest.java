package org.course.registration.repository;

import org.course.registration.domain.Student;
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
@Transactional
@Rollback(value = false)
public class StudentRepositoryTest {

    @Autowired StudentRepository studentRepository;

    @Test
    public void testStudent() throws Exception{
        //given
        Student student = new Student();
        student.setId(20201044);

        //when
        studentRepository.save(student);

        //then
    }
}