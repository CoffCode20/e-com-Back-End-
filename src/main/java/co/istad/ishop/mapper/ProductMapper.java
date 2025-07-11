package co.istad.ishop.mapper;

import co.istad.ishop.entities.Product;
import co.istad.ishop.model.request.ProductCreateDTO;
import co.istad.ishop.model.request.ProductUpdateDTO;
import co.istad.ishop.model.response.ProductRespondDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    Product toProduct(ProductCreateDTO productCreateDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "name", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toProductUpdate(ProductUpdateDTO productUpdateDTO, @MappingTarget Product product);

    ProductRespondDTO toProductRespondDTO(Product product);

}




