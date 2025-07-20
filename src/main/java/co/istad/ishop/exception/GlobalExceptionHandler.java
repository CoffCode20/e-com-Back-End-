package co.istad.ishop.exception;

import co.istad.ishop.model.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> handle(ApiException ex) {
        ErrorRespond<Object> errorRespond = ErrorRespond.builder()
                .code(ex.getHttpStatus().value())
                .message("Error Service")
                .timestamp(LocalDateTime.now())
                .data(ex.getMessage())
                .build();
        return ResponseEntity.status(ex.getHttpStatus()).body(errorRespond);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleIllegalArgument(ResponseStatusException ex) {
        ErrorRespond<Object> errorRespond = ErrorRespond.builder()
                .code(ex.getStatusCode().value())
                .message("Error Service")
                .timestamp(LocalDateTime.now())
                .data(ex.getReason())
                .build();
        return ResponseEntity.status(ex.getStatusCode()).body(errorRespond);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorRespond<Object>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
         ErrorRespond<Object> errorRespond = ErrorRespond.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message("Error Service")
                .timestamp(LocalDateTime.now())
                .data(ex.getMessage())
                .build();
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorRespond);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorRespond<Object>> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorRespond<Object> errorRespond = ErrorRespond.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Error Form User Input")
                .timestamp(LocalDateTime.now())
                .data(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorRespond);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNotVerifiedException.class)
    public ResponseEntity<BaseResponse<Object>> handleUserNotVerifiedException(UserNotVerifiedException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "User not verified");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<BaseResponse<Object>> handleInvalidPasswordException(InvalidPasswordException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid password");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Object>> handleGenericException(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    private ResponseEntity<BaseResponse<Object>> buildErrorResponse(HttpStatus status, String data) {
        BaseResponse<Object> error = new BaseResponse<>(
                status.toString(),
                "Authentication failed",
                LocalDateTime.now().toString(),
                Map.of("data", data)
        );
        return ResponseEntity.status(status).body(error);
    }

}
