package org.course.registration.repository;

import jakarta.persistence.EntityManager;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

import org.course.registration.entity.Student;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StudentRepository {

    private final EntityManager em;

    public void save(Student student) {
        em.persist(student);
    }

    public Student fineOne(int id) {
        return em.find(Student.class, id);
    }

    public List<Student> findAll() {
        return em.createQuery("select s from Student s", Student.class)
                .getResultList();
    }

    public List<Student> findById(String studentId) {
        return em.createQuery("select s from Student s where s.studentId = :id", Student.class)
                .setParameter("id", studentId)
                .getResultList();
    }

    // 특정 ID에 해당하는 학생 한 명을 반환
    public Student findOneById(String studentId) {
        return em.find(Student.class, studentId);
    }
}