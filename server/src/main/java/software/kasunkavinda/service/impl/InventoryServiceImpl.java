package software.kasunkavinda.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.kasunkavinda.dao.AccessoriesRepo;
import software.kasunkavinda.dao.CustomerRepo;
import software.kasunkavinda.dao.ShoeRepo;
import software.kasunkavinda.dao.SupplierRepo;
import software.kasunkavinda.dto.InventoryDTO;
import software.kasunkavinda.entity.AccessoriesEntity;
import software.kasunkavinda.entity.ShoeEntity;
import software.kasunkavinda.service.InventoryService;
import software.kasunkavinda.util.Mapping;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

    private final ShoeRepo shoeRepo;
    private final AccessoriesRepo accessoriesRepo;
    private final SupplierRepo supplierRepo;
    private final Mapping mapper;

    @Override
    public String saveItem(InventoryDTO inventoryDTO) {

        System.out.println(inventoryDTO);

        if (inventoryDTO.getType().equals("Shoe")) {
            if (inventoryDTO.getInvt_id() == null) {
                return "Shoe ID must be set before saving";
            }
            boolean opt = shoeRepo.existsById(inventoryDTO.getInvt_id());
            if (opt) {
                return "Item already exists";
            } else {
                ShoeEntity shoeEntity = mapper.toShoeEntity(inventoryDTO);
                shoeEntity.setSupplier(supplierRepo.getReferenceById(inventoryDTO.getSupplier_id()));
                shoeRepo.save(shoeEntity);
                logger.info("Shoe item saved successfully: {}", inventoryDTO.getInvt_id());
                return "Item saved successfully";
            }
        } else {
            if (inventoryDTO.getInvt_id() == null) {
                return "Accessory ID must be set before saving";
            }
            boolean opt2 = accessoriesRepo.existsById(inventoryDTO.getInvt_id());
            if (opt2) {
                return "Item already exists";
            } else {
                AccessoriesEntity accessoryEntity = mapper.toAccessoryEntity(inventoryDTO);
                accessoryEntity.setSupplier(supplierRepo.getReferenceById(inventoryDTO.getSupplier_id()));
                accessoriesRepo.save(accessoryEntity);
                logger.info("Accessory item saved successfully: {}", inventoryDTO.getInvt_id());
                return "Item saved successfully";
            }
        }
    }

    @Override
    public void deleteItem(String itemId) {
        if (itemId.startsWith("ACC")) {
            accessoriesRepo.deleteById(itemId);
            logger.info("Accessory item deleted: {}", itemId);
        } else {
            shoeRepo.deleteById(itemId);
            logger.info("Shoe item deleted: {}", itemId);
        }
    }

    @Override
    public InventoryDTO getSelectedItem(String itemId) {
        if (itemId.startsWith("ACC")) {
            AccessoriesEntity accessoryEntity = accessoriesRepo.getReferenceById(itemId);
            logger.info("Accessory item retrieved: {}", itemId);
            return mapper.accessoryToInventoryDto(accessoryEntity);
        } else {
            ShoeEntity shoeEntity = shoeRepo.getReferenceById(itemId);
            logger.info("Shoe item retrieved: {}", itemId);
            return mapper.shoeToInventoryDTO(shoeEntity);
        }
    }

    @Override
    public List<InventoryDTO> getAllItems() {
        List<AccessoriesEntity> accessories = accessoriesRepo.findAll();
        List<ShoeEntity> shoes = shoeRepo.findAll();
        logger.info("All items retrieved. Accessories count: {}, Shoes count: {}", accessories.size(), shoes.size());
        return mapper.toInventoryDTOList(shoes, accessories);
    }

    @Override
    public String updateItem(InventoryDTO inventoryDTO) {
        if (inventoryDTO.getType().equals("Shoe")) {
            ShoeEntity shoeEntity = mapper.toShoeEntity(inventoryDTO);
            shoeEntity.setSupplier(supplierRepo.getReferenceById(inventoryDTO.getSupplier_id()));
            shoeRepo.save(shoeEntity);
            logger.info("Shoe item updated successfully: {}", inventoryDTO.getInvt_id());
        } else {
            AccessoriesEntity accessoryEntity = mapper.toAccessoryEntity(inventoryDTO);
            accessoryEntity.setSupplier(supplierRepo.getReferenceById(inventoryDTO.getSupplier_id()));
            accessoriesRepo.save(accessoryEntity);
            logger.info("Accessory item updated successfully: {}", inventoryDTO.getInvt_id());
        }
        return "Item updated successfully";
    }
}
