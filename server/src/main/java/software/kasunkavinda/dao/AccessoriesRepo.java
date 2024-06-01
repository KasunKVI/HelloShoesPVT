package software.kasunkavinda.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import software.kasunkavinda.entity.AccessoriesEntity;

public interface AccessoriesRepo extends JpaRepository<AccessoriesEntity,String> {
}
