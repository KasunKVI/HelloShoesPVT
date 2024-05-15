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
    private String profile_pic;
    private Gender gender;
    private String address;
    private String status;
    private String email;
    private String designation;
    private String contact;
    private Role role;
    private String emergency_contact;
    private Date joined_date;
    private Date dob;
    private String guardian_name;

    @ManyToOne
    private BranchEntity branch;

    @OneToMany(mappedBy = "employee")
    private List<OrderEntity> orders;

    @OneToMany(mappedBy = "employee")
    private List<RefundEntity> refunds;

    @OneToOne
    private UserEntity user;

}
