package org.course.registration.domain;

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

    public Student(int student_id, String phoneNum){
        this.id = student_id;
        this.phoneNum = phoneNum;
    }

}


