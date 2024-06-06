package software.kasunkavinda.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RefundDTO {

    private String order_id;
    private Date date;
    private String employee_id;
}
