package org.course.registration.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class EnrolmentController {

    @GetMapping("/enrolment")
    public String enrolment(){
        return "enrolls";
    }

}
