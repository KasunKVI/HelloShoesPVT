package software.kasunkavinda.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import software.kasunkavinda.entity.EmployeeEntity;
import software.kasunkavinda.entity.OrderEntity;
import software.kasunkavinda.entity.SupplierEntity;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<OrderEntity, String> {

    @Query(value = "SELECT * FROM orders s ORDER BY order_id DESC LIMIT 1", nativeQuery = true)
    Optional<OrderEntity> findTopByOrderByOrderIdDesc();

    @Query("SELECT o FROM OrderEntity o WHERE o.branch.branch_id = :branchId")
    List<OrderEntity> findAllByBranchId(String branchId);
}
