package co.istad.ishop.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

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
