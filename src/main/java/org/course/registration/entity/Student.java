package org.course.registration.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* unique 제약 조건 추가 */
    @Column(name = "student_id")
    private String studentId; // 학번

    @Column(name = "phone_number")
    private String phoneNumber; // 전화번호

    @OneToMany(mappedBy = "student")
    private List<Enroll> enrolls = new ArrayList<>();

    public Student(String studentId, String phoneNumber) {
        this.studentId = studentId;
        this.phoneNumber = phoneNumber;
    }
}


