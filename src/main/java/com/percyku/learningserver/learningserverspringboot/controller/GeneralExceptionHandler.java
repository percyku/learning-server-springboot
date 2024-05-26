package com.percyku.learningserver.learningserverspringboot.controller;

import com.percyku.learningserver.learningserverspringboot.util.CommonErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;

import java.util.List;

@ControllerAdvice
public class GeneralExceptionHandler {
    private final static Logger log = LoggerFactory.getLogger(GeneralExceptionHandler.class);
    //add exception handling code here

    //Add an exception handler using @ExceptionHandler
    @ExceptionHandler
    public ResponseEntity<CommonErrorResponse> handleException(CommonException commonException){

        //create a CommonErrorResponse
        CommonErrorResponse error =new CommonErrorResponse();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(commonException.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        //return ResponseEntity
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<CommonErrorResponse> MethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException){

        log.debug("Which controller method method :" +methodArgumentNotValidException.getParameter().getExecutable().getName());

        BindingResult rs =methodArgumentNotValidException.getBindingResult();
        StringBuilder resultMsg =new StringBuilder();

        if(rs.hasErrors()){
            List<FieldError> fieldErrors=rs.getFieldErrors();
            fieldErrors.forEach(fieldError ->{
                resultMsg.append(fieldError.getDefaultMessage()).append(",");
            });
        }

        log.debug("Error resultMsg :" +resultMsg.toString());

        //create a UserErrorResponse
        CommonErrorResponse error =new CommonErrorResponse();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(resultMsg.toString());
        error.setTimeStamp(System.currentTimeMillis());

        //return ResponseEntity
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity<CommonErrorResponse> HttpMessageNotReadableException(HttpMessageNotReadableException httpMessageNotReadableException,
                                                                                HandlerMethod handlerMethod){
        log.debug("Which controller method method :" +handlerMethod.getMethod());

        StringBuilder resultMsg =new StringBuilder();
        resultMsg.append(httpMessageNotReadableException.getMessage());


        log.debug("Error resultMsg :" +resultMsg.toString());

        //create a UserErrorResponse
        CommonErrorResponse error =new CommonErrorResponse();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(resultMsg.toString());
        error.setTimeStamp(System.currentTimeMillis());

        //return ResponseEntity
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

}
