package com.mokitoyjunit.springmokitoyjunit.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {

    Map<String, Object> responseBody;


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> methodArgumentNotValidException( MethodArgumentNotValidException ex){

        //-- Preparando ResponseBody
        this.responseBody=new HashMap<>();

        //-- Obteniendo errores
        this.responseBody.put("send","@RestControllerAdvice");
        this.responseBody.put("timestamp", LocalDateTime.now());
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            this.responseBody.put(fieldName, errorMessage);
        });

        //-- Enviando ResponseBody
        return  this.responseBody;
    }


/**/

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RuntimeExceptionDataNotFound.class)
    public Map<String, Object> runtimeExceptionDataNotFound (RuntimeExceptionDataNotFound ex){

        //-- Preparando ResponseBody
        this.responseBody=new HashMap<>();

        //-- Obteniendo errores
        this.responseBody.put("send","@RestControllerAdvice");
        this.responseBody.put("timestamp", LocalDateTime.now());
        this.responseBody.put("message",ex.getMessage());
        this.responseBody.put("code",ex.hashCode());

        //-- Enviando ResponseBody
        return this.responseBody;
    }


    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(ExceptionSaldoUnsuficiente.class)
    public Map<String, Object> exceptionSaldoUnsuficiente(ExceptionSaldoUnsuficiente ex){

        //-- Preparando ResponseBody
        this.responseBody=new HashMap<>();

        //-- Obteniendo errores
        this.responseBody.put("send","@RestControllerAdvice");
        this.responseBody.put("timestamp", LocalDateTime.now());
        this.responseBody.put("message",ex.getMessage());
        this.responseBody.put("code",ex.hashCode());

        //-- Enviando ResponseBody
        return this.responseBody;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Map<String, Object> exception(Exception ex){

        //-- Preparando ResponseBody
        this.responseBody=new HashMap<>();

        //-- Obteniendo errores
        this.responseBody.put("send","@RestControllerAdvice");
        this.responseBody.put("timestamp", LocalDateTime.now());
        this.responseBody.put("message",ex.getMessage());
        this.responseBody.put("code",ex.hashCode());

        //-- Enviando ResponseBody
        return this.responseBody;
    }



}
