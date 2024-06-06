package software.kasunkavinda.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import software.kasunkavinda.entity.CustomerEntity;

import java.util.List;

@Repository
public interface CustomerRepo extends JpaRepository<CustomerEntity,String> {
    boolean existsByEmail(String email);

    @Query("SELECT c FROM CustomerEntity c WHERE MONTH(c.dob) = MONTH(CURRENT_DATE) AND DAY(c.dob) = DAY(CURRENT_DATE)")
    List<CustomerEntity> findByDob();

}
