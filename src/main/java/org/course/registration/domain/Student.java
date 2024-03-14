package org.course.registration.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Student {

//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long studentId;
    private String name;

}
