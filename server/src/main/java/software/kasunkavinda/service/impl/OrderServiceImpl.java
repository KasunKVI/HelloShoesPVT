package software.kasunkavinda.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.kasunkavinda.dao.*;
import software.kasunkavinda.dto.MostSoldItemDTO;
import software.kasunkavinda.dto.OrderDTO;
import software.kasunkavinda.entity.*;
import software.kasunkavinda.enums.Level;
import software.kasunkavinda.exception.NotFoundException;
import software.kasunkavinda.exception.QuantityExceededException;
import software.kasunkavinda.service.OrderService;
import software.kasunkavinda.util.Mapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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


    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public String getLatestOrderId() {
        Optional<OrderEntity> order = orderRepo.findTopByOrderByOrderIdDesc();
        String latestOrderId = order.map(OrderEntity::getOrder_id).orElse(null);
        logger.info("Fetched latest order ID: {}", latestOrderId);
        return latestOrderId;
    }

    @Override
    public List<OrderDTO> getAllOrder(String branchId) {
        return mapper.toOrderDtoList(orderRepo.findAllByBranchId(branchId));
    }


    @Override
    public String saveOrder(OrderDTO order) {
        logger.info("Attempting to save order with ID: {}", order.getOrder_id());
        Optional<OrderEntity> orderEntity = orderRepo.findById(order.getOrder_id());
        if (orderEntity.isPresent()) {
            logger.warn("Order ID already exists: {}", order.getOrder_id());
            return "Order ID already exists";
        } else {
            CustomerEntity customer = null;
            if (order.getCustomer_id()!=null){
                customer = customerRepo.findById(order.getCustomer_id())
                        .orElseThrow(() -> new NotFoundException("Customer not found"));
                customer.setPoints(customer.getPoints() + order.getPoints());
                setCustomerLevel(customer);
                customerRepo.save(customer);
                logger.info("Customer points updated: {}, New level: {}", customer.getPoints(), customer.getLevel());

            }

            EmployeeEntity employee = employeeRepo.findById(order.getEmployee_id())
                    .orElseThrow(() -> new NotFoundException("Employee not found"));

            OrderEntity newOrder = mapper.toOrderEntity(order);
            newOrder.setCustomer(customer);
            newOrder.setEmployee(employee);

            newOrder.setOrdersShoes(order.getShoes().stream().map(shoeDTO -> {
                ShoeEntity shoe = shoeRepo.findById(shoeDTO.getId())
                        .orElseThrow(() -> new NotFoundException("Item not found"));
                int quantity = shoe.getQty() - shoeDTO.getQty();
                if (quantity < 0) {
                    logger.warn("Shoe out of stock: {}", shoe.getShoe_id());
                    throw new QuantityExceededException("Quantity for shoe " + shoe.getShoe_id() + " cannot be less than zero.");
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
                    logger.warn("Accessory out of stock: {}", accessories.getAccessories_id());
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

            logger.info("Order placed successfully: {}", newOrder.getOrder_id());
            mapper.toOrderDTO(orderRepo.save(newOrder));
            return "Order placed successfully";
        }
    }

    @Override
    public Double getTotalSalesBalance(String branchId){
        return orderRepo.findTotalSalesBalance(branchId);
    }

    @Override
    public Double getTotalSalesBalanceToday(String branchId) {
        Date today = new Date();
        return orderRepo.findTodaysSales(today, branchId);
    }

    @Override
    public List<Object[]> getSalesData(String branchId) {
        return orderRepo.findSalesDataByBranch(branchId);
    }

    @Override
    public double calculateTotalProfit(String branchId) {
        // Step 1: Calculate Total Revenue (Total Sales)
        Double totalRevenue = orderRepo.findTotalSalesBalance(branchId);

        // Step 2: Calculate Total Cost of Goods Sold (COGS)
        double totalCostOfGoodsSold =calculateTotalCostOfGoodsSold();

        // Step 3: Calculate Total Profit
        return totalRevenue - totalCostOfGoodsSold;
    }

    @Override
    public MostSoldItemDTO getMostSaledItem() {
        List<Object[]> mostSaledShoe = shoeRepo.findMostSoldShoeAndQty();
        List<Object[]> mostSaledAccessory = accessoriesRepo.findMostSoldAccessoryAndQty();

        if (!mostSaledShoe.isEmpty() && mostSaledShoe.get(0)[1] != null) {
            Object[] shoeData = mostSaledShoe.get(0);
            MostSoldItemDTO shoeDTO = new MostSoldItemDTO();
            shoeDTO.setDescription((String) shoeData[0]);
            shoeDTO.setQuantity((Long) shoeData[2]);
            // Set the picture from the shoeData array if available
            shoeDTO.setPicture(shoeData.length > 2 ? (String) shoeData[1] : null);
            return shoeDTO;
        }

        if (!mostSaledAccessory.isEmpty() && mostSaledAccessory.get(0)[1] != null) {
            Object[] accessoryData = mostSaledAccessory.get(0);
            MostSoldItemDTO accessoryDTO = new MostSoldItemDTO();
            accessoryDTO.setDescription((String) accessoryData[0]);
            accessoryDTO.setQuantity(Long.valueOf((Integer) accessoryData[2]));
            // Set the picture from the accessoryData array if available
            accessoryDTO.setPicture(accessoryData.length > 2 ? (String) accessoryData[1] : null);
            return accessoryDTO;
        }

        // If both shoe and accessory lists are empty or contain no sold items
        return null;
    }

    private double calculateTotalCostOfGoodsSold() {
        double totalCostOfGoodsSold = 0;

// Calculate cost for shoes
        List<Object[]> ordersShoes = shoeRepo.findOrderShoeQtyAndBoughtPrice();
        for (Object[] os : ordersShoes) {
            int quantity = (int) os[0];
            double boughtPrice = (double) os[1];
            totalCostOfGoodsSold += quantity * boughtPrice;
        }

// Calculate cost for accessories
        List<Object[]> ordersAccessories = accessoriesRepo.findMostSoldAccessoryAndQty();
        for (Object[] oa : ordersAccessories) {
            int quantity = (int) oa[1];
            double boughtPrice = (double) oa[2];
            totalCostOfGoodsSold += quantity * boughtPrice;
        }

        return totalCostOfGoodsSold;
    }

    private void setCustomerLevel(CustomerEntity customer) {
        Level level;
        int points = customer.getPoints();
        if (points >= 200) {
            level = Level.GOLD;
        } else if (points >= 100) {
            level = Level.SILVER;
        } else if (points >= 50) {
            level = Level.BRONZE;
        } else {
            level = Level.NEW;
        }
        customer.setLevel(level);
        logger.info("Customer level set to: {} for customer ID: {}", level, customer.getCustomer_id());
    }
}
