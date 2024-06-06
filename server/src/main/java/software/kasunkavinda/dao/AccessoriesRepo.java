package software.kasunkavinda.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import software.kasunkavinda.entity.AccessoriesEntity;
import software.kasunkavinda.entity.ShoeEntity;

import java.util.List;

public interface AccessoriesRepo extends JpaRepository<AccessoriesEntity,String> {

    @Query("SELECT s FROM AccessoriesEntity s JOIN s.branches b WHERE b.branch_id = :branchId")
    List<AccessoriesEntity> findAllAccessoriesByBranchId(@Param("branchId") String branchId);

}
