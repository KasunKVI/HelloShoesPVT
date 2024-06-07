package software.kasunkavinda.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import software.kasunkavinda.entity.AccessoriesEntity;
import software.kasunkavinda.entity.ShoeEntity;

import java.util.List;

public interface AccessoriesRepo extends JpaRepository<AccessoriesEntity,String> {

    @Query("SELECT s FROM AccessoriesEntity s JOIN s.branches b WHERE b.branch_id = :branchId")
    List<AccessoriesEntity> findAllAccessoriesByBranchId(@Param("branchId") String branchId);

    @Query("SELECT a.description, a.picture, SUM(oa.quantity) " +
            "FROM Orders_Accessories oa JOIN oa.accessoriesEntity a " +
            "GROUP BY a.description, a.picture " +
            "ORDER BY SUM(oa.quantity) DESC")
    List<Object[]> findMostSoldAccessoryAndQty();

    @Query("SELECT oa.quantity, ae.bought_price " +
            "FROM AccessoriesEntity ae " +
            "INNER JOIN ae.ordersAccessories oa")
    List<Object[]> findOrderItemQtyAndBoughtPrice();

    @Modifying
    @Transactional
    @Query("DELETE FROM AccessoriesEntity s WHERE s.accessories_id = :accessoriesId")
    void deleteAccessoryById(@Param("accessoriesId") String accessoriesId);


}
