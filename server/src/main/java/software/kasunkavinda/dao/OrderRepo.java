package software.kasunkavinda.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import software.kasunkavinda.entity.EmployeeEntity;
import software.kasunkavinda.entity.OrderEntity;
import software.kasunkavinda.entity.SupplierEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<OrderEntity, String> {

    @Query(value = "SELECT * FROM orders s ORDER BY order_id DESC LIMIT 1", nativeQuery = true)
    Optional<OrderEntity> findTopByOrderByOrderIdDesc();

    @Query("SELECT o FROM OrderEntity o WHERE o.branch.branch_id = :branchId")
    List<OrderEntity> findAllByBranchId(String branchId);

    @Query("SELECT SUM(o.total) FROM OrderEntity o WHERE o.branch.branch_id = :branchId")
    Double findTotalSalesBalance(String branchId);

    @Query("SELECT SUM(o.total) FROM OrderEntity o WHERE FUNCTION('DATE', o.date) = :currentDate AND o.branch.branch_id = :branchId")
    Double findTodaysSales(@Param("currentDate") Date currentDate, @Param("branchId") String branchId);

    @Query("SELECT o.date, SUM(o.total) FROM OrderEntity o WHERE o.branch.branch_id = :branchId GROUP BY o.date ORDER BY o.date")
    List<Object[]> findSalesDataByBranch(@Param("branchId") String branchId);


}
