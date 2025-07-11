package co.istad.ishop.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFound extends ApiException{

    public ResourceNotFound(String resName, Integer id) {
        super(HttpStatus.NOT_FOUND, String.format("%s WITH %d NOT FOUND", resName, id));
    }
}
