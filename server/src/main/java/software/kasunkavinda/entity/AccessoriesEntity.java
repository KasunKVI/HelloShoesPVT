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

    @Column(columnDefinition = "LONGTEXT")
    private String picture;
    private int qty;
    private double bought_price;
    private double sell_price;

    @ManyToOne(cascade = CascadeType.MERGE)
    private SupplierEntity supplier;

    @ManyToMany(mappedBy = "accessories", cascade = CascadeType.MERGE)
    private List<BranchEntity> branches;

    @OneToMany(mappedBy = "accessoriesEntity", cascade = CascadeType.ALL)
    private List<Orders_Accessories> ordersAccessories;

}
