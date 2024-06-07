package software.kasunkavinda.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MostSoldItemDTO {
    private String description;
    private Long quantity;
    private String picture;
}
