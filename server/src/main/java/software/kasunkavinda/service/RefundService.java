package software.kasunkavinda.service;

import software.kasunkavinda.dto.OrderDTO;
import software.kasunkavinda.dto.RefundDTO;

public interface RefundService {

    String makeRefund(RefundDTO refund);

}
