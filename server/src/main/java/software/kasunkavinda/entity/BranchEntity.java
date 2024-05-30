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

    @ManyToMany
    private List<ShoeEntity> shoes;

    @ManyToMany
    private List<AccessoriesEntity> accessories;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
    private List<OrderEntity> orders;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
    private List<EmployeeEntity> employees;

}
