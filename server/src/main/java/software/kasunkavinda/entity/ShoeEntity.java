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
@Table(name = "shoes")
public class ShoeEntity implements SuperEntity{

    @Id
    @Column(name = "shoe_id")
    private String shoe_id;
    private String description;

    @Column(columnDefinition = "LONGTEXT")
    private String picture;
    private int qty;
    private double bought_price;
    private double sell_price;

    @ManyToOne
    private SupplierEntity supplier;

    @ManyToMany(mappedBy = "shoes")
    private List<BranchEntity> branches;

    @OneToMany(mappedBy = "shoeEntity", cascade = CascadeType.ALL)
    private List<Orders_Shoes> ordersShoes;


}
