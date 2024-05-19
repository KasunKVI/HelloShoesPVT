package software.kasunkavinda.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.kasunkavinda.dao.CustomerRepo;
import software.kasunkavinda.dao.EmployeeRepo;
import software.kasunkavinda.dto.EmployeeDTO;
import software.kasunkavinda.entity.EmployeeEntity;
import software.kasunkavinda.service.EmployeeService;
import software.kasunkavinda.util.Mapping;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final Mapping mapper;

    @Override
    public String saveEmployee(EmployeeDTO employeeDTO) {
        boolean optional = employeeRepo.existsById(employeeDTO.getEmployee_id());
        boolean emailExists = employeeRepo.existsByEmail(employeeDTO.getEmail());
        if (optional){
            return "Employee already exists";
        } else if (emailExists){
            return "Email already exists";
        } else {
            employeeRepo.save(mapper.toEmployeeEntity(employeeDTO));
            return "Employee saved successfully";
        }

    }

    @Override
    public void deleteEmployee(String employeeId) {
        employeeRepo.deleteById(employeeId);
    }

    @Override
    public EmployeeDTO getSelectedEmployee(String employeeId) {
        return mapper.toEmployeeDTO(employeeRepo.getReferenceById(employeeId));

    }

    @Override
    public List<EmployeeDTO> getAllEmployee() {
        return mapper.toEmployeeDtoList(employeeRepo.findAll());
    }

    @Override
    public String updateEmployee(EmployeeDTO employeeDTO) {
        boolean emailExists = employeeRepo.existsByEmail(employeeDTO.getEmail());
        if (emailExists){
            return "Email already exists";
        } else {
            employeeRepo.save(mapper.toEmployeeEntity(employeeDTO));
            return "Employee updated successfully";
        }
    }
}
