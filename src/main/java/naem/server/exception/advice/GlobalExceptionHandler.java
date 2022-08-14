package naem.server.exception.advice;

import static naem.server.exception.ErrorCode.*;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import naem.server.exception.CustomException;
import naem.server.exception.ErrorResponse;

@Slf4j
@RestControllerAdvice
// public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
public class GlobalExceptionHandler {

    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException() {
    //     log.error("handleDataException throw Exception : {}", METHOD_ARG_NOT_VALID);
    //     return ErrorResponse.toResponseEntity(METHOD_ARG_NOT_VALID);
    // }

    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // protected Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
    //     HttpServletRequest request) {
    //     return e.getBindingResult().getAllErrors();
    // }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected Object handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
        HttpServletRequest request) {
        String defaultMessage = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        return ErrorResponse.toResponseEntity(METHOD_ARG_NOT_VALID, defaultMessage);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class, DataIntegrityViolationException.class})
    protected ResponseEntity<ErrorResponse> handleDataException() {
        log.error("handleDataException throw Exception : {}", CONSTRAINT_VIOLATION);
        return ErrorResponse.toResponseEntity(CONSTRAINT_VIOLATION);
    }

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("handleCustomException throw CustomException : {}", e.getErrorCode());
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }
}
