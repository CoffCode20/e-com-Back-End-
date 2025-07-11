package co.istad.ishop.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record BrandCreateDTO(

        @NotBlank
        String name
) { }
