package software.kasunkavinda.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.kasunkavinda.dto.OrderDTO;
import software.kasunkavinda.dto.ResponseDTO;
import software.kasunkavinda.service.OrderService;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
public class Order {

    private final OrderService orderService;
    private final ResponseDTO responseDTO;


    @GetMapping("/health")
    public String healthTest() {
        return "Order Health Good";
    }

    @GetMapping("/latest")
    public ResponseEntity<String> getLastOrderId() {
        String latestId = orderService.getLatestOrderId();
        return ResponseEntity.ok(latestId);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveOrder(@RequestBody OrderDTO order) {

        String opt = orderService.saveOrder(order);
        if (opt.equals("Order Id already exists")) {
            responseDTO.setCode("400");
            responseDTO.setMessage("order already exists");
            responseDTO.setContent(order);
            return new ResponseEntity(responseDTO, HttpStatus.MULTI_STATUS);
        } else {
            responseDTO.setCode("200");
            responseDTO.setMessage("Order placed successfully");
            responseDTO.setContent(order);
            return new ResponseEntity(responseDTO, HttpStatus.OK);
        }
    }
}
