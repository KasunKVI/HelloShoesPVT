package software.kasunkavinda.dto;

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
    private String name;
    private String email;
    private String contact;
    private String building_no;
    private String lane;
    private String city;
    private String state;
    private String postal_code;
    private String brand;
    private String category;

}
