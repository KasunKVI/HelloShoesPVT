package software.kasunkavinda.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import software.kasunkavinda.entity.CustomerEntity;

@Repository
public interface CustomerRepo extends JpaRepository<CustomerEntity,String> {
    boolean existsByEmail(String email);
}
