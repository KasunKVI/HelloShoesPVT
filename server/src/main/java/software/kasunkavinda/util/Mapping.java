package software.kasunkavinda.util;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import software.kasunkavinda.controller.Employee;
import software.kasunkavinda.dto.*;
import software.kasunkavinda.entity.*;
import software.kasunkavinda.enums.Gender;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        EmployeeEntity employee = new EmployeeEntity();
        employee.setEmployee_id(employeeDTO.getEmployee_id());
        employee.setName(employeeDTO.getName());
        employee.setProfile_pic(employeeDTO.getProfile_pic());
        employee.setGender(Gender.valueOf(employeeDTO.getGender()));
        employee.setBuilding_no(employeeDTO.getBuilding_no());
        employee.setLane(employeeDTO.getLane());
        employee.setCity(employeeDTO.getCity());
        employee.setState(employeeDTO.getState());
        employee.setPostal_code(employeeDTO.getPostal_code());
        employee.setStatus(employeeDTO.getStatus());
        employee.setEmail(employeeDTO.getEmail());
        employee.setDesignation(employeeDTO.getDesignation());
        employee.setContact(employeeDTO.getContact());
        employee.setEmergency_contact(employeeDTO.getEmergency_contact());
        employee.setJoined_date(employeeDTO.getJoined_date());
        employee.setDob(employeeDTO.getDob());
        employee.setGuardian_name(employeeDTO.getGuardian_name());

        return employee;
    }
    public EmployeeDTO toEmployeeDTO(EmployeeEntity employeeEntity) {
        return new EmployeeDTO(employeeEntity.getEmployee_id(), employeeEntity.getName(), employeeEntity.getProfile_pic(), employeeEntity.getGender().toString(), employeeEntity.getBuilding_no(), employeeEntity.getLane(), employeeEntity.getCity(), employeeEntity.getState(), employeeEntity.getPostal_code(), employeeEntity.getStatus(), employeeEntity.getEmail(), employeeEntity.getDesignation(), employeeEntity.getContact(), employeeEntity.getEmergency_contact(), employeeEntity.getJoined_date(), employeeEntity.getDob(), employeeEntity.getGuardian_name(),employeeEntity.getBranch().getBranch_id());
    }
    public List<EmployeeDTO> toEmployeeDtoList(List<EmployeeEntity> employees) {
        List<EmployeeDTO> employeeDTOS = new ArrayList<>();
        for (EmployeeEntity employee : employees) {
            employeeDTOS.add(new EmployeeDTO(employee.getEmployee_id(), employee.getName(), employee.getProfile_pic(), employee.getGender().toString(), employee.getBuilding_no(), employee.getLane(), employee.getCity(), employee.getState(), employee.getPostal_code(), employee.getStatus(), employee.getEmail(), employee.getDesignation(), employee.getContact(), employee.getEmergency_contact(), employee.getJoined_date(), employee.getDob(), employee.getGuardian_name(),employee.getBranch().getBranch_id()));

        }
        return employeeDTOS;
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
        AccessoriesEntity accessoriesEntity = new AccessoriesEntity();
        accessoriesEntity.setAccessories_id(inventoryDTO.getInvt_id());
        accessoriesEntity.setDescription(inventoryDTO.getDescription());
        accessoriesEntity.setPicture(inventoryDTO.getPicture());
        accessoriesEntity.setQty(inventoryDTO.getQty());
        accessoriesEntity.setBought_price(inventoryDTO.getBought_price());
        accessoriesEntity.setSell_price(inventoryDTO.getSell_price());
        return accessoriesEntity;
    }
    public InventoryDTO accessoryToInventoryDto(AccessoriesEntity accessoriesEntity) {
        InventoryDTO inventoryDTO = new InventoryDTO();
        inventoryDTO.setInvt_id(accessoriesEntity.getAccessories_id());
        inventoryDTO.setQty(accessoriesEntity.getQty());
        inventoryDTO.setDescription(accessoriesEntity.getDescription());
        inventoryDTO.setPicture(accessoriesEntity.getPicture());
        inventoryDTO.setSell_price(accessoriesEntity.getSell_price());
        inventoryDTO.setBought_price(accessoriesEntity.getBought_price());
        inventoryDTO.setSupplier_id(accessoriesEntity.getSupplier().getSupplier_id());
        return inventoryDTO;
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

        return suppliers.stream()
                .map(supplierEntity -> mapper.map(supplierEntity, SupplierDTO.class))
                .collect(Collectors.toList());

//        return mapper.map(suppliers, List.class);
    }


    public List<InventoryDTO> toInventoryDTOList(List<ShoeEntity> shoes, List<AccessoriesEntity> accessories) {

        List<InventoryDTO> inventoryDTOS = new ArrayList<>();
        for (ShoeEntity shoe : shoes) {
            inventoryDTOS.add(new InventoryDTO(shoe.getShoe_id(), shoe.getDescription(), shoe.getPicture(), shoe.getQty(), shoe.getBought_price(), shoe.getSell_price(), "Shoe",shoe.getSupplier().getSupplier_id()));
        }
        for (AccessoriesEntity accessory : accessories) {
            inventoryDTOS.add(new InventoryDTO(accessory.getAccessories_id(), accessory.getDescription(), accessory.getPicture(), accessory.getQty(), accessory.getBought_price(), accessory.getSell_price(), "Accessory",accessory.getSupplier().getSupplier_id() ));
        }
        return inventoryDTOS;
    }
}
