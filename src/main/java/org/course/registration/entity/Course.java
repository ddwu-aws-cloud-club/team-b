package org.course.registration.entity;

import jakarta.persistence.*;

import lombok.Getter;

@Entity
@Getter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // 과목 id

    private String name; // 과목 이름

    private String professor; // 교수님 성함

    private int limited; // 수강 정원

    private int count; // 현재 정원

    public void increaseCount() {
        this.count += 1;
    }

    public void decreaseCount() {
        this.count = Math.max(0, this.count - 1);
    }
}