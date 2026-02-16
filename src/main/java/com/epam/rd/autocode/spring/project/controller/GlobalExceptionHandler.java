package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    private String getReferer(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        return (referer != null) ? "redirect:" + referer : "redirect:/";
    }

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(NotFoundException ex, HttpServletRequest request, RedirectAttributes ra) {
        ra.addFlashAttribute("errorMessage", "Error: " + ex.getMessage());
        return getReferer(request);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public String handleAlreadyExists(AlreadyExistException ex, HttpServletRequest request, RedirectAttributes ra) {
        ra.addFlashAttribute("errorMessage", "Conflict: " + ex.getMessage());
        return getReferer(request);
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralError(Exception ex, RedirectAttributes ra) {
        ra.addFlashAttribute("errorMessage", "An unexpected error occurred: " + ex.getMessage());
        return "redirect:/";
    }
}
