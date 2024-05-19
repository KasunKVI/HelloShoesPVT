package software.kasunkavinda.reqAndresp.secure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.kasunkavinda.enums.Role;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SignUp {

    private String id;
    private String email;
    private String password;
    private String role;

}
