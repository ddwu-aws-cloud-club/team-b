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
    @Column(name = "student_id")
    private int id; // 학번

    private String phoneNum; // 전화번호

    @OneToMany(mappedBy = "student")
    private List<Enroll> enrolls = new ArrayList<>();

    public Student(int studentId, String phoneNum) {
        this.id = studentId;
        this.phoneNum = phoneNum;
    }
}


