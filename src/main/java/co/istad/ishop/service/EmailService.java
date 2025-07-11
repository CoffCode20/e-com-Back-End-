package co.istad.ishop.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sentOptEmail(String to, String otp) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("Your OTP Code - SecureApp");
        String htmlContent = """
            <html>
            <body style="font-family: Arial, sans-serif;">
                <h2>SecureApp OTP Verification</h2>
                <p>Your one-time password (OTP) is: <b>%s</b></p>
                <p>This OTP is valid for 5 minutes. Do not share it with anyone.</p>
                <p>If you did not request this, please ignore this email.</p>
                <footer>
                    <p>SecureApp Team</p>
                    <p><small>Never share your password or OTP with anyone.</small></p>
                </footer>
            </body>
            </html>
            """.formatted(otp);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

}
