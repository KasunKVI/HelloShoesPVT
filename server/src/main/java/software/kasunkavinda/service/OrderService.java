package software.kasunkavinda.service;

import software.kasunkavinda.dto.OrderDTO;

public interface OrderService {

    String getLatestOrderId();
    String saveOrder(OrderDTO order);
}
