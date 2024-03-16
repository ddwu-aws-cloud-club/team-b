package org.course.registration.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.course.registration.domain.Enroll;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EnrollRepository {
    private final EntityManager em;

    public void save(Enroll enroll){
        em.persist(enroll);
    }

    public Enroll findOne(Long id){
        return em.find(Enroll.class, id);
    }

    public List<Enroll> findAll() {
        return em.createQuery("select e from Enroll e", Enroll.class)
                .getResultList();
    }

    public List<Enroll> findByStudentId(int studentId){
        return em.createQuery("select e from Enroll e where e.student.id = :studentId", Enroll.class)
                .setParameter("studentId", studentId)
                .getResultList();
    }


}
