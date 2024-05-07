package software.kasunkavinda.service;

import software.kasunkavinda.dto.CustomerDTO;
import software.kasunkavinda.entity.CustomerEntity;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    void deleteCustomer(String customerId);
    CustomerDTO getSelectedCustomer(String customerId);
    List<CustomerDTO> getAllCustomers();
    void updateCustomer(CustomerDTO customerDTO);

}