package software.kasunkavinda.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.kasunkavinda.dto.OrderDTO;
import software.kasunkavinda.dto.ResponseDTO;
import software.kasunkavinda.service.OrderService;

import java.util.List;

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
}
