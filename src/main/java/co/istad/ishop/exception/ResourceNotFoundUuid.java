package co.istad.ishop.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundUuid extends ApiException{

    public ResourceNotFoundUuid(String resName, String uuid) {
        super(HttpStatus.NOT_FOUND, String.format("%s WITH %s NOT FOUND", resName, uuid));
    }
}
