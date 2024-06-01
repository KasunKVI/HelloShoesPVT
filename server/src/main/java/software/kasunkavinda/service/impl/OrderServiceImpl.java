package software.kasunkavinda.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.kasunkavinda.dao.*;
import software.kasunkavinda.dto.OrderDTO;
import software.kasunkavinda.entity.*;
import software.kasunkavinda.enums.Level;
import software.kasunkavinda.exception.NotFoundException;
import software.kasunkavinda.exception.QuantityExceededException;
import software.kasunkavinda.service.OrderService;
import software.kasunkavinda.util.Mapping;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional

public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final CustomerRepo customerRepo;
    private final EmployeeRepo employeeRepo;
    private final Mapping mapper;
    private final ShoeRepo shoeRepo;
    private final AccessoriesRepo accessoriesRepo;

    @Override
    public String getLatestOrderId() {
        Optional<OrderEntity> order = orderRepo.findTopByOrderByOrderIdDesc();
        return order.map(OrderEntity::getOrder_id).orElse(null);
    }

    @Override
    public String saveOrder(OrderDTO order) {
        Optional<OrderEntity> orderEntity = orderRepo.findById(order.getOrder_id());
        if (orderEntity.isPresent()) {
            return "Order Id already exists";
        } else {

            CustomerEntity customer = customerRepo.findById(order.getCustomer_id())
                    .orElseThrow(() -> new NotFoundException("Customer not found"));
            customer.setPoints(customer.getPoints() + order.getPoints());


            setCustomerLevel(customer);
            customerRepo.save(customer);


            /////////


            EmployeeEntity employee = employeeRepo.findById(order.getEmployee_id())
                    .orElseThrow(() -> new NotFoundException("Employee not found"));


            OrderEntity newOrder = mapper.toOrderEntity(order);
            newOrder.setCustomer(customer);
            newOrder.setEmployee(employee);


            newOrder.setOrdersShoes(order.getShoes().stream().map(shoeDTO -> {

                System.out.println(shoeDTO);
                ShoeEntity shoe = shoeRepo.findById(shoeDTO.getId())
                        .orElseThrow(() -> new NotFoundException("Item not found"));
                int quantity = shoe.getQty() - shoeDTO.getQty();
                if (quantity < 0) {
//                    logger.warn("Item out of stock: {}", shoe.getShoe_id());
                    throw new QuantityExceededException("Quantity for accessory " + shoe.getShoe_id() + " cannot be less than zero.");
                }
                shoe.setQty(quantity);
                shoeRepo.save(shoe);

                Orders_Shoes ordersShoes = new Orders_Shoes();
                ordersShoes.setShoeEntity(shoe);
                ordersShoes.setOrders(newOrder);
                ordersShoes.setQuantity(shoeDTO.getQty());
                return ordersShoes;
            }).toList());

            newOrder.setAccessories(order.getAccessories().stream().map(AccessoriesDTO -> {
                AccessoriesEntity accessories = accessoriesRepo.findById(AccessoriesDTO.getId())
                        .orElseThrow(() -> new NotFoundException("Item not found"));
                int quantity = accessories.getQty() - AccessoriesDTO.getQty();
                if (quantity < 0) {
//                    logger.warn("Item out of stock: {}", shoe.getShoe_id());
                    throw new QuantityExceededException("Quantity for accessory " + accessories.getAccessories_id() + " cannot be less than zero.");
                }
                accessories.setQty(quantity);
                accessoriesRepo.save(accessories);

                Orders_Accessories ordersAccessories = new Orders_Accessories();
                ordersAccessories.setAccessoriesEntity(accessories);
                ordersAccessories.setOrders(newOrder);
                ordersAccessories.setQuantity(AccessoriesDTO.getQty());
                return ordersAccessories;
            }).toList());




//            logger.info("Order placed successfully: {}", orderCodeString);
            mapper.toOrderDTO(orderRepo.save(newOrder));
            return "Order placed successfully";
        }
    }

    private void setCustomerLevel(CustomerEntity customer) {

        Level level;
        int points = customer.getPoints();
        if (points >= 200) {
            level = Level.GOLD;
        } else if (points >= 100) {
            level = Level.SILVER;
        } else if (points >= 50){
            level = Level.BRONZE;
        }else {
            level = Level.NEW;
        }

        customer.setLevel(level);
    }

}
