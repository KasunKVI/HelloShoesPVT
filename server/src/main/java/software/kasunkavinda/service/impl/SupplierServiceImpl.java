package software.kasunkavinda.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.kasunkavinda.dao.SupplierRepo;
import software.kasunkavinda.dto.SupplierDTO;
import software.kasunkavinda.entity.CustomerEntity;
import software.kasunkavinda.entity.SupplierEntity;
import software.kasunkavinda.service.SupplierService;
import software.kasunkavinda.util.Mapping;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SupplierServiceImpl implements SupplierService {

     private final SupplierRepo supplierRepo;
     private final Mapping mapper;

    @Override
    public String saveSupplier(SupplierDTO supplierDTO) {
        boolean optional = supplierRepo.existsById(supplierDTO.getSupplier_id());
        boolean emailExists = supplierRepo.existsByEmail(supplierDTO.getEmail());
        if (optional){
            return "Supplier already exists";
        } else if (emailExists){
            return "Email already exists";
        } else {
            supplierRepo.save(mapper.toSupplierEntity(supplierDTO));
            return "Supplier saved successfully";
        }
    }

    @Override
    public void deleteSupplier(String supplierId) {
        supplierRepo.deleteById(supplierId);
    }

    @Override
    public SupplierDTO getSelectedSupplier(String supplierId) {
        return mapper.toSupplierDTO(supplierRepo.getReferenceById(supplierId));
    }

    @Override
    public List<SupplierDTO> getAllSuppliers() {
        return mapper.toSupplierDtoList(supplierRepo.findAll());
    }

    @Override
    public String updateSupplier(SupplierDTO supplierDTO) {
        Optional<SupplierEntity> existingSupplierOpt = supplierRepo.findById(supplierDTO.getSupplier_id());
        if (!existingSupplierOpt.isPresent()) {
            return "Supplier not found";
        }

        SupplierEntity existSupplier = existingSupplierOpt.get();

        // Check if the new email is different and if it already exists in the database
        if (!existSupplier.getEmail().equals(supplierDTO.getEmail())) {
            boolean emailExists = supplierRepo.existsByEmail(supplierDTO.getEmail());
            if (emailExists) {
                return "Email already exists";
            }
        }

        // Update the supplier entity with new values
        SupplierEntity updateSupplier = mapper.toSupplierEntity(supplierDTO);
        updateSupplier.setSupplier_id(existSupplier.getSupplier_id()); // Ensure the ID remains the same

        supplierRepo.save(updateSupplier);
        return "Supplier updated successfully";
    }

    @Override
    public String getLatestSupplierId() {
        Optional<SupplierEntity> supplier = supplierRepo.findTopByOrderBySupplierIdDesc();
        return supplier.map(SupplierEntity::getSupplier_id).orElse(null);
    }
}
