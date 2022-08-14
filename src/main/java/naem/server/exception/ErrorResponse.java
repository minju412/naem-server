package naem.server.exception;

import org.springframework.http.ResponseEntity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
    private final String error;
    private final String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
            .status(errorCode.getHttpStatus())
            .body(ErrorResponse.builder()
                .error(errorCode.getHttpStatus().name())
                .message(errorCode.getMessage())
                .build()
            );
    }

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode, String defaultMessage) {
        return ResponseEntity
            .status(errorCode.getHttpStatus())
            .body(ErrorResponse.builder()
                .error(errorCode.getHttpStatus().name())
                .message(defaultMessage)
                .build()
            );
    }
}
