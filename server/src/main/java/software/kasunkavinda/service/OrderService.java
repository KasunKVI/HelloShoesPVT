package software.kasunkavinda.service;

import software.kasunkavinda.dto.MostSoldItemDTO;
import software.kasunkavinda.dto.OrderDTO;

import java.util.List;

public interface OrderService {

    String getLatestOrderId();
    List<OrderDTO> getAllOrder(String branchId);
    String saveOrder(OrderDTO order);
    Double getTotalSalesBalance(String branchId);
    Double getTotalSalesBalanceToday(String branchId);
    List<Object[]> getSalesData(String branchId);
    double calculateTotalProfit(String branchId);

    MostSoldItemDTO getMostSaledItem();
}
