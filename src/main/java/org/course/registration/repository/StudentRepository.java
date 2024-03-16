package org.course.registration.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.course.registration.domain.Student;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StudentRepository {

    private final EntityManager em;

    public void save(Student student){
        em.persist(student);
    }

    public Student fineOne(int id){
        return em.find(Student.class, id);
    }

    public List<Student> findAll(){
        return em.createQuery("select s from Student s", Student.class)
                .getResultList();
    }

    public List<Student> findById(int id){
        return em.createQuery("select s from Student s where s.id = :id", Student.class)
                .setParameter("id", id)
                .getResultList();
    }
}
