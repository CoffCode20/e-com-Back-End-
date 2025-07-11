package co.istad.ishop.model.request;

import lombok.Builder;

@Builder
public record UpdatePasswordRequest(
        String oldPassword,
        String newPassword,
        String confirmPassword
) { }
