package software.kasunkavinda.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import software.kasunkavinda.entity.RefundEntity;

public interface RefundRepo extends JpaRepository<RefundEntity,String> {
}
