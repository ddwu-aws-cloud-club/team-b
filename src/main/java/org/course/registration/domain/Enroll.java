package org.course.registration.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Table(name = "enroll", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"studentId", "courseId"})
})
@Getter
@Setter
public class Enroll{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId")
    private Course course;

    //==연관관계 메서드==//
    public void setStudent(Student student){
        this.student = student;
        student.getEnrolls().add(this);
    }
}