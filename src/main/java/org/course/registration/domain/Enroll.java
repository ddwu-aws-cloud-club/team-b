package org.course.registration.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Table(name = "enroll")
@Getter
@Setter
public class Enroll{

    @Id @GeneratedValue
    @Column(name = "enroll_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    //==연관관계 메서드==//
    public void setStudent(Student student){
        this.student = student;
        student.getEnrolls().add(this);
    }
}