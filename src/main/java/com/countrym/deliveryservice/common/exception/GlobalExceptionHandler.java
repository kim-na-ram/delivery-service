package com.countrym.deliveryservice.common.exception;

import com.countrym.deliveryservice.common.response.FailureResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity<FailureResponse> handleApiException(BaseException baseException) {
        FailureResponse failureResponse = FailureResponse.from(baseException);
        return new ResponseEntity<>(failureResponse, baseException.getHttpStatus());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<FailureResponse> handleException(Exception exception) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        FailureResponse failureResponse = FailureResponse.of(httpStatus.value(), exception.getMessage());
        return new ResponseEntity<>(failureResponse, httpStatus);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<FailureResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        List<String> fieldErrorList = exception.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        FailureResponse errorResponse = FailureResponse.of(httpStatus.value(), fieldErrorList.toString());
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<FailureResponse> handlerFolderNotFoundException(ConstraintViolationException exception) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        FailureResponse errorResponse = FailureResponse.of(httpStatus.value(), exception.getMessage());
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}