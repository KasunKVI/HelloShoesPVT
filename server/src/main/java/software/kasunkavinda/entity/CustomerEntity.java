package software.kasunkavinda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.kasunkavinda.enums.Gender;
import software.kasunkavinda.enums.Level;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "customer")
public class CustomerEntity implements SuperEntity{

    @Id
    private String customer_id;
    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    private Date joined_date;
    private Date dob;

    @Enumerated(EnumType.STRING)
    private Level level;
    private int points;
    private String address;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "customer")
    private List<OrderEntity> orders;

}
