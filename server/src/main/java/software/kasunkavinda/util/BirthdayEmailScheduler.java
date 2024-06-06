package software.kasunkavinda.util;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.kasunkavinda.dao.CustomerRepo;
import software.kasunkavinda.entity.CustomerEntity;
import software.kasunkavinda.service.EmailService;


import java.util.List;

@Component
@RequiredArgsConstructor
public class BirthdayEmailScheduler {

    private static final Logger logger = LoggerFactory.getLogger(BirthdayEmailScheduler.class);

    private final CustomerRepo customerRepo;

    private final EmailService emailService;

    @Scheduled(cron = "0 0 8 * * *")
    public void sendBirthdayEmails() {
        logger.info("Running birthday email scheduler");
        List<CustomerEntity> birthdayCustomers = customerRepo.findByDob();

        for (CustomerEntity customer : birthdayCustomers) {
            String subject = "Happy Birthday!";
            String body = "Dear " + customer.getName() + ",\n\nWishing you a very happy birthday!\n\nBest regards,\nHello Shoes Pvt. Ltd";
            emailService.sendEmail(customer.getEmail(), subject, body);
            logger.info("Sent birthday email to: " + customer.getEmail());
        }
    }
}
