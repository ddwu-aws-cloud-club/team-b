package org.course.registration.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ModelAndView handleIllegalStateException(IllegalStateException ex) {

        ModelAndView modelAndView = new ModelAndView("redirect:/enrollment");
        return modelAndView;
    }

    @ExceptionHandler(NotEnoughException.class)
    public String handleNotEnoughException(NotEnoughException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "정원이 초과되었습니다.");
        return "redirect:/enrollment?overError=true";
    }

    @ExceptionHandler(AlreadyExistException.class)
    public String handleAlreadyExistException(AlreadyExistException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "이미 수강신청된 과목입니다.");
        return "redirect:/enrollment?existError=true";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "수강신청 중 오류가 발생했습니다.");
        return "redirect:/enrollment?error=true";
    }
}