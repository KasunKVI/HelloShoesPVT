package software.kasunkavinda.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import software.kasunkavinda.entity.AccessoriesEntity;
import software.kasunkavinda.entity.ShoeEntity;

public interface ShoeRepo extends JpaRepository<ShoeEntity,String>{
}
