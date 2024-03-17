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

    // Course 엔티티 저장 또는 업데이트
    public Course save(Course course) {
        if (course.getId() == 0) {
            // 새로운 거면 저장
            em.persist(course);
            return course;
        } else {
            // 이미 존재하면 업데이트
            return em.merge(course);
        }
    }
}
