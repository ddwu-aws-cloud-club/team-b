package org.course.registration.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class Student {
    @Id
    @Column(name = "student_id")
    private int id; // 학번

    @OneToMany(mappedBy = "student")
    private List<Enroll> enrolls = new ArrayList<>();

}


