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
import software.kasunkavinda.dto.InventoryDTO;
import software.kasunkavinda.dto.ResponseDTO;
import software.kasunkavinda.service.EmployeeService;
import software.kasunkavinda.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("api/v1/inventory")
@RequiredArgsConstructor
public class Inventory {

    private static final Logger logger = LoggerFactory.getLogger(Inventory.class);

    private final InventoryService inventoryService;
    private final ResponseDTO responseDTO;

    @GetMapping("/health")
    public ResponseEntity<String> healthTest() {
        logger.info("Health test endpoint called");
        return new ResponseEntity<>("Inventory Health Good", HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO> saveInventory(@RequestBody InventoryDTO inventoryDTO) {
        logger.info("Saving inventory item");
        try {
            String opt = inventoryService.saveItem(inventoryDTO);
            if (opt.equals("Item already exists")) {
                responseDTO.setCode("400");
                responseDTO.setMessage("Item already exists");
                responseDTO.setContent(inventoryDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.MULTI_STATUS);
            } else {
                responseDTO.setCode("200");
                responseDTO.setMessage("Item saved successfully");
                responseDTO.setContent(inventoryDTO);
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            }
        } catch (Exception exception) {
            logger.error("Error saving inventory item: ", exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllInventory() {
        logger.info("Fetching all inventory items");
        try {
            List<InventoryDTO> inventoryList = inventoryService.getAllItems();
            return new ResponseEntity<>(inventoryList, HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("Error fetching all inventory items: ", exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getItemById(@PathVariable String id) {
        logger.info("Fetching inventory item with ID: {}", id);
        try {
            InventoryDTO inventoryDTO = inventoryService.getSelectedItem(id);
            return new ResponseEntity<>(inventoryDTO, HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("Error fetching inventory item by ID: ", id, exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> updateInventory(@RequestBody InventoryDTO inventoryDTO) {
        logger.info("Updating inventory item");
        try {
            inventoryService.updateItem(inventoryDTO);
            responseDTO.setCode("200");
            responseDTO.setMessage("Item updated successfully");
            responseDTO.setContent(inventoryDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("Error updating inventory item: ", exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> deleteInventory(@PathVariable String id) {
        logger.info("Deleting inventory item with ID: {}", id);
        try {
            inventoryService.deleteItem(id);
            responseDTO.setCode("200");
            responseDTO.setMessage("Item deleted successfully");
            responseDTO.setContent(null);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception exception) {
            logger.error("Error deleting inventory item: ", id, exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
