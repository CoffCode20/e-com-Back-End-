package co.istad.ishop.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
public record ErrorRespond<T>(
        Integer code,
        String message,
        LocalDateTime timestamp,
        T data
){
}
