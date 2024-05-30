package software.kasunkavinda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.kasunkavinda.enums.Gender;
import software.kasunkavinda.enums.Role;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "employee")
public class EmployeeEntity implements SuperEntity {

    @Id
    private String employee_id;
    private String name;
    @Column(columnDefinition = "LONGTEXT")
    private String profile_pic;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String building_no;
    private String lane;
    private String city;
    private String state;
    private String postal_code;
    private String status;

    @Column(unique = true)
    private String email;
    private String designation;
    private String contact;
    private String emergency_contact;
    private Date joined_date;
    private Date dob;
    private String guardian_name;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private BranchEntity branch;

    @OneToMany(mappedBy = "employee")
    private List<OrderEntity> orders;

    @OneToMany(mappedBy = "employee")
    private List<RefundEntity> refunds;

    @OneToOne
    private UserEntity user;

}
