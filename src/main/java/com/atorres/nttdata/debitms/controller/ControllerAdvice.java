package com.atorres.nttdata.debitms.controller;

import com.atorres.nttdata.debitms.exception.CustomException;
import com.atorres.nttdata.debitms.exception.ErrorDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<ErrorDto> customExceptionHandler(CustomException ex){
        ErrorDto error =  new ErrorDto();
        error.setHttpStatus(ex.getStatus());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error,ex.getStatus());
    }
    @ExceptionHandler(value = ResponseStatusException.class)
    public ResponseEntity<ErrorDto> handleResponseStatusException(ResponseStatusException ex){
        HttpStatus httpStatus = HttpStatus.valueOf(ex.getStatusCode().value());

        // Obtener el cuerpo (body) de la respuesta desde el ServerHttpResponse
        String responseBody = ex.getReason();
        ObjectMapper objectMapper = new ObjectMapper();
        ErrorDto errorDto = new ErrorDto();
        try {
            errorDto = objectMapper.readValue(responseBody, ErrorDto.class);
        } catch (JsonProcessingException e) {
            errorDto.setMessage(e.getMessage());
            return ResponseEntity.status(httpStatus).body(errorDto);
        }
        // Utilizar Mono.map para deserializar el cuerpo en un objeto ErrorDto
        return ResponseEntity.status(httpStatus).body(errorDto);
    }
}
