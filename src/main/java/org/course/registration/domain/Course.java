package org.course.registration.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.course.registration.exception.NotEnoughException;

@Entity
@Getter
@Setter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "course_id")
    private int id; // 과목 id

    private String name; // 과목 이름

    private String professor; // 교수님 성함

    private int limited; // 수강 정원

    private int count; // 현재 정원
}