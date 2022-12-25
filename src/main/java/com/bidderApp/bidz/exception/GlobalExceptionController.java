package com.bidderApp.bidz.exception;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler
    public ResponseEntity<ExceptionObject>handleDataNotFoundException(NoDataFoundException noDataFoundException){
        return getExceptionResponse(HttpStatus.NOT_FOUND, noDataFoundException.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionObject>handleInvalidDataFoundException(InvalidDataFoundException invalidDataFoundException){
        return getExceptionResponse(HttpStatus.BAD_REQUEST, invalidDataFoundException.getMessage());

    }
    @ExceptionHandler
    public ResponseEntity<ExceptionObject>handleUnauthorizedException(UnauthorizedException unauthorizedException){
        return getExceptionResponse(HttpStatus.UNAUTHORIZED, unauthorizedException.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionObject>handleDataAlreadyExistException(DataAlreadyExistException dataAlreadyExistException){
        return getExceptionResponse(HttpStatus.CONFLICT, dataAlreadyExistException.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionObject>handleInternalServerErrorException(InternalServerErrorException internalServerErrorException){
        return getExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, internalServerErrorException.getMessage());
    }
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ExceptionObject> FoundException(Exception ex){
        return getExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @NotNull
    private static ResponseEntity<ExceptionObject> getExceptionResponse(HttpStatus httpStatus, String exceptionMessage) {
        ExceptionObject exceptionObject = new ExceptionObject();
        exceptionObject.setStatusCode(httpStatus.value());
        exceptionObject.setMessage(exceptionMessage);
        exceptionObject.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(exceptionObject, HttpStatus.OK);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()

                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.OK);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;

    }

}

