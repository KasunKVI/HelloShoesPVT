package software.kasunkavinda.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.kasunkavinda.service.EmailService;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailServiceImpl implements EmailService {


    private final JavaMailSender mailSender;

    public String sendVerificationCode(String recipientEmail) {

        // Generate random 5-digit verification code
        String verificationCode = generateVerificationCode();

        // HTML content for the email
        String htmlContent = "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>Email Verification</title>" +
                "    <style>" +
                "        body {" +
                "            font-family: Arial, sans-serif;" +
                "            margin: 0;" +
                "            padding: 0;" +
                "            background-color: #f5f5f5;" +
                "        }" +
                "        .container {" +
                "            max-width: 600px;" +
                "            margin: 20px auto;" +
                "            background-color: #ffffff;" +
                "            padding: 30px;" +
                "            border-radius: 10px;" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);" +
                "        }" +
                "        h1 {" +
                "            color: #333333;" +
                "        }" +
                "        p {" +
                "            color: #666666;" +
                "        }" +
                "        .verification-code {" +
                "            font-size: 24px;" +
                "            font-weight: bold;" +
                "            color: #009688;" +
                "            margin-top: 20px;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <h1>Hello from Shoe Pvt Ltd</h1>" +
                "        <p>Your verification code is:</p>" +
                "        <p class=\"verification-code\">" + verificationCode + "</p>" +
                "    </div>" +
                "</body>" +
                "</html>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(recipientEmail);
            helper.setSubject("Email Verification Code");
            helper.setText(htmlContent, true); // true indicates HTML content

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return verificationCode;
    }


    private String generateVerificationCode() {
        Random random = new Random();
        int min = 10000;
        int max = 99999;
        return String.valueOf(random.nextInt(max - min + 1) + min);
    }



}