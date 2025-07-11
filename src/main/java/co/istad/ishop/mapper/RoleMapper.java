package co.istad.ishop.mapper;

import co.istad.ishop.entities.Role;
import co.istad.ishop.model.request.CreateRoleDTO;
import co.istad.ishop.model.response.RoleRespondDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid",  ignore = true)
    @Mapping(target = "name", source = "name")
    Role toRole(CreateRoleDTO roleDTO);

    RoleRespondDTO toRoleRespondDTO(Role role);


}
