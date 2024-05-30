package software.kasunkavinda.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EmployeeDTO {

    private String employee_id;
    private String name;

    @Column(columnDefinition = "LONGTEXT")
    private String profile_pic;
    private String gender;
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
    private String branchId;
}
