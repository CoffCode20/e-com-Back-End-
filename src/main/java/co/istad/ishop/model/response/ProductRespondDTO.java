package co.istad.ishop.model.response;

import lombok.Builder;

import java.util.Set;

@Builder
public record ProductRespondDTO(

        String uuid,

        String name,

        Double price,

        Integer qty,

        Set<String> imageUrls
) { }
