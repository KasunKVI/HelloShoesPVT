package software.kasunkavinda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.kasunkavinda.enums.Size;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "shoes")
public class ShoeEntity implements SuperEntity{

    @Id
    private String shoe_id;
    private String description;
    private String picture;
    private int qty;
    private String category;
    private Size size;
    private double bought_price;
    private double sell_price;

    @ManyToOne
    private SupplierEntity supplier;

    @ManyToMany(mappedBy = "items")
    private List<BranchEntity> branches;

    @ManyToMany(mappedBy = "items")
    private List<OrderEntity> orders;


}
