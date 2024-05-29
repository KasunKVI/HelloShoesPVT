package software.kasunkavinda.util;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import software.kasunkavinda.dto.*;
import software.kasunkavinda.entity.*;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Mapping {

    private final ModelMapper mapper;

    public UserEntity toUserEntity(UserDTO userDTO) {
        return mapper.map(userDTO, UserEntity.class);
    }
    public UserDTO toUserDTO(UserEntity userEntity) {
        return  mapper.map(userEntity, UserDTO.class);
    }

    public CustomerEntity toCustomerEntity(CustomerDTO customerDTO) {
        return mapper.map(customerDTO, CustomerEntity.class);
    }
    public CustomerDTO toCustomerDTO(CustomerEntity customerEntity) {
        return  mapper.map(customerEntity, CustomerDTO.class);
    }
    public List<CustomerDTO> toCustomerDTOList(List<CustomerEntity> customers) {
        return mapper.map(customers, List.class);
    }

    public BranchEntity toBranchEntity(BranchDTO branchDTO) {
        return mapper.map(branchDTO, BranchEntity.class);
    }
    public BranchDTO toBranchDTO(BranchEntity branchEntity) {
        return  mapper.map(branchEntity, BranchDTO.class);
    }
    public List<BranchDTO> toBranchDtoList(List<BranchEntity> branches) {
        return mapper.map(branches, List.class);
    }

    public EmployeeEntity toEmployeeEntity(EmployeeDTO employeeDTO) {
        return mapper.map(employeeDTO, EmployeeEntity.class);
    }
    public EmployeeDTO toEmployeeDTO(EmployeeEntity employeeEntity) {
        return  mapper.map(employeeEntity, EmployeeDTO.class);
    }
    public List<EmployeeDTO> toEmployeeDtoList(List<EmployeeEntity> employees) {
        return mapper.map(employees, List.class);
    }

    public ShoeEntity toShoeEntity(InventoryDTO inventoryDTO) {
        ShoeEntity shoeEntity = new ShoeEntity();
        shoeEntity.setShoe_id(inventoryDTO.getInvt_id());
        shoeEntity.setDescription(inventoryDTO.getDescription());
        shoeEntity.setPicture(inventoryDTO.getPicture());
        shoeEntity.setQty(inventoryDTO.getQty());
        shoeEntity.setBought_price(inventoryDTO.getBought_price());
        shoeEntity.setSell_price(inventoryDTO.getSell_price());

        return shoeEntity;
    }
    public InventoryDTO shoeToInventoryDTO(ShoeEntity shoeEntity) {
        return  mapper.map(shoeEntity, InventoryDTO.class);
    }
    public List<InventoryDTO> shoeToInventoryDtoList(List<ShoeEntity> shoes) {
        return mapper.map(shoes, List.class);
    }
    public AccessoriesEntity toAccessoryEntity(InventoryDTO inventoryDTO) {
        return mapper.map(inventoryDTO, AccessoriesEntity.class);
    }
    public InventoryDTO accessoryToInventoryDto(AccessoriesEntity accessoriesEntity) {
        return  mapper.map(accessoriesEntity, InventoryDTO.class);
    }
    public List<InventoryDTO> accessoryTooInventoryDtoList(List<AccessoriesEntity> accessories) {
        return mapper.map(accessories, List.class);
    }

    public OrderEntity toOrderEntity(OrderDTO orderDTO) {
        return mapper.map(orderDTO, OrderEntity.class);
    }
    public OrderDTO toOrderDTO(OrderEntity orderEntity) {
        return  mapper.map(orderEntity, OrderDTO.class);
    }
    public List<OrderDTO> toOrderDtoList(List<OrderEntity> orders) {
        return mapper.map(orders, List.class);
    }

    public RefundEntity toRefundEntity(RefundDTO refundDTO) {
        return mapper.map(refundDTO, RefundEntity.class);
    }
    public RefundDTO toRefundDTO(RefundEntity refundEntity) {
        return  mapper.map(refundEntity, RefundDTO.class);
    }

    public SupplierEntity toSupplierEntity(SupplierDTO supplierDTO) {
        return mapper.map(supplierDTO, SupplierEntity.class);
    }
    public SupplierDTO toSupplierDTO(SupplierEntity supplierEntity) {
        return  mapper.map(supplierEntity, SupplierDTO.class);
    }
    public List<SupplierDTO> toSupplierDtoList(List<SupplierEntity> suppliers) {
        return mapper.map(suppliers, List.class);
    }



}
