package software.kasunkavinda.service;

import software.kasunkavinda.dto.CustomerDTO;
import software.kasunkavinda.dto.EmployeeDTO;
import software.kasunkavinda.entity.CustomerEntity;
import software.kasunkavinda.entity.EmployeeEntity;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    String saveEmployee(EmployeeDTO employeeDTO);
    void deleteEmployee(String employeeId);
    EmployeeDTO getSelectedEmployee(String employeeId);
    List<EmployeeDTO> getAllEmployee();
    String updateEmployee(EmployeeDTO employeeDTO);
}
