package software.kasunkavinda.controller;

import lombok.RequiredArgsConstructor;
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

    private final CustomerService customerService;
    private final ResponseDTO responseDTO;

    @GetMapping("/health")
    public String healthTest() {
        return "Customer Health Good";
    }

    @PostMapping("/save")
    public ResponseEntity saveCustomer(@RequestBody CustomerDTO customerDTO){
        String opt = customerService.saveCustomer(customerDTO);
        if (opt.equals("Customer already exists")){
            responseDTO.setCode("400");
            responseDTO.setMessage("Customer already exists");
            responseDTO.setContent(customerDTO);
            return new ResponseEntity(responseDTO, HttpStatus.MULTI_STATUS);
        }else if (opt.equals("Email already exists")){
            responseDTO.setCode("400");
            responseDTO.setMessage("Email already exists");
            responseDTO.setContent(customerDTO);
            return new ResponseEntity(responseDTO, HttpStatus.MULTI_STATUS);
        }
        else {
            responseDTO.setCode("200");
            responseDTO.setMessage("Customer saved successfully");
            responseDTO.setContent(customerDTO);
            return new ResponseEntity(responseDTO, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public CustomerDTO getCustomerById(@PathVariable String id){
        return customerService.getSelectedCustomer(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCustomer(@PathVariable String id){
        customerService.deleteCustomer(id);
    }

    @GetMapping
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateCustomer(@RequestBody CustomerDTO customerDTO) {
        String resp = customerService.updateCustomer(customerDTO);
        if (resp.equals("Email already exists")) {
            responseDTO.setCode("400");
            responseDTO.setMessage("Email already exists");
            responseDTO.setContent(customerDTO);
            return new ResponseEntity(responseDTO, HttpStatus.MULTI_STATUS);
        } else {
            responseDTO.setCode("200");
            responseDTO.setMessage("Customer updated successfully");
            responseDTO.setContent(customerDTO);
            return new ResponseEntity(responseDTO, HttpStatus.OK);
        }
    }
}
