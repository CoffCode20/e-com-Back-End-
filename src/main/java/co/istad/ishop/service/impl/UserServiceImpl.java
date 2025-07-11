package co.istad.ishop.service.impl;

import co.istad.ishop.entities.Role;
import co.istad.ishop.entities.User;
import co.istad.ishop.exception.ApiException;
import co.istad.ishop.exception.ResourceNotFoundUuid;
import co.istad.ishop.mapper.UserMapper;
import co.istad.ishop.model.request.SetNewPasswordRequest;
import co.istad.ishop.model.request.UpdatePasswordRequest;
import co.istad.ishop.model.request.UserCreateDTO;
import co.istad.ishop.model.request.UserUpdateDTO;
import co.istad.ishop.model.response.UserRespondDTO;
import co.istad.ishop.repository.RoleRepository;
import co.istad.ishop.repository.UserRepository;
import co.istad.ishop.service.OtpService;
import co.istad.ishop.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final OtpService otpService;


    @Override
    public User createUser(UserCreateDTO userCreateDto) {
        User user = userMapper.toUser(userCreateDto);
        // handle exception for register
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already exist.");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already exist.");
        }
        user.setUuid(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsVerified(false);
        user.setIsActive(true);
        user.setIsDeleted(false);
        user.setCreatedDate(new Date(System.currentTimeMillis()));

        Role saleRole = roleRepository.findRoleByName("SALE");
        if (saleRole == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "ROLE_SALE NOT-FOUNT");
        }
        user.setRoles(Set.of(saleRole));

        user = userRepository.save(user);
        try{
            otpService.generateAndSendOtp(user.getEmail());
        }catch (MessagingException e){
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send OTP.");
        }
        return user;
    }

    @Override
    public List<UserRespondDTO> findAllUsers() {
        List<UserRespondDTO> userList = userRepository.findAll().stream()
                .filter(u -> u.getIsDeleted() == false)
                .map(userMapper::toUserRespondDTO)
                .toList();
        if (userList.isEmpty()) {
            throw new ApiException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND");
        }
        return userList;
    }

    @Override
    public UserRespondDTO getUserByUuid(String uuid) {
        User findUserByUuid = userRepository.findUserByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundUuid("User", uuid));
        return userMapper.toUserRespondDTO(findUserByUuid);
    }

    @Override
    public void updateUser(String uuid, UserUpdateDTO userCreateDto) {
        User existingUser = userRepository.findAll().stream()
                .filter(user -> user.getUuid().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundUuid("User", uuid));
        if(userCreateDto.username() != null) {
            existingUser.setUsername(userCreateDto.username());
        }

        if(userCreateDto.profilePicture() != null) {
            existingUser.setProfilePicture(userCreateDto.profilePicture());
        }
        userRepository.save(existingUser);
    }

    @Override
    public void forgotPassword(String uuid, SetNewPasswordRequest setNewPasswordRequest) {
        User user = userRepository.findUserByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundUuid("User", uuid));
        if (setNewPasswordRequest.newPassword().equals(setNewPasswordRequest.confirmPassword())
                && user.getEmail().equals(setNewPasswordRequest.email())) {
            user.setPassword(passwordEncoder.encode(setNewPasswordRequest.newPassword()));
            userRepository.save(user);
            return;
        }
        throw new ApiException(HttpStatus.BAD_REQUEST, "PASSWORD_MISMATCH");

    }

    @Override
    public void updatePassword(String uuid, UpdatePasswordRequest updatePasswordRequest) {
        User user = userRepository.findUserByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundUuid("User", uuid));
        if (passwordEncoder.matches(updatePasswordRequest.oldPassword(), user.getPassword())) {
            if (updatePasswordRequest.newPassword().equals(updatePasswordRequest.confirmPassword())) {
                user.setPassword(passwordEncoder.encode(updatePasswordRequest.newPassword()));
                userRepository.save(user);
            } else {
                throw new ApiException(HttpStatus.BAD_REQUEST, "NEW_PASSWORD_MISMATCH");
            }
        } else {
            throw new ApiException(HttpStatus.BAD_REQUEST, "OLD_PASSWORD_INCORRECT");
        }
    }

    @Override
    public void deleteUser(String uuid) {
        User userToDelete = userRepository.findAll().stream()
                .filter(user -> user.getUuid().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundUuid("User", uuid));
        userToDelete.setIsDeleted(true);
        userToDelete.setIsActive(false);
        userRepository.save(userToDelete);
    }

    // method to promote normal USER to ADMIN
    @Override
    public User promoteUserToAdmin(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role adminRole = roleRepository.findRoleByName("ADMIN");
        if (adminRole ==  null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Admin role is not found.");
        }

        user.getRoles().add(adminRole);
        return userRepository.save(user);
    }
}
