package org.course.registration.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import lombok.RequiredArgsConstructor;

import org.course.registration.entity.Course;
import org.course.registration.entity.Enroll;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EnrollRepository {

    private final EntityManager em;

    public void save(Enroll enroll) {
        em.persist(enroll);
    }

    // 학생의 수강신청한 과목들 조회
    public List<Course> findCoursesByStudentId(int studentId) {
        TypedQuery<Course> query = em.createQuery(
                "SELECT e.course FROM Enroll e WHERE e.student.id = :studentId", Course.class);
        query.setParameter("studentId", studentId);
        return query.getResultList();
    }

    // 수강 취소
    public void deleteByStudentIdAndCourseId(int studentId, int courseId) {
        em.createQuery("DELETE FROM Enroll e WHERE e.student.id = :studentId AND e.course.id = :courseId")
                .setParameter("studentId", studentId)
                .setParameter("courseId", courseId)
                .executeUpdate();
    }

    // 이미 수강신청한 과목인지 check
    public Optional<Enroll> findByStudentIdAndCourseId(String studentId, int courseId) {
        return em.createQuery("SELECT e FROM Enroll e WHERE e.student.studentId = :studentId AND e.course.id = :courseId", Enroll.class)
                .setParameter("studentId", studentId)
                .setParameter("courseId", courseId)
                .getResultList().stream().findFirst();
    }
}
