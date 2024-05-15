package software.kasunkavinda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.kasunkavinda.enums.Payment_Method;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "orders")
public class OrderEntity implements SuperEntity {

    @Id
    private String order_id;
    private Date date;
    private double total;

    @Enumerated(EnumType.STRING)
    private Payment_Method payment_method;
    private int points;

    @ManyToOne
    private CustomerEntity customer;

    @ManyToOne
    private EmployeeEntity employee;

    @ManyToOne
    private BranchEntity branch;

    @OneToOne
    private RefundEntity refund;

    @ManyToMany(mappedBy = "orders")
    private List<ShoeEntity> items;

    @ManyToMany(mappedBy = "orders")
    private List<AccessoriesEntity> accessories;
}
