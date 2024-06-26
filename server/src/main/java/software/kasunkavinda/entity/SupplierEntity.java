package software.kasunkavinda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.kasunkavinda.enums.Category;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "supplier")
public class SupplierEntity implements SuperEntity{

    @Id
    private String supplier_id;
    private String name;
    private String email;
    private String contact;
    private String building_no;
    private String lane;
    private String city;
    private String state;
    private String postal_code;
    private String brand;
    @Enumerated(EnumType.STRING)
    private Category category;


    @OneToMany(mappedBy = "supplier")
    private List<AccessoriesEntity> accessories;

    @OneToMany(mappedBy = "supplier")
    private List<ShoeEntity> shoes;

}
