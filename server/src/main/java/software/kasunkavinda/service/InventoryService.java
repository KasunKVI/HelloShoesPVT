package software.kasunkavinda.service;

import software.kasunkavinda.dto.CustomerDTO;
import software.kasunkavinda.dto.InventoryDTO;

import java.util.List;

public interface InventoryService {

    String saveItem(InventoryDTO inventoryDTO);
    void deleteItem(String itemId);
    InventoryDTO getSelectedItem(String itemId);
    List<InventoryDTO> getAllItems(String branchId);
    String updateItem(InventoryDTO inventoryDTO);

}
