package software.kasunkavinda.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.kasunkavinda.entity.BranchEntity;
import software.kasunkavinda.entity.OrderEntity;
import software.kasunkavinda.entity.RefundEntity;
import software.kasunkavinda.entity.UserEntity;
import software.kasunkavinda.enums.Gender;
import software.kasunkavinda.enums.Role;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EmployeeDTO {

    private String employee_id;
    private String name;

    @Column(columnDefinition = "LONGTEXT")
    private String profile_pic;

    private Gender gender;

    private String building_no;
    private String lane;
    private String city;
    private String state;
    private String postal_code;
    private String status;

    private String email;
    private String designation;

    private String contact;

    private String emergency_contact;
    private Date joined_date;
    private Date dob;
    private String guardian_name;

}
