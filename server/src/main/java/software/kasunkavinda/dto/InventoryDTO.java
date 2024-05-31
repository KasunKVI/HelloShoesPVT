package software.kasunkavinda.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InventoryDTO {

    private String invt_id;
    private String description;

    @Column(columnDefinition = "LONGTEXT")
    private String picture;
    private int qty;
    private double bought_price;
    private double sell_price;
    private String type;
    private String supplier_id;

}
