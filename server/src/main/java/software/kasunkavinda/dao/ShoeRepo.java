package software.kasunkavinda.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import software.kasunkavinda.entity.AccessoriesEntity;
import software.kasunkavinda.entity.ShoeEntity;

import java.util.List;

public interface ShoeRepo extends JpaRepository<ShoeEntity,String>{
    @Query("SELECT s FROM ShoeEntity s JOIN s.branches b WHERE b.branch_id = :branchId")
    List<ShoeEntity> findAllShoesByBranchId(@Param("branchId") String branchId);

    @Query("SELECT s.description, s.picture, SUM(o.quantity) " +
            "FROM Orders_Shoes o JOIN o.shoeEntity s " +
            "GROUP BY s.description, s.picture " +
            "ORDER BY SUM(o.quantity) DESC")
    List<Object[]> findMostSoldShoeAndQty();

    @Query("SELECT os.quantity, se.bought_price " +
            "FROM Orders_Shoes os " +
            "INNER JOIN os.shoeEntity se")
    List<Object[]> findOrderShoeQtyAndBoughtPrice();
}
