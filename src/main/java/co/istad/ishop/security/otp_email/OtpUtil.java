package co.istad.ishop.security.otp_email;

import java.security.SecureRandom;

public class OtpUtil {
    public static String generateOtp(int length) {
        String numberToGen = "0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(numberToGen.charAt(random.nextInt(numberToGen.length())));
        }
        return otp.toString();
    }
}
