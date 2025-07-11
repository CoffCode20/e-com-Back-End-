package co.istad.ishop.mapper;

import co.istad.ishop.entities.User;
import co.istad.ishop.model.request.UserCreateDTO;
import co.istad.ishop.model.request.UserUpdateDTO;
import co.istad.ishop.model.response.UserRespondDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id",  ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "isVerified", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreateDTO userCreateDTO);

    @Mapping(target = "id",  ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    @Mapping(target = "profilePicture", ignore = true)
    void toUserUpdate(UserUpdateDTO userUpdateDTO, @MappingTarget User user);

    UserRespondDTO  toUserRespondDTO(User user);
}
