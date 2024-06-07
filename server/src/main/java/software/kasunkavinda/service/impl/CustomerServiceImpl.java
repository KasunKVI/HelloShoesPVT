package software.kasunkavinda.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.kasunkavinda.dao.CustomerRepo;
import software.kasunkavinda.dto.CustomerDTO;
import software.kasunkavinda.entity.CustomerEntity;
import software.kasunkavinda.exception.GlobalExceptionHandler;
import software.kasunkavinda.exception.NotFoundException;
import software.kasunkavinda.exception.QuantityExceededException;
import software.kasunkavinda.service.CustomerService;
import software.kasunkavinda.util.Mapping;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepo customerRepo;
    private final Mapping mapper;

    @Override
    public String saveCustomer(CustomerDTO customerDTO) {
        logger.info("Attempting to save customer: {}", customerDTO.getCustomer_id());
        boolean opt = customerRepo.existsById(customerDTO.getCustomer_id());
        boolean emailExists = customerRepo.existsByEmail(customerDTO.getEmail());
        if (opt) {
            logger.warn("Customer already exists: {}", customerDTO.getCustomer_id());
            throw new QuantityExceededException("Customer already exists");
        } else if (emailExists) {
            logger.warn("Email already exists: {}", customerDTO.getEmail());
            throw new QuantityExceededException("Email already exists");
        } else {
            mapper.toCustomerDTO(customerRepo.save(mapper.toCustomerEntity(customerDTO)));
            logger.info("Customer saved successfully: {}", customerDTO.getCustomer_id());
            return "Customer saved successfully";
        }
    }

    @Override
    public void deleteCustomer(String customerId) {
        logger.info("Attempting to delete customer: {}", customerId);
        customerRepo.deleteById(customerId);
        logger.info("Customer deleted successfully: {}", customerId);
    }

    @Override
    public CustomerDTO getSelectedCustomer(String customerId) {
        logger.info("Fetching customer: {}", customerId);
        CustomerEntity customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));
        return mapper.toCustomerDTO(customer);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        logger.info("Fetching all customers");
        return mapper.toCustomerDTOList(customerRepo.findAll());
    }

    @Override
    public String updateCustomer(CustomerDTO customerDTO) {
        logger.info("Attempting to update customer: {}", customerDTO.getCustomer_id());

        Optional<CustomerEntity> existingCustomerOpt = customerRepo.findById(customerDTO.getCustomer_id());
        if (!existingCustomerOpt.isPresent()) {
            logger.warn("Customer not found: {}", customerDTO.getCustomer_id());
            throw new NotFoundException("Customer not found");
        }

        CustomerEntity existingCustomer = existingCustomerOpt.get();

        // Check if the new email is different and if it already exists in the database
        if (!existingCustomer.getEmail().equals(customerDTO.getEmail())) {
            boolean emailExists = customerRepo.existsByEmail(customerDTO.getEmail());
            if (emailExists) {
                logger.warn("Email already exists: {}", customerDTO.getEmail());
                throw new QuantityExceededException("Email already exists");
            }
        }

        // Update the customer entity with new values
        CustomerEntity updateCustomer = mapper.toCustomerEntity(customerDTO);
        updateCustomer.setCustomer_id(existingCustomer.getCustomer_id()); // Ensure the ID remains the same

        customerRepo.save(updateCustomer);
        logger.info("Customer updated successfully: {}", customerDTO.getCustomer_id());
        return "Customer updated successfully";
    }

    @Override
    public Long getTodayCustomers() {
        Date today = new Date();
        return customerRepo.countCustomersJoinedToday(today);
    }

    @Override
    public List<Object[]> getWeeklyCustomers() {
        return customerRepo.countWeeklyCustomers();
    }
}
