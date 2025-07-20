package co.istad.ishop.model.request;

import java.util.Set;

import jakarta.persistence.ElementCollection;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record ProductUpdateDTO(

        @Positive
        Double price,

        @Positive
        Integer qty,

        @ElementCollection
        Set<String> imageUrls

) { }
