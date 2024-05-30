package software.kasunkavinda.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import software.kasunkavinda.entity.EmployeeEntity;

import java.util.List;

@Repository
public interface EmployeeRepo extends JpaRepository<EmployeeEntity,String> {

    boolean existsByEmail(String email);

    @Query("SELECT e FROM EmployeeEntity e WHERE e.branch.branch_id = :branchId")
    List<EmployeeEntity> findAllByBranchId(String branchId);

}
