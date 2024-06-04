package software.kasunkavinda.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.kasunkavinda.dao.SupplierRepo;
import software.kasunkavinda.dto.SupplierDTO;
import software.kasunkavinda.entity.SupplierEntity;
import software.kasunkavinda.exception.NotFoundException;
import software.kasunkavinda.exception.QuantityExceededException;
import software.kasunkavinda.service.SupplierService;
import software.kasunkavinda.util.Mapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepo supplierRepo;
    private final Mapping mapper;
    private static final Logger logger = LoggerFactory.getLogger(SupplierServiceImpl.class);

    @Override
    public String saveSupplier(SupplierDTO supplierDTO) {
        logger.info("Attempting to save supplier with ID: {}", supplierDTO.getSupplier_id());
        boolean existsById = supplierRepo.existsById(supplierDTO.getSupplier_id());
        boolean emailExists = supplierRepo.existsByEmail(supplierDTO.getEmail());

        if (existsById) {
            logger.warn("Supplier already exists with ID: {}", supplierDTO.getSupplier_id());
            throw new QuantityExceededException("Supplier already exists");
        } else if (emailExists) {
            logger.warn("Email already exists: {}", supplierDTO.getEmail());
            throw new QuantityExceededException("Email already exists");
        } else {
            supplierRepo.save(mapper.toSupplierEntity(supplierDTO));
            logger.info("Supplier saved successfully with ID: {}", supplierDTO.getSupplier_id());
            return "Supplier saved successfully";
        }
    }

    @Override
    public void deleteSupplier(String supplierId) {
        logger.info("Deleting supplier with ID: {}", supplierId);
        if (!supplierRepo.existsById(supplierId)) {
            logger.warn("Supplier not found with ID: {}", supplierId);
            throw new NotFoundException("Supplier not found with ID: " + supplierId);
        }
        supplierRepo.deleteById(supplierId);
        logger.info("Supplier deleted successfully with ID: {}", supplierId);
    }

    @Override
    public SupplierDTO getSelectedSupplier(String supplierId) {
        logger.info("Fetching supplier with ID: {}", supplierId);
        SupplierEntity supplierEntity = supplierRepo.findById(supplierId)
                .orElseThrow(() -> new NotFoundException("Supplier not found with ID: " + supplierId));
        SupplierDTO supplierDTO = mapper.toSupplierDTO(supplierEntity);
        logger.info("Fetched supplier details: {}", supplierDTO);
        return supplierDTO;
    }

    @Override
    public List<SupplierDTO> getAllSuppliers() {
        logger.info("Fetching all suppliers");
        List<SupplierDTO> supplierDTOs = mapper.toSupplierDtoList(supplierRepo.findAll());
        logger.info("Fetched all suppliers, total count: {}", supplierDTOs.size());
        return supplierDTOs;
    }

    @Override
    public String updateSupplier(SupplierDTO supplierDTO) {
        logger.info("Attempting to update supplier with ID: {}", supplierDTO.getSupplier_id());
        SupplierEntity existingSupplier = supplierRepo.findById(supplierDTO.getSupplier_id())
                .orElseThrow(() -> new NotFoundException("Supplier not found with ID: " + supplierDTO.getSupplier_id()));

        // Check if the new email is different and if it already exists in the database
        if (!existingSupplier.getEmail().equals(supplierDTO.getEmail())) {
            boolean emailExists = supplierRepo.existsByEmail(supplierDTO.getEmail());
            if (emailExists) {
                logger.warn("Email already exists: {}", supplierDTO.getEmail());
                throw new QuantityExceededException("Email already exists");
            }
        }

        // Update the supplier entity with new values
        SupplierEntity updatedSupplier = mapper.toSupplierEntity(supplierDTO);
        updatedSupplier.setSupplier_id(existingSupplier.getSupplier_id()); // Ensure the ID remains the same

        supplierRepo.save(updatedSupplier);
        logger.info("Supplier updated successfully with ID: {}", supplierDTO.getSupplier_id());
        return "Supplier updated successfully";
    }

    @Override
    public String getLatestSupplierId() {
        logger.info("Fetching the latest supplier ID");
        Optional<SupplierEntity> supplier = supplierRepo.findTopByOrderBySupplierIdDesc();
        String latestSupplierId = supplier.map(SupplierEntity::getSupplier_id).orElseThrow(() -> new NotFoundException("No suppliers found"));
        logger.info("Fetched latest supplier ID: {}", latestSupplierId);
        return latestSupplierId;
    }

    @Override
    public List<String> getAllSupplierIds() {
        logger.info("Fetching all supplier IDs");
        List<SupplierEntity> suppliers = supplierRepo.findAll();
        List<String> supplierIds = suppliers.stream()
                .map(SupplierEntity::getSupplier_id)
                .collect(Collectors.toList());
        logger.info("Fetched all supplier IDs, total count: {}", supplierIds.size());
        return supplierIds;
    }
}
