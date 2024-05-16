package software.kasunkavinda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "orders_accessories")
public class Orders_Accessories {

    @Id
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private OrderEntity orders;

    @Id
    @ManyToOne
    @JoinColumn(name = "accessories_id", referencedColumnName = "accessories_id")
    private AccessoriesEntity accessoriesEntity;

    private int quantity;
}
