package software.kasunkavinda.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.kasunkavinda.dao.BranchRepo;
import software.kasunkavinda.dao.EmployeeRepo;
import software.kasunkavinda.dto.EmployeeDTO;
import software.kasunkavinda.entity.BranchEntity;
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
    private final BranchRepo branchRepo;
    private final Mapping mapper;

    @Override
    public String saveEmployee(EmployeeDTO employeeDTO) {

        System.out.println(employeeDTO.getEmployee_id());
        System.out.println(employeeDTO.getBranchId());
        // Validate the employee ID
        if (employeeDTO.getEmployee_id() == null) {
            throw new IllegalArgumentException("The given id must not be null");
        }

        boolean optional = employeeRepo.existsById(employeeDTO.getEmployee_id());
        boolean emailExists = employeeRepo.existsByEmail(employeeDTO.getEmail());
        if (optional){
            return "Employee already exists";
        } else if (emailExists){
            return "Email already exists";
        } else {
            BranchEntity branchEntity = branchRepo.getReferenceById(employeeDTO.getBranchId());
            EmployeeEntity employeeEntity = mapper.toEmployeeEntity(employeeDTO);
            employeeEntity.setBranch(branchEntity);
            employeeRepo.save(employeeEntity);
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
    public List<EmployeeDTO> getAllEmployee(String branchId){
        return mapper.toEmployeeDtoList(employeeRepo.findAllByBranchId(branchId));
    }

    @Override
    public String updateEmployee(EmployeeDTO employeeDTO) {

        // Check if the employee exists
        Optional<EmployeeEntity> existingEmployeeOpt = employeeRepo.findById(employeeDTO.getEmployee_id());
        if (!existingEmployeeOpt.isPresent()) {
            return "Employee not found";
        }

        EmployeeEntity existingEmployee = existingEmployeeOpt.get();

        // Check if the new email is different and if it already exists in the database
        if (!existingEmployee.getEmail().equals(employeeDTO.getEmail())) {
            boolean emailExists = employeeRepo.existsByEmail(employeeDTO.getEmail());
            if (emailExists) {
                return "Email already exists";
            }
        }

        // Update the employee entity with new values

        BranchEntity branchEntity = branchRepo.getReferenceById(employeeDTO.getBranchId());
        EmployeeEntity updatedEmployee = mapper.toEmployeeEntity(employeeDTO);
        updatedEmployee.setEmployee_id(existingEmployee.getEmployee_id()); // Ensure the ID remains the same
        updatedEmployee.setBranch(branchEntity);
        employeeRepo.save(updatedEmployee);

        return "Employee updated successfully";
    }

}
