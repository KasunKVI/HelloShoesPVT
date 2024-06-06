package software.kasunkavinda.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.kasunkavinda.dao.*;
import software.kasunkavinda.dto.InventoryDTO;
import software.kasunkavinda.entity.AccessoriesEntity;
import software.kasunkavinda.entity.BranchEntity;
import software.kasunkavinda.entity.ShoeEntity;
import software.kasunkavinda.exception.NotFoundException;
import software.kasunkavinda.exception.QuantityExceededException;
import software.kasunkavinda.service.InventoryService;
import software.kasunkavinda.util.Mapping;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

    private final ShoeRepo shoeRepo;
    private final AccessoriesRepo accessoriesRepo;
    private final SupplierRepo supplierRepo;
    private final BranchRepo branchRepo;
    private final Mapping mapper;

    @Override
    public String saveItem(InventoryDTO inventoryDTO) {

        logger.info("Saving item with ID: {}", inventoryDTO.getInvt_id());
        BranchEntity branchEntity = branchRepo.findById(inventoryDTO.getBranch_id())
                .orElseThrow(() -> new NotFoundException("Branch not found with ID: " + inventoryDTO.getBranch_id()));
        List<BranchEntity> branches = new ArrayList<>();
        branches.add(branchEntity);

        if (inventoryDTO.getType().equals("Shoe")) {
            if (inventoryDTO.getInvt_id() == null) {
                throw new IllegalArgumentException("Shoe ID must be set before saving");
            }
            boolean itemExists = shoeRepo.existsById(inventoryDTO.getInvt_id());
            if (itemExists) {
                logger.warn("Item already exists with ID: {}", inventoryDTO.getInvt_id());
                return  "Item already exists";

            } else {

                ShoeEntity shoeEntity = mapper.toShoeEntity(inventoryDTO);
                shoeEntity.setSupplier(supplierRepo.getReferenceById(inventoryDTO.getSupplier_id()));
                shoeEntity.setBranches(branches);

                // Add shoe to branch entity
                branchEntity.getShoes().add(shoeEntity);
                shoeRepo.save(shoeEntity);
                branchRepo.save(branchEntity);

                logger.info("Shoe item saved successfully: {}", inventoryDTO.getInvt_id());
                return "Item saved successfully";
            }
        } else {
            if (inventoryDTO.getInvt_id() == null) {
                throw new IllegalArgumentException("Accessory ID must be set before saving");
            }
            boolean itemExists = accessoriesRepo.existsById(inventoryDTO.getInvt_id());
            if (itemExists) {
                logger.warn("Item already exists with ID: {}", inventoryDTO.getInvt_id());
                return "Item already exists";
            } else {
                AccessoriesEntity accessoryEntity = mapper.toAccessoryEntity(inventoryDTO);
                accessoryEntity.setSupplier(supplierRepo.getReferenceById(inventoryDTO.getSupplier_id()));
                accessoryEntity.setBranches(branches);

                // Add accessory to branch entity
                branchEntity.getAccessories().add(accessoryEntity);
                accessoriesRepo.save(accessoryEntity);
                branchRepo.save(branchEntity); // Ensure both entities are saved

                logger.info("Accessory item saved successfully: {}", inventoryDTO.getInvt_id());
                return "Item saved successfully";
            }
        }
    }

    @Override
    public void deleteItem(String itemId) {
        logger.info("Deleting item with ID: {}", itemId);
        if (itemId.startsWith("ACC")) {
            if (!accessoriesRepo.existsById(itemId)) {
                throw new NotFoundException("Accessory not found with ID: " + itemId);
            }
            accessoriesRepo.deleteById(itemId);
            logger.info("Accessory item deleted: {}", itemId);
        } else {
            if (!shoeRepo.existsById(itemId)) {
                throw new NotFoundException("Shoe not found with ID: " + itemId);
            }
            shoeRepo.deleteById(itemId);
            logger.info("Shoe item deleted: {}", itemId);
        }
    }

    @Override
    public InventoryDTO getSelectedItem(String itemId) {
        logger.info("Fetching item with ID: {}", itemId);
        if (itemId.startsWith("ACC")) {
            AccessoriesEntity accessoryEntity = accessoriesRepo.findById(itemId)
                    .orElseThrow(() -> new NotFoundException("Accessory not found with ID: " + itemId));
            logger.info("Accessory item retrieved: {}", itemId);
            return mapper.accessoryToInventoryDto(accessoryEntity);
        } else {
            ShoeEntity shoeEntity = shoeRepo.findById(itemId)
                    .orElseThrow(() -> new NotFoundException("Shoe not found with ID: " + itemId));
            logger.info("Shoe item retrieved: {}", itemId);
            return mapper.shoeToInventoryDTO(shoeEntity);
        }
    }

    @Override
    public List<InventoryDTO> getAllItems(String branchId) {
        List<AccessoriesEntity> accessories = accessoriesRepo.findAllAccessoriesByBranchId(branchId);
        List<ShoeEntity> shoes = shoeRepo.findAllShoesByBranchId(branchId);
        logger.info("All items retrieved. Accessories count: {}, Shoes count: {}", accessories.size(), shoes.size());
        return mapper.toInventoryDTOList(shoes, accessories);
    }

    @Override
    public String updateItem(InventoryDTO inventoryDTO) {
        logger.info("Updating item with ID: {}", inventoryDTO.getInvt_id());
        if (inventoryDTO.getType().equals("Shoe")) {
            ShoeEntity shoeEntity = shoeRepo.findById(inventoryDTO.getInvt_id())
                    .orElseThrow(() -> new NotFoundException("Shoe not found with ID: " + inventoryDTO.getInvt_id()));
            shoeEntity = mapper.toShoeEntity(inventoryDTO);
            shoeEntity.setSupplier(supplierRepo.getReferenceById(inventoryDTO.getSupplier_id()));
            shoeRepo.save(shoeEntity);
            logger.info("Shoe item updated successfully: {}", inventoryDTO.getInvt_id());
        } else {
            AccessoriesEntity accessoryEntity = accessoriesRepo.findById(inventoryDTO.getInvt_id())
                    .orElseThrow(() -> new NotFoundException("Accessory not found with ID: " + inventoryDTO.getInvt_id()));
            accessoryEntity = mapper.toAccessoryEntity(inventoryDTO);
            accessoryEntity.setSupplier(supplierRepo.getReferenceById(inventoryDTO.getSupplier_id()));
            accessoriesRepo.save(accessoryEntity);
            logger.info("Accessory item updated successfully: {}", inventoryDTO.getInvt_id());
        }
        return "Item updated successfully";
    }
}
