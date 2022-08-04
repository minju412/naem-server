package naem.server.exhandler.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import naem.server.exhandler.CustomException;
import naem.server.exhandler.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // @ExceptionHandler(value = { ConstraintViolationException.class, DataIntegrityViolationException.class})
    // protected ResponseEntity<ErrorResponse> handleDataException() {
    //     log.error("handleDataException throw Exception : {}", DUPLICATE_RESOURCE);
    //     return ErrorResponse.toResponseEntity(DUPLICATE_RESOURCE);
    // }

    @ExceptionHandler(value = { CustomException.class })
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("handleCustomException throw CustomException : {}", e.getErrorCode());
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }
}
