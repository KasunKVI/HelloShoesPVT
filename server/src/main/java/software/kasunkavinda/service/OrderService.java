package software.kasunkavinda.service;

import software.kasunkavinda.dto.OrderDTO;

import java.util.List;

public interface OrderService {

    String getLatestOrderId();
    List<OrderDTO> getAllOrder(String branchId);
    String saveOrder(OrderDTO order);
}
