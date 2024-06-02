package software.kasunkavinda.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.kasunkavinda.enums.Category;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SupplierDTO {

    private String supplier_id;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Contact cannot be blank")
    private String contact;

    @NotBlank(message = "Building number cannot be blank")
    private String building_no;

    @NotBlank(message = "Lane cannot be blank")
    private String lane;


    private String city;
    private String state;

    @NotBlank(message = "Postal code cannot be blank")
    @Pattern(regexp = "^[0-9]{5}(?:-[0-9]{4})?$", message = "Invalid postal code format")
    private String postal_code;

    @NotBlank(message = "Brand cannot be blank")
    private String brand;

    @NotBlank(message = "Category cannot be blank")
    private String category;

}
