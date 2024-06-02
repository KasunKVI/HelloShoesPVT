package software.kasunkavinda.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.kasunkavinda.enums.Gender;
import software.kasunkavinda.enums.Level;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerDTO {

    private String customer_id;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Gender cannot be null")
    private Gender gender;

    @NotNull(message = "Joined date cannot be null")
    @PastOrPresent(message = "Joined date must be in the past or present")
    private Date joined_date;

    @NotNull(message = "Date of birth cannot be null")
    @Past(message = "Date of birth should be in the past")
    private Date dob;

    @NotNull(message = "Level cannot be null")
    private Level level;

    @PositiveOrZero(message = "Points must be positive or zero")
    private int points;

    @NotBlank(message = "Building number cannot be blank")
    private String building_no;

    @NotBlank(message = "Lane cannot be blank")
    private String lane;

    private String city;
    private String state;

    @NotBlank(message = "Postal code cannot be blank")
    private String postal_code;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;
}
