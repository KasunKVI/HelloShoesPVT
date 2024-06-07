package software.kasunkavinda.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import software.kasunkavinda.dto.CustomerDTO;
import software.kasunkavinda.dto.EmployeeDTO;
import software.kasunkavinda.dto.ResponseDTO;
import software.kasunkavinda.dto.SupplierDTO;
import software.kasunkavinda.entity.SuperEntity;
import software.kasunkavinda.entity.SupplierEntity;
import software.kasunkavinda.service.SupplierService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/supplier")
@RequiredArgsConstructor
public class Supplier  {

    private static final Logger logger = LoggerFactory.getLogger(Supplier.class);

    private final SupplierService supplierService;
    private final ResponseDTO responseDTO;

    @GetMapping("/health")
    public ResponseEntity<String> healthTest() {
        logger.info("Health test endpoint called");
        return new ResponseEntity<>("Supplier Health Good", HttpStatus.OK);
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> saveSupplier(@RequestBody SupplierDTO supplierDTO) {
        logger.info("Saving supplier details");
        try {
            String opt = supplierService.saveSupplier(supplierDTO);
            if (opt.equals("Supplier already exists")) {
                responseDTO.setCode("400");
                responseDTO.setMessage("Supplier already exists");
                responseDTO.setContent(supplierDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.MULTI_STATUS);
            } else if (opt.equals("Email already exists")) {
                responseDTO.setCode("400");
                responseDTO.setMessage("Email already exists");
                responseDTO.setContent(supplierDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.MULTI_STATUS);
            } else {
                responseDTO.setCode("200");
                responseDTO.setMessage("Supplier saved successfully");
                responseDTO.setContent(supplierDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            }
        } catch (Exception exception) {
            logger.error("Error saving supplier: ", exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/latest")
    public ResponseEntity<?> getLatestSupplierId() {
        logger.info("Fetching latest supplier ID");
        try {
            String latestId = supplierService.getLatestSupplierId();
            return ResponseEntity.ok(latestId);
        } catch (Exception exception) {
            logger.error("Error fetching latest supplier ID: ", exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSelectedSupplier(@PathVariable String id) {
        logger.info("Fetching supplier with ID: {}", id);
        try {
            SupplierDTO supplier = supplierService.getSelectedSupplier(id);
            return ResponseEntity.ok(supplier);
        } catch (Exception exception) {
            logger.error("Error fetching supplier by ID: ", exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteSupplier(@PathVariable String id) {
        logger.info("Deleting supplier with ID: {}", id);
        try {
            supplierService.deleteSupplier(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception exception) {
            logger.error("Error deleting supplier: ", exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllSuppliers() {
        logger.info("Fetching all suppliers");
        try {
            List<SupplierDTO> suppliers = supplierService.getAllSuppliers();
            return ResponseEntity.ok(suppliers);
        } catch (Exception exception) {
            logger.error("Error fetching all suppliers: ", exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateSupplier(@RequestBody SupplierDTO supplierDTO) {
        logger.info("Updating supplier");
        try {
            String resp = supplierService.updateSupplier(supplierDTO);
            if (resp.equals("Email already exists")) {
                responseDTO.setCode("400");
                responseDTO.setMessage("Email already exists");
                responseDTO.setContent(supplierDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.MULTI_STATUS);
            } else {
                responseDTO.setCode("200");
                responseDTO.setMessage("Supplier updated successfully");
                responseDTO.setContent(supplierDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            }
        } catch (Exception exception) {
            logger.error("Error updating supplier: ", exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/ids")
    public ResponseEntity<?> getAllSupplierIds() {
        logger.info("Fetching all supplier IDs");
        try {
            List<String> supplierIds = supplierService.getAllSupplierIds();
            return ResponseEntity.ok(supplierIds);
        } catch (Exception exception) {
            logger.error("Error fetching all supplier IDs: ", exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/count-by-category")
    public ResponseEntity<Map<String, Long>> getSuppliersCount() {
        logger.info("Fetching suppliers count");
        try {
            Map<String, Long> counts = new HashMap<>();
            counts.put("international", supplierService.getInternationalSuppliersCount());
            counts.put("local", supplierService.getLocalSuppliersCount());
            return new ResponseEntity<>(counts, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching suppliers count", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
