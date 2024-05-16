package software.kasunkavinda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "orders_shoes")
public class Orders_Shoes {

    @Id
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private OrderEntity orders;

    @Id
    @ManyToOne
    @JoinColumn(name = "shoe_id", referencedColumnName = "shoe_id")
    private ShoeEntity shoeEntity;

    private int quantity;

}
