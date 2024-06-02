package software.kasunkavinda.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Column(columnDefinition = "LONGTEXT")
    private String profile_pic;

    @NotBlank(message = "Gender cannot be blank")
    private String gender;

    @NotBlank(message = "Building number cannot be blank")
    private String building_no;

    @NotBlank(message = "Lane cannot be blank")
    private String lane;

    private String city;
    private String state;

    @NotBlank(message = "Postal code cannot be blank")
    private String postal_code;

    @NotBlank(message = "Status cannot be blank")
    private String status;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Designation cannot be blank")
    private String designation;

    @NotBlank(message = "Contact cannot be blank")
    private String contact;

    @NotBlank(message = "Emergency contact cannot be blank")
    private String emergency_contact;

    @NotNull(message = "Joined date cannot be null")
    @PastOrPresent(message = "Joined date must be in the past or present")
    private Date joined_date;

    @NotNull(message = "Date of birth cannot be null")
    @Past(message = "Date of birth should be in the past")
    private Date dob;

    @NotBlank(message = "Guardian name cannot be blank")
    private String guardian_name;

    @NotBlank(message = "Branch ID cannot be blank")
    private String branchId;
}
