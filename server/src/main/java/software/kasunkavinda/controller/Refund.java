package software.kasunkavinda.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.kasunkavinda.dto.OrderDTO;
import software.kasunkavinda.dto.RefundDTO;
import software.kasunkavinda.dto.ResponseDTO;
import software.kasunkavinda.service.OrderService;
import software.kasunkavinda.service.RefundService;

@RestController
@RequestMapping("api/v1/refund")
@RequiredArgsConstructor
public class Refund {

    private static final Logger logger = LoggerFactory.getLogger(Order.class);

    private final OrderService orderService;
    private final RefundService refundService;
    private final ResponseDTO responseDTO;

    @GetMapping("/health")
    public String healthTest() {
        return "Refund Health Good";
    }

    @PostMapping()
    public ResponseEntity<?> makeRefund(@RequestBody RefundDTO refund) {
        logger.info("Making Refund");
        try {
            String opt = refundService.makeRefund(refund);
            if (opt.equals("Refund already exists")) {
                responseDTO.setCode("400");
                responseDTO.setMessage("Refunded already exists");
                responseDTO.setContent(refund);
                return new ResponseEntity<>(responseDTO, HttpStatus.MULTI_STATUS);
            } else {
                responseDTO.setCode("200");
                responseDTO.setMessage("Refunded successfully");
                responseDTO.setContent(refund);
                return new ResponseEntity<>(responseDTO, HttpStatus.OK);
            }
        } catch (Exception exception) {
            logger.error("Error refund order: ", exception);
            responseDTO.setCode("500");
            responseDTO.setMessage("Internal server error");
            responseDTO.setContent(exception.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
