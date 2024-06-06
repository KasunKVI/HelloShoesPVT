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
}
