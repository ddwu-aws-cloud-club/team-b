package org.course.registration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.course.registration.domain.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Student findById(int id);
}
