package software.kasunkavinda.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import software.kasunkavinda.dto.CustomerDTO;
import software.kasunkavinda.dto.ResponseDTO;
import software.kasunkavinda.entity.CustomerEntity;
import software.kasunkavinda.service.CustomerService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/customer")
@RequiredArgsConstructor
public class Customer {


    private static final Logger logger = LoggerFactory.getLogger(Customer.class);

    private final CustomerService customerService;
    private final ResponseDTO responseDTO;

    @GetMapping("/health")
    public ResponseEntity<String> healthTest() {
        logger.info("Health test endpoint called");
        return new ResponseEntity<>("Customer Health Good", HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO> saveCustomer(@RequestBody CustomerDTO customerDTO) {
        logger.info("Saving customer details");
        try {
            String opt = customerService.saveCustomer(customerDTO);
            if (opt.equals("Customer already exists")) {
                responseDTO.setCode("400");
                responseDTO.setMessage("Customer already exists");
                responseDTO.setContent(customerDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.MULTI_STATUS);
            } else if (opt.equals("Email already exists")) {
                responseDTO.setCode("400");
                responseDTO.setMessage("Email already exists");
                responseDTO.setContent(customerDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.MULTI_STATUS);
            } else {
                responseDTO.setCode("200");
                responseDTO.setMessage("Customer saved successfully");
                responseDTO.setContent(customerDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            }
        } catch (Exception exception) {
            logger.error("Error saving customer: ", exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable String id) {
        logger.info("Fetching customer with ID: {}", id);
        try {
            CustomerDTO customerDTO = customerService.getSelectedCustomer(id);
            return new ResponseEntity<>(customerDTO, HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("Error fetching customer by ID: {}", id, exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> deleteCustomer(@PathVariable String id) {
        logger.info("Deleting customer with ID: {}", id);
        try {
            customerService.deleteCustomer(id);
            responseDTO.setCode("200");
            responseDTO.setMessage("Customer deleted successfully");
            responseDTO.setContent(null);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("Error deleting customer: ", id, exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCustomers() {
        logger.info("Fetching all customers");
        try {
            List<CustomerDTO> customersList = customerService.getAllCustomers();
            return new ResponseEntity<>(customersList, HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("Error fetching all customers: ", exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> updateCustomer(@RequestBody CustomerDTO customerDTO) {
        logger.info("Updating customer details");
        try {
            String resp = customerService.updateCustomer(customerDTO);
            if (resp.equals("Email already exists")) {
                responseDTO.setCode("400");
                responseDTO.setMessage("Email already exists");
                responseDTO.setContent(customerDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.MULTI_STATUS);
            } else {
                responseDTO.setCode("200");
                responseDTO.setMessage("Customer updated successfully");
                responseDTO.setContent(customerDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            }
        } catch (Exception exception) {
            logger.error("Error updating customer: ", exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
