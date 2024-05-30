package software.kasunkavinda.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import software.kasunkavinda.entity.BranchEntity;

import java.util.List;

public interface BranchRepo extends JpaRepository<BranchEntity, String> {

    @Query("SELECT b.name FROM BranchEntity b")
    List<String> findAllBranchNames();
}
