package com.dp.ishare.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class FileExceptionHandler {
    @ExceptionHandler(FileException.class)
    public Object fileExceptionHandler(Model model, FileException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(e.getRedirect());
        model.addAttribute("message",e.getMessage());
        return modelAndView;
    }
}
