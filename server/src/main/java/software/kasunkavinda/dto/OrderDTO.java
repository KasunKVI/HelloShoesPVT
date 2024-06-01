package software.kasunkavinda.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.kasunkavinda.enums.Payment_Method;

import java.util.Date;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class OrderDTO {

    private String order_id;
    private Date date;
    private double total;
    private Payment_Method payment_method;
    private int points;

    private String customer_id;

    private String employee_id;

    private String branch_id;

    private List<ShoeDTO> shoes;

    private List<AccessoriesDTO> accessories;

}
