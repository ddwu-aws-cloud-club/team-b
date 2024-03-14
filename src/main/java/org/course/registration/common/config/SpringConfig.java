package org.course.registration.common.config;


import jakarta.persistence.EntityManager;
import org.course.registration.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ComponentScan(basePackages = {"org.course.registration"})
public class SpringConfig {
//    private EntityManager em;
//
//    @Autowired
//    public SpringConfig(EntityManager em) {
//        this.em = em;
//    }

    @Bean
    public StudentService studentService() {
        return new StudentService();
    }
}
