package co.istad.ishop.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record SetNewPasswordRequest(

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 8, max = 16)
        String newPassword,

        @NotBlank
        @Size(min = 8, max = 16)
        String confirmPassword
){ }
