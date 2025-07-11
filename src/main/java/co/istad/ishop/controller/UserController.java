package co.istad.ishop.controller;

import co.istad.ishop.exception.ApiException;
import co.istad.ishop.model.request.*;
import co.istad.ishop.model.response.UserRespondDTO;
import co.istad.ishop.service.OtpService;
import co.istad.ishop.service.RateLimiter;
import co.istad.ishop.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final OtpService otpService;
    private final RateLimiter rateLimiter;

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> register(@Valid @RequestBody UserCreateDTO userCreateDto) {
        if(!rateLimiter.resolveBucket(userCreateDto.username()).tryConsume(1)){
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many registration attempts");
        }
        try{
            userService.createUser(userCreateDto);
            return ResponseEntity.ok("OTP sent successfully to " + userCreateDto.email());
        }catch (ApiException e){
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }

    @PostMapping("/verified-otp")
    public ResponseEntity<String> verifiedOtp(@RequestParam String otp) {
        if(!rateLimiter.resolveBucket(otp).tryConsume(1)){
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many verification attempts");
        }
        try{
            otpService.verifyOtp(otp);
            return ResponseEntity.ok("Email verified successfully");
        }catch (ApiException e){
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@RequestBody ResendOtpRequest resendOtpRequest) {
        if (!rateLimiter.resolveBucket(resendOtpRequest.getEmail()).tryConsume(1)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many OTP requests");
        }
        try {
            otpService.generateAndSendOtp(resendOtpRequest.getEmail());
            return ResponseEntity.ok("OTP sent to " + resendOtpRequest.getEmail());
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send OTP");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, List<UserRespondDTO>>> getUsers() {
        return ResponseEntity.ok(Map.of("users", userService.findAllUsers()));
    }

    @GetMapping("/uuid/{userUuid}")
    public ResponseEntity<Map<String, UserRespondDTO>> getUserByUuid(@PathVariable String userUuid) {
        return ResponseEntity.ok(Map.of("users", userService.getUserByUuid(userUuid)));
    }

    @PutMapping("/uuid/{userUuid}")
    public ResponseEntity<String> updateUserByUuid(@PathVariable String userUuid, @RequestBody UserUpdateDTO userUpdateDTO) {
        userService.updateUser(userUuid, userUpdateDTO);
        return ResponseEntity.ok("User updated successfully");
    }

    @PatchMapping("/uuid/{userUuid}/set-new-password")
    public ResponseEntity<String> setNewPassword(@Valid @PathVariable String userUuid, @RequestBody SetNewPasswordRequest setNewPasswordRequest) {
        userService.forgotPassword(userUuid, setNewPasswordRequest);
        return ResponseEntity.ok("Password changed successfully");
    }

    @PatchMapping("/uuid/{userUuid}/update-password")
    public ResponseEntity<String> updatePassword(@Valid @PathVariable String userUuid, @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        userService.updatePassword(userUuid, updatePasswordRequest);
        return ResponseEntity.ok("Password changed successfully");
    }

    @DeleteMapping("/uuid/{userUuid}")
    public ResponseEntity<String> deleteUserByUuid(@PathVariable String userUuid) {
        userService.deleteUser(userUuid);
        return ResponseEntity.ok("User deleted successfully");
    }
}
