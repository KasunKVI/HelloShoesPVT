package software.kasunkavinda.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InventoryDTO {

    private String invt_id;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @ToString.Exclude
    @NotBlank(message = "Picture cannot be blank")
    private String picture;

    @Min(value = 0, message = "Quantity must be zero or greater")
    private int qty;

    @Positive(message = "Bought price must be positive")
    private double bought_price;

    @Positive(message = "Sell price must be positive")
    private double sell_price;

    @NotBlank(message = "Type cannot be blank")
    private String type;

    @NotNull(message = "Supplier ID cannot be null")
    private String supplier_id;

}
