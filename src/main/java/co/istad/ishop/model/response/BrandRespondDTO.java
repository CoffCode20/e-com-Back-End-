package co.istad.ishop.model.response;

import lombok.Builder;

@Builder
public record BrandRespondDTO(

        String uuid,

        String name

) { }
