package software.kasunkavinda.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.kasunkavinda.dao.CustomerRepo;
import software.kasunkavinda.dto.CustomerDTO;
import software.kasunkavinda.entity.CustomerEntity;
import software.kasunkavinda.entity.EmployeeEntity;
import software.kasunkavinda.service.CustomerService;
import software.kasunkavinda.util.Mapping;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepo customerRepo;
    private final Mapping mapper;

    @Override
    public String saveCustomer(CustomerDTO customerDTO) {
        boolean opt = customerRepo.existsById(customerDTO.getCustomer_id());
        boolean emailExists = customerRepo.existsByEmail(customerDTO.getEmail());
        if (opt) {
            return "Customer already exists";
        }else if (emailExists) {
            return "Email already exists";
        }else {
            mapper.toCustomerDTO(customerRepo.save(mapper.toCustomerEntity(customerDTO)));
            return "Customer saved successfully";
        }

    }

    @Override
    public void deleteCustomer(String customerId) {
        customerRepo.deleteById(customerId);
    }

    @Override
    public CustomerDTO getSelectedCustomer(String customerId) {
        return mapper.toCustomerDTO(customerRepo.getReferenceById(customerId));
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return mapper.toCustomerDTOList(customerRepo.findAll());
    }

    @Override
    public String updateCustomer(CustomerDTO customerDTO) {

        Optional<CustomerEntity> existingCustomerOpt = customerRepo.findById(customerDTO.getCustomer_id());
        if (!existingCustomerOpt.isPresent()) {
            return "Customer not found";
        }

        CustomerEntity existingCustomer = existingCustomerOpt.get();

        // Check if the new email is different and if it already exists in the database
        if (!existingCustomer.getEmail().equals(customerDTO.getEmail())) {
            boolean emailExists = customerRepo.existsByEmail(customerDTO.getEmail());
            if (emailExists) {
                return "Email already exists";
            }
        }

        // Update the employee entity with new values
        CustomerEntity updateCustomer = mapper.toCustomerEntity(customerDTO);
        updateCustomer.setCustomer_id(existingCustomer.getCustomer_id()); // Ensure the ID remains the same

        customerRepo.save(updateCustomer);
        return "Customer updated successfully";
    }

}
