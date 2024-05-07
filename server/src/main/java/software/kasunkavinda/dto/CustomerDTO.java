package software.kasunkavinda.dto;

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
    private String name;
    private Gender gender;
    private Date joined_date;
    private Date dob;
    private Level level;
    private int points;
    private String address;
    private String email;
}
