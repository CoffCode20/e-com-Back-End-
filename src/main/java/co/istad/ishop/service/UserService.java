package co.istad.ishop.service;

import co.istad.ishop.entities.User;
import co.istad.ishop.model.request.SetNewPasswordRequest;
import co.istad.ishop.model.request.UpdatePasswordRequest;
import co.istad.ishop.model.request.UserCreateDTO;
import co.istad.ishop.model.request.UserUpdateDTO;
import co.istad.ishop.model.response.UserRespondDTO;

import java.util.List;

public interface UserService {

    User createUser(UserCreateDTO userCreateDto);

    List<UserRespondDTO> findAllUsers();

    UserRespondDTO getUserByUuid(String uuid);

    void updateUser(String uuid, UserUpdateDTO userUpdateDTO);

    void forgotPassword(String uuid, SetNewPasswordRequest setNewPasswordRequest);

    void updatePassword(String uuid, UpdatePasswordRequest updatePasswordRequest);

    void deleteUser(String uuid);

    User promoteUserToAdmin(String username);

}
