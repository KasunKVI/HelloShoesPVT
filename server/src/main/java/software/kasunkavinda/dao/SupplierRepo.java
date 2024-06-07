package software.kasunkavinda.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import software.kasunkavinda.entity.SupplierEntity;

import java.util.Optional;

public interface SupplierRepo extends JpaRepository<SupplierEntity, String> {

    @Query(value = "SELECT * FROM supplier s ORDER BY supplier_id DESC LIMIT 1", nativeQuery = true)
    Optional<SupplierEntity> findTopByOrderBySupplierIdDesc();

    boolean existsByEmail(String email);

    @Query("SELECT COUNT(s) FROM SupplierEntity s WHERE s.category = 'INTERNATIONAL'")
    Long countInternationalSuppliers();

    @Query("SELECT COUNT(s) FROM SupplierEntity s WHERE s.category = 'LOCAL'")
    Long countLocalSuppliers();

}
