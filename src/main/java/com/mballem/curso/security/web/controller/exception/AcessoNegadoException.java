package com.mballem.curso.security.web.controller.exception;

public class AcessoNegadoException extends RuntimeException{

    public AcessoNegadoException(String message) {
        super(message);
    }
}
