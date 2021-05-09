package com.mballem.curso.security.web.controller.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ModelAndView usuarioNaoEncontradoException(UsernameNotFoundException ex){
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("stauts",404);
        modelAndView.addObject("error","Operaçao não pode ser realizada.");
        modelAndView.addObject("message",ex.getMessage());
        return modelAndView;
    }
}
