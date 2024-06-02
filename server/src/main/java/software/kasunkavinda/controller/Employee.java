package software.kasunkavinda.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import software.kasunkavinda.dto.EmployeeDTO;
import software.kasunkavinda.dto.ResponseDTO;
import software.kasunkavinda.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("api/v1/employee")
@RequiredArgsConstructor
public class Employee {

    private static final Logger logger = LoggerFactory.getLogger(Employee.class);

    private final EmployeeService employeeService;
    private final ResponseDTO responseDTO;

    @GetMapping("/health")
    public ResponseEntity<String> healthTest() {
        logger.info("Health test endpoint called");
        return new ResponseEntity<>("Employee Health Good", HttpStatus.OK);
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        logger.info("Saving employee details");

        if (employeeDTO.getEmployee_id() == null) {
            logger.warn("Employee ID is null");
            return ResponseEntity.badRequest().body(null);
        }

        try {
            String opt = employeeService.saveEmployee(employeeDTO);

            if (opt.equals("Employee already exists")) {
                responseDTO.setCode("400");
                responseDTO.setMessage("Employee already exists");
                responseDTO.setContent(employeeDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.MULTI_STATUS);
            } else if (opt.equals("Email already exists")) {
                responseDTO.setCode("400");
                responseDTO.setMessage("Email already exists");
                responseDTO.setContent(employeeDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.MULTI_STATUS);
            } else {
                responseDTO.setCode("200");
                responseDTO.setMessage("Employee saved successfully");
                responseDTO.setContent(employeeDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            }
        } catch (Exception exception) {
            logger.error("Error saving employee: ", exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable String id) {
        logger.info("Fetching employee with ID: {}", id);
        try {
            EmployeeDTO employeeDTO = employeeService.getSelectedEmployee(id);
            return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("Error fetching employee by ID: {}", id, exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> deleteEmployee(@PathVariable String id) {
        logger.info("Deleting employee with ID: {}", id);
        try {
            employeeService.deleteEmployee(id);
            responseDTO.setCode("200");
            responseDTO.setMessage("Employee deleted successfully");
            responseDTO.setContent(null);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("Error deleting employee: ", id, exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("getAll/{branchId}")
    public ResponseEntity<?> getAllEmployee(@PathVariable String branchId) {
        logger.info("Fetching all employees for branch ID: {}", branchId);
        try {
            List<EmployeeDTO> employees = employeeService.getAllEmployee(branchId);
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("Error fetching all employees for branch ID: {}", branchId, exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
        logger.info("Updating employee details");

        try {
            String resp = employeeService.updateEmployee(employeeDTO);
            if (resp.equals("Email already exists")) {
                responseDTO.setCode("400");
                responseDTO.setMessage("Email already exists");
                responseDTO.setContent(employeeDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.MULTI_STATUS);
            } else {
                responseDTO.setCode("200");
                responseDTO.setMessage("Employee updated successfully");
                responseDTO.setContent(employeeDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            }
        } catch (Exception exception) {
            logger.error("Error updating employee: ", exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
