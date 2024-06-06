package software.kasunkavinda.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.kasunkavinda.dao.EmployeeRepo;
import software.kasunkavinda.dao.OrderRepo;
import software.kasunkavinda.dao.RefundRepo;
import software.kasunkavinda.dto.RefundDTO;
import software.kasunkavinda.entity.BranchEntity;
import software.kasunkavinda.entity.EmployeeEntity;
import software.kasunkavinda.entity.OrderEntity;
import software.kasunkavinda.entity.RefundEntity;
import software.kasunkavinda.exception.QuantityExceededException;
import software.kasunkavinda.service.RefundService;

@Service
@RequiredArgsConstructor
@Transactional
public class RefundServiceImpl implements RefundService {

    private final RefundRepo refundRepo;
    private final OrderRepo orderRepo;
    private final EmployeeRepo employeeRepo;
    private static final Logger logger = LoggerFactory.getLogger(RefundService.class);

    @Override
    public String makeRefund(RefundDTO refund) {

        logger.info("Refund order with Order ID: {}",refund.getOrder_id());

        // Validate the Order ID
        if (refund.getOrder_id() == null) {
            logger.error("The given ID must not be null");
            throw new IllegalArgumentException("The given ID must not be null");
        }

        // Modify the order ID to reflect the refund
        String refundId = "F" + refund.getOrder_id().substring(1); // Replace 'R' with 'F'

        boolean refundExist = refundRepo.existsById(refundId);

        if (refundExist) {
            logger.warn("Refund already exists with ID: {}", refundId);
            throw new QuantityExceededException("Refund already exists");
        } else {

            RefundEntity refund1 = new RefundEntity();
            refund1.setRefund_id(refundId);
            refund1.setDate(refund.getDate());

            /////////
            OrderEntity order = orderRepo.getReferenceById(refund.getOrder_id());
            refund1.setAmount(order.getTotal());
            refund1.setOrder(order);


            EmployeeEntity employeeEntity = employeeRepo.getReferenceById(refund.getEmployee_id());
            refund1.setEmployee(employeeEntity);
            refundRepo.save(refund1);

            order.setRefund(refund1);
            orderRepo.save(order);

            logger.info("Refund done successfully with ID: {}", refund1.getRefund_id());
            return "Refunded successfully";
        }
    }
}
