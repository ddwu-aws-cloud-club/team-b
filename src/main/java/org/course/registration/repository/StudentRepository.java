package org.course.registration.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.course.registration.domain.Student;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Student findById(int id);
}
