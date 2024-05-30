package software.kasunkavinda.service;

import software.kasunkavinda.dto.CustomerDTO;
import software.kasunkavinda.dto.SupplierDTO;

import java.util.List;

public interface SupplierService {

    String saveSupplier(SupplierDTO supplierDTO);
    void deleteSupplier(String supplierId);
    SupplierDTO getSelectedSupplier(String supplierId);
    List<SupplierDTO> getAllSuppliers();
    String updateSupplier(SupplierDTO supplierDTO);
    String getLatestSupplierId();

}
