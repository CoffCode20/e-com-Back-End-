package co.istad.ishop.service;

import co.istad.ishop.entities.Otp;
import co.istad.ishop.entities.User;
import co.istad.ishop.exception.ApiException;
import co.istad.ishop.repository.OtpRepository;
import co.istad.ishop.repository.UserRepository;
import co.istad.ishop.security.otp_email.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final OtpRepository otpRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void generateAndSendOtp(String email) throws MessagingException {
        String otp = OtpUtil.generateOtp(6);
        String otpHash = passwordEncoder.encode(otp);

        Otp otpEntity = new Otp();
        otpEntity.setOtp(otpHash);
        otpEntity.setEmail(email);
        otpEntity.setCreatedAt(LocalDateTime.now());
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(2));
        otpRepository.save(otpEntity);

        emailService.sentOptEmail(email, otp);
    }

    public void verifyOtp(String otp) {
        Otp otpEntity = otpRepository.findAll().stream()
                .filter(o -> passwordEncoder.matches(otp, o.getOtp()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or Expired OTP"));
        if (otpEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            otpRepository.delete(otpEntity);
        }
        User user = userRepository.findByEmail(otpEntity.getEmail());
        if(user == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "User not found for email: " + otpEntity.getEmail());
        }
        user.setIsVerified(true);
        userRepository.save(user);
        otpRepository.delete(otpEntity);
    }
}