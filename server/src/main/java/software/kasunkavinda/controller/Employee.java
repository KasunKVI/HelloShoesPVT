package software.kasunkavinda.controller;

import lombok.RequiredArgsConstructor;
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

    private final EmployeeService employeeService;
    private final ResponseDTO responseDTO;

    @GetMapping("/health")
    public String healthTest() {
        return "Employee Health Good";
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity saveEmployee(@RequestBody EmployeeDTO employeeDTO){

        if (employeeDTO.getEmployee_id() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        String opt = employeeService.saveEmployee(employeeDTO);
        
        if (opt.equals("Employee already exists")){
            responseDTO.setCode("400");
            responseDTO.setMessage("Employee already exists");
            responseDTO.setContent(employeeDTO);
            return new ResponseEntity(responseDTO, HttpStatus.MULTI_STATUS);
        }else if (opt.equals("Email already exists")){
            responseDTO.setCode("400");
            responseDTO.setMessage("Email already exists");
            responseDTO.setContent(employeeDTO);
            return new ResponseEntity(responseDTO, HttpStatus.MULTI_STATUS);
        }
        else {
            responseDTO.setCode("200");
            responseDTO.setMessage("Employee saved successfully");
            responseDTO.setContent(employeeDTO);
            return new ResponseEntity(responseDTO, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public EmployeeDTO getEmployeeById(@PathVariable String id){
        return employeeService.getSelectedEmployee(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteEmployee(@PathVariable String id){
        employeeService.deleteEmployee(id);
    }

    @GetMapping("getAll/{branchId}")
    public List<EmployeeDTO> getAllEmployee(@PathVariable String branchId) {
        return employeeService.getAllEmployee(branchId);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateEmployee(@RequestBody EmployeeDTO employeeDTO){

        String resp = employeeService.updateEmployee(employeeDTO);
        if (resp.equals("Email already exists")) {
            responseDTO.setCode("400");
            responseDTO.setMessage("Email already exists");
            responseDTO.setContent(employeeDTO);
            return new ResponseEntity(responseDTO, HttpStatus.MULTI_STATUS);
        } else {
            responseDTO.setCode("200");
            responseDTO.setMessage("Employee updated successfully");
            responseDTO.setContent(employeeDTO);
            return new ResponseEntity(responseDTO, HttpStatus.OK);
        }
    }

}
