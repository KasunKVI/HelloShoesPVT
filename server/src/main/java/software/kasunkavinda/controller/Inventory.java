package software.kasunkavinda.controller;

import lombok.RequiredArgsConstructor;
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

    private final InventoryService inventoryService;
    private final ResponseDTO responseDTO;

    @GetMapping("/health")
    public String healthTest() {
        return "Inventory Health Good";
    }

    @PostMapping("/save")
    public ResponseEntity saveInventory(@RequestBody InventoryDTO inventoryDTO){

        String opt = inventoryService.saveItem(inventoryDTO);
        if (opt.equals("Item already exists")){
            responseDTO.setCode("400");
            responseDTO.setMessage("Item already exists");
            responseDTO.setContent(inventoryDTO);
            return new ResponseEntity(responseDTO, HttpStatus.MULTI_STATUS);
        }
        else {
            responseDTO.setCode("200");
            responseDTO.setMessage("Item saved successfully");
            responseDTO.setContent(inventoryDTO);
            return new ResponseEntity(responseDTO, HttpStatus.OK);
        }
    }

    @GetMapping
    public List<InventoryDTO> getAllInventory() {
        return inventoryService.getAllItems();
    }

    @GetMapping("/{id}")
    public InventoryDTO getItemById(@PathVariable String id){
        return inventoryService.getSelectedItem(id);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> updateInventory(@RequestBody InventoryDTO inventoryDTO){

        inventoryService.updateItem(inventoryDTO);

            responseDTO.setCode("200");
            responseDTO.setMessage("Item updated successfully");
            responseDTO.setContent(inventoryDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteInventory(@PathVariable String id){
        inventoryService.deleteItem(id);
    }

}
