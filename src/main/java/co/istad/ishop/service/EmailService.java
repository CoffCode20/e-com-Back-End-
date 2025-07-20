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
            <head>
                <style>
                    body {
                        font-family: 'Helvetica Neue', Arial, sans-serif;
                        background-color: #f4f4f9;
                        margin: 0;
                        padding: 0;
                    }
                    .container {
                        max-width: 600px;
                        margin: 20px auto;
                        background-color: #ffffff;
                        border-radius: 8px;
                        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                        overflow: hidden;
                    }
                    .header {
                        background-color: #007bff;
                        padding: 20px;
                        text-align: center;
                        color: #ffffff;
                    }
                    .header img {
                        max-width: 150px;
                    }
                    .content {
                        padding: 30px;
                        text-align: center;
                    }
                    .otp {
                        display: inline-block;
                        background-color: #e9ecef;
                        padding: 15px 25px;
                        font-size: 24px;
                        font-weight: bold;
                        color: #343a40;
                        border-radius: 5px;
                        margin: 20px 0;
                        letter-spacing: 2px;
                    }
                    .content p {
                        color: #555555;
                        font-size: 16px;
                        line-height: 1.5;
                    }
                    .footer {
                        background-color: #f8f9fa;
                        padding: 20px;
                        text-align: center;
                        font-size: 14px;
                        color: #6c757d;
                    }
                    .footer a {
                        color: #007bff;
                        text-decoration: none;
                    }
                    @media only screen and (max-width: 600px) {
                        .container {
                            margin: 10px;
                        }
                        .content {
                            padding: 20px;
                        }
                        .otp {
                            font-size: 20px;
                            padding: 10px 20px;
                        }
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <img src="http://localhost:8080/files/08329e33-c9d2-4d5d-a581-15cd0589088d.webp" alt="SecureApp Logo">
                        <h2>OTP Verification</h2>
                    </div>
                    <div class="content">
                        <p>Hello,</p>
                        <p>Your one-time password (OTP) for SecureApp is:</p>
                        <div class="otp">%s</div>
                        <p>This OTP is valid for 2 minutes. Please do not share it with anyone.</p>
                        <p>If you did not request this, please ignore this email or contact our support team.</p>
                    </div>
                    <div class="footer">
                        <p>SecureApp Team</p>
                        <p><small>Never share your password or OTP with anyone.</small></p>
                        <p><a href="https://secureapp.com/support">Contact Support</a></p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(otp);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

}
