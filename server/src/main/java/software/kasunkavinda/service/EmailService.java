package software.kasunkavinda.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public interface EmailService {

    public String sendVerificationCode(String recipientEmail);
}
