package software.kasunkavinda.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "refund")
public class RefundEntity implements SuperEntity{

    @Id
    private String refund_id;
    private Date date;
    private double amount;

    @ManyToOne
    private EmployeeEntity employee;

    @OneToOne
    private OrderEntity order;
}
