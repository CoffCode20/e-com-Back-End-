package co.istad.ishop.model.response;

import lombok.Builder;

@Builder
public record UserRespondDTO(

        String uuid,

        String username,

        String email,

        String phoneNumber,

        String profilePicture

) {
}
