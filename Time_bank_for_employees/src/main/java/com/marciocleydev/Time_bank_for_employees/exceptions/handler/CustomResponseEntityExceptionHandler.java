package com.marciocleydev.Time_bank_for_employees.exceptions.handler;

import com.marciocleydev.Time_bank_for_employees.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@ControllerAdvice //serve para centralizar o tratamento de erros dos controllers. Ou seja, em vez de tratar erros em cada controller, você faz isso em um só lugar
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    //O HttpServletRequest é um objeto do Java usado para representar uma requisição HTTP feita pelo cliente (por exemplo, navegador) para o servidor. Ele contém informações como parâmetros enviados, headers, dados do corpo, URL acessada, métodos (GET, POST, etc.) e outras informações da requisição.
    //ResponseEntity é usado no Spring para devolver uma resposta HTTP completa = corpo + status + headers! Só o objeto = só o corpo
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> handleResourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request){
        String error = "Resource not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError standardError = new StandardError(Instant.now(),status.value(),error,e.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<StandardError> handleBadRequestException(BadRequestException e, HttpServletRequest request){
        String error = "Bad request";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError standardError = new StandardError(Instant.now(),status.value(),error,e.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(DataIntegrityException.class)
    public ResponseEntity<StandardError> handleDataIntegrityException(DataIntegrityException e, HttpServletRequest request){
        String error = "Bad request";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError standardError = new StandardError(Instant.now(),status.value(),error,e.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    public ResponseEntity<StandardError> handleInvalidJwtAuthenticationException(InvalidJwtAuthenticationException e, HttpServletRequest request){
        String error = "Invalid JWT token";
        HttpStatus status = HttpStatus.FORBIDDEN;
        StandardError standardError = new StandardError(Instant.now(),status.value(),error,e.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(standardError);
    }
}
