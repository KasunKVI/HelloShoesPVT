package software.kasunkavinda.controller;

import lombok.RequiredArgsConstructor;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/supplier")
@RequiredArgsConstructor
public class Supplier  {

    private final SupplierService supplierService;
    private final ResponseDTO responseDTO;


    @GetMapping("/health")
    public String healthTest() {
        return "Supplier Health Good";
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity saveSupplier(@RequestBody SupplierDTO supplierDTO){

        String opt = supplierService.saveSupplier(supplierDTO);
        System.out.println(opt);
        if (opt.equals("Supplier already exists")){
            responseDTO.setCode("400");
            responseDTO.setMessage("Supplier already exists");
            responseDTO.setContent(supplierDTO);
            return new ResponseEntity(responseDTO, HttpStatus.MULTI_STATUS);
        }else if (opt.equals("Email already exists")){
            responseDTO.setCode("400");
            responseDTO.setMessage("Email already exists");
            responseDTO.setContent(supplierDTO);
            return new ResponseEntity(responseDTO, HttpStatus.MULTI_STATUS);
        }
        else {
            responseDTO.setCode("200");
            responseDTO.setMessage("Supplier saved successfully");
            responseDTO.setContent(supplierDTO);
            return new ResponseEntity(responseDTO, HttpStatus.OK);
        }

    }

    @GetMapping("/latest")
    public ResponseEntity<String> getLatestSupplierId() {
        String latestId = supplierService.getLatestSupplierId();
        return ResponseEntity.ok(latestId);
    }

    @GetMapping("/{id}")
    public SupplierDTO getSelectedSupplier(@PathVariable String id){
        return supplierService.getSelectedSupplier(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteSupplier(@PathVariable String id){
        supplierService.deleteSupplier(id);
    }

    @GetMapping("/all")
    public List<SupplierDTO> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateSupplier(@RequestBody SupplierDTO supplierDTO) {
        String resp = supplierService.updateSupplier(supplierDTO);
        if (resp.equals("Email already exists")) {
            responseDTO.setCode("400");
            responseDTO.setMessage("Email already exists");
            responseDTO.setContent(supplierDTO);
            return new ResponseEntity(responseDTO, HttpStatus.MULTI_STATUS);
        } else {
            responseDTO.setCode("200");
            responseDTO.setMessage("Supplier updated successfully");
            responseDTO.setContent(supplierDTO);
            return new ResponseEntity(responseDTO, HttpStatus.OK);
        }
    }
    @GetMapping("/ids")
    public List<String> getAllSupplierIds() {
        return supplierService.getAllSupplierIds();
    }
}
