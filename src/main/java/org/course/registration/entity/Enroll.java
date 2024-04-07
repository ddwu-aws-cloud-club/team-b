package org.course.registration.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.Getter;

import jakarta.persistence.*;

@Entity
@Table(name = "enroll", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "course_id"})
})
@Getter
public class Enroll {

    /* 비식별 관계 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    // ==연관관계 메서드== //
    public void setStudent(Student student) {
        this.student = student;
        student.getEnrolls().add(this);
    }
}