package org.course.registration.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.course.registration.domain.Course;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CourseRepository {

    private final EntityManager em;

    public List<Course> findAll(){
        return em.createQuery("select c from Course c", Course.class)
                .getResultList();
    }

    // ID로 과목 조회
    public Course findById(int id){
        return em.find(Course.class, id);
    }

    public void persist(Course course) {
        em.persist(course);
    }

    public Course merge(Course course) {
        return em.merge(course);
    }

}
