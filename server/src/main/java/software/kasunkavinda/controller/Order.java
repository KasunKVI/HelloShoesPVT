package software.kasunkavinda.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import software.kasunkavinda.dto.MostSoldItemDTO;
import software.kasunkavinda.dto.OrderDTO;
import software.kasunkavinda.dto.ResponseDTO;
import software.kasunkavinda.service.OrderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
public class Order {

    private static final Logger logger = LoggerFactory.getLogger(Order.class);

    private final OrderService orderService;
    private final ResponseDTO responseDTO;

    @GetMapping("/health")
    public ResponseEntity<String> healthTest() {
        logger.info("Health test endpoint called");
        return new ResponseEntity<>("Order Health Good", HttpStatus.OK);
    }

    @GetMapping("/latest")
    public ResponseEntity<?> getLastOrderId() {
        logger.info("Fetching latest order ID");
        try {
            String latestId = orderService.getLatestOrderId();
            return ResponseEntity.ok(latestId);
        } catch (Exception exception) {
            logger.error("Error fetching latest order ID: ", exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{branchId}")
    public ResponseEntity<?> getAllOrder(@PathVariable String branchId) {
        logger.info("Fetching all orders");
        try {
            List<OrderDTO> orderList = orderService.getAllOrder(branchId);
            return ResponseEntity.ok(orderList);
        } catch (Exception exception) {
            logger.error("Error fetching all orders: ", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error | Unable to fetch orders.\nMore Details\n" + exception);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveOrder(@RequestBody OrderDTO order) {
        logger.info("Saving order");
        try {
            String opt = orderService.saveOrder(order);
            if (opt.equals("Order Id already exists")) {
                responseDTO.setCode("400");
                responseDTO.setMessage("Order already exists");
                responseDTO.setContent(order);
                return new ResponseEntity<>(responseDTO, HttpStatus.MULTI_STATUS);
            } else {
                responseDTO.setCode("200");
                responseDTO.setMessage("Order placed successfully");
                responseDTO.setContent(order);
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            }
        } catch (Exception exception) {
            logger.error("Error saving order: ", exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/total-sales-balance/{branchId}")
    public Double getTotalSalesBalance(@PathVariable String branchId) {
        return orderService.getTotalSalesBalance(branchId);
    }

    @GetMapping("/today-sales/{branchId}")
    public Double getTotalSalesBalanceToday(@PathVariable String branchId) {
        return orderService.getTotalSalesBalanceToday(branchId);
    }
    @GetMapping("/sales/{branchId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getSalesData(@PathVariable String branchId) {
        logger.info("Fetching sales data");
        try {
            List<Object[]> salesData = orderService.getSalesData(branchId);
            Map<String, Object> response = new HashMap<>();
            response.put("dates", salesData.stream().map(data -> data[0]).toArray());
            response.put("totals", salesData.stream().map(data -> data[1]).toArray());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching sales data", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/total-profit/{branchId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Double getTotalProfit(@PathVariable String branchId) {
        return orderService.calculateTotalProfit(branchId);
    }

    @GetMapping("/most-saled-item")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getMostSaledItem() {
        logger.info("Fetching most saled item");
        try {
            MostSoldItemDTO mostSaledItem = orderService.getMostSaledItem();
            return ResponseEntity.ok(mostSaledItem);
        } catch (Exception e) {
            logger.error("Error fetching most saled item", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error | Unable to fetch most saled item.\nMore Details\n" + e);
        }
    }

}
