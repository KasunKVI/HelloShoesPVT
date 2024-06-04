package software.kasunkavinda.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.kasunkavinda.dao.BranchRepo;
import software.kasunkavinda.dao.EmployeeRepo;
import software.kasunkavinda.dto.EmployeeDTO;
import software.kasunkavinda.entity.BranchEntity;
import software.kasunkavinda.entity.EmployeeEntity;
import software.kasunkavinda.exception.NotFoundException;
import software.kasunkavinda.exception.QuantityExceededException;
import software.kasunkavinda.service.EmployeeService;
import software.kasunkavinda.util.Mapping;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepo employeeRepo;
    private final BranchRepo branchRepo;
    private final Mapping mapper;

    @Override
    public String saveEmployee(EmployeeDTO employeeDTO) {
        logger.info("Saving employee with ID: {}", employeeDTO.getEmployee_id());

        // Validate the employee ID
        if (employeeDTO.getEmployee_id() == null) {
            logger.error("The given ID must not be null");
            throw new IllegalArgumentException("The given ID must not be null");
        }

        boolean employeeExists = employeeRepo.existsById(employeeDTO.getEmployee_id());
        boolean emailExists = employeeRepo.existsByEmail(employeeDTO.getEmail());
        if (employeeExists) {
            logger.warn("Employee already exists with ID: {}", employeeDTO.getEmployee_id());
            throw new QuantityExceededException("Employee already exists");
        } else if (emailExists) {
            logger.warn("Email already exists: {}", employeeDTO.getEmail());
            throw new QuantityExceededException("Email already exists");
        } else {
            BranchEntity branchEntity = branchRepo.getReferenceById(employeeDTO.getBranchId());
            EmployeeEntity employeeEntity = mapper.toEmployeeEntity(employeeDTO);
            employeeEntity.setBranch(branchEntity);
            employeeRepo.save(employeeEntity);
            logger.info("Employee saved successfully with ID: {}", employeeDTO.getEmployee_id());
            return "Employee saved successfully";
        }
    }

    @Override
    public void deleteEmployee(String employeeId) {
        logger.info("Deleting employee with ID: {}", employeeId);
        employeeRepo.deleteById(employeeId);
        logger.info("Employee deleted successfully with ID: {}", employeeId);
    }

    @Override
    public EmployeeDTO getSelectedEmployee(String employeeId) {
        logger.info("Fetching employee with ID: {}", employeeId);
        Optional<EmployeeEntity> employeeOpt = employeeRepo.findById(employeeId);
        if (employeeOpt.isPresent()) {
            EmployeeEntity employee = employeeOpt.get();
            return mapper.toEmployeeDTO(employee);
        } else {
            throw new NotFoundException("Employee not found with ID: " + employeeId);
        }
    }

    @Override
    public List<EmployeeDTO> getAllEmployee(String branchId) {
        logger.info("Fetching all employees for branch ID: {}", branchId);
        List<EmployeeEntity> employees = employeeRepo.findAllByBranchId(branchId);
        if (!employees.isEmpty()) {
            return mapper.toEmployeeDtoList(employees);
        } else {
            throw new NotFoundException("No employees found for branch ID: " + branchId);
        }
    }

    @Override
    public String updateEmployee(EmployeeDTO employeeDTO) {
        logger.info("Updating employee with ID: {}", employeeDTO.getEmployee_id());

        // Check if the employee exists
        Optional<EmployeeEntity> existingEmployeeOpt = employeeRepo.findById(employeeDTO.getEmployee_id());
        if (existingEmployeeOpt.isPresent()) {
            EmployeeEntity existingEmployee = existingEmployeeOpt.get();

            // Check if the new email is different and if it already exists in the database
            if (!existingEmployee.getEmail().equals(employeeDTO.getEmail())) {
                boolean emailExists = employeeRepo.existsByEmail(employeeDTO.getEmail());
                if (emailExists) {
                    logger.warn("Email already exists: {}", employeeDTO.getEmail());
                    throw new QuantityExceededException("Email already exists");
                }
            }

            // Update the employee entity with new values
            BranchEntity branchEntity = branchRepo.getReferenceById(employeeDTO.getBranchId());
            EmployeeEntity updatedEmployee = mapper.toEmployeeEntity(employeeDTO);
            updatedEmployee.setEmployee_id(existingEmployee.getEmployee_id()); // Ensure the ID remains the same
            updatedEmployee.setBranch(branchEntity);
            employeeRepo.save(updatedEmployee);

            logger.info("Employee updated successfully with ID: {}", employeeDTO.getEmployee_id());
            return "Employee updated successfully";
        } else {
            throw new NotFoundException("Employee not found with ID: " + employeeDTO.getEmployee_id());
        }
    }
}
