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
@Table(name = "branch")
public class BranchEntity implements SuperEntity {

    @Id
    private String branch_id;
    private String name;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "branch_shoes",
            joinColumns = @JoinColumn(name = "branch_id"),
            inverseJoinColumns = @JoinColumn(name = "shoe_id")
    )
    private List<ShoeEntity> shoes;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "branch_accessories",
            joinColumns = @JoinColumn(name = "branch_id"),
            inverseJoinColumns = @JoinColumn(name = "accessories_id")
    )
    private List<AccessoriesEntity> accessories;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
    private List<OrderEntity> orders;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
    private List<EmployeeEntity> employees;

}
