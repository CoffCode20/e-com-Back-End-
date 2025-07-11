package co.istad.ishop.model.request;

import jakarta.persistence.ElementCollection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.util.Set;

@Builder
public record ProductUpdateDTO(

        @Positive
        Double price,

        @Positive
        Integer qty,

        @ElementCollection
        Set<String> imageUrls

) { }
