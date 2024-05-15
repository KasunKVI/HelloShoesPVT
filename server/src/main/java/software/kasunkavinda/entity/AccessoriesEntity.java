package software.kasunkavinda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "accessories")

public class AccessoriesEntity implements SuperEntity{

    @Id
    private String accessories_id;
    private String description;
    private String picture;
    private int qty;
    private String category;
    private double bought_price;
    private double sell_price;

    @ManyToOne
    private SupplierEntity supplier;

    @ManyToMany(mappedBy = "accessories")
    private List<BranchEntity> branches;

    @ManyToMany(mappedBy = "accessories")
    private List<OrderEntity> orders;

}
