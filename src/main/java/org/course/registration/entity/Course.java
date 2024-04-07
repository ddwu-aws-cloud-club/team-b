package org.course.registration.entity;

import jakarta.persistence.*;

import lombok.Getter;

@Entity
@Getter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String professor;

    private int limited;

    @Column(name = "current_count")
    private int currentCount;
}