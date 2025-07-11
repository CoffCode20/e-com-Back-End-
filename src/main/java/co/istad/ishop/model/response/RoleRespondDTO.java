package co.istad.ishop.model.response;

import lombok.Builder;

@Builder
public record RoleRespondDTO (

        String uuid,

        String name
){
}
