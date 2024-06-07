package software.kasunkavinda.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import software.kasunkavinda.entity.CustomerEntity;

import java.util.Date;
import java.util.List;

@Repository
public interface CustomerRepo extends JpaRepository<CustomerEntity,String> {
    boolean existsByEmail(String email);

    @Query("SELECT c FROM CustomerEntity c WHERE MONTH(c.dob) = MONTH(CURRENT_DATE) AND DAY(c.dob) = DAY(CURRENT_DATE)")
    List<CustomerEntity> findByDob();

    @Query("SELECT COUNT(c) FROM CustomerEntity c WHERE FUNCTION('DATE', c.joined_date) = :today")
    Long countCustomersJoinedToday(@Param("today") Date today);

    @Query(value = "SELECT DATE(joined_date) as join_date, COUNT(*) as count FROM customer WHERE joined_date >= CURRENT_DATE - INTERVAL 7 DAY GROUP BY DATE(joined_date)", nativeQuery = true)
    List<Object[]> countWeeklyCustomers();
}
