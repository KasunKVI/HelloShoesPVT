package software.kasunkavinda.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Order date cannot be null")
    private Date date;

    @NotNull(message = "Total amount cannot be null")
    private Double total;

    @NotNull(message = "Payment method cannot be null")
    private Payment_Method payment_method;

    @NotNull(message = "Points cannot be null")
    private Integer points;

    @NotEmpty(message = "Customer ID cannot be empty")
    private String customer_id;

    @NotEmpty(message = "Employee ID cannot be empty")
    private String employee_id;

    @NotEmpty(message = "Branch ID cannot be empty")
    private String branch_id;

    private String refund_id;

    private List<ShoeDTO> shoes;

    private List<AccessoriesDTO> accessories;

}
