package software.kasunkavinda.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.kasunkavinda.dao.CustomerRepo;
import software.kasunkavinda.dto.CustomerDTO;
import software.kasunkavinda.entity.CustomerEntity;
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
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        return mapper.toCustomerDTO(customerRepo.save(mapper.toCustomerEntity(customerDTO)));
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
    public void updateCustomer( CustomerDTO customerDTO) {
        customerRepo.save(mapper.toCustomerEntity(customerDTO));
    }
}
