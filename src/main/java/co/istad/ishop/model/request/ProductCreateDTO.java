package co.istad.ishop.model.request;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record ProductCreateDTO(

        @NotBlank
        String name,

        @Positive
        Double price,

        @Positive
        Integer qty,

        Set<String> imageUrls
) { }
