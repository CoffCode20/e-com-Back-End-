package co.istad.ishop.mapper;

import co.istad.ishop.entities.Brand;
import co.istad.ishop.model.request.BrandCreateDTO;
import co.istad.ishop.model.response.BrandRespondDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid",  ignore = true)
    @Mapping(target = "name", source = "name")
    Brand toBrandCreate(BrandCreateDTO brandCreateDTO);

    BrandRespondDTO toBrandRespondDTO(Brand brand);


}
