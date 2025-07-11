package co.istad.ishop.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {
    private String httpStatus;
    private String message;
    private String timestamp;
    private Map<String, T> payload;
}