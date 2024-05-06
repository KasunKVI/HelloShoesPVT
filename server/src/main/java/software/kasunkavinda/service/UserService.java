package software.kasunkavinda.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import software.kasunkavinda.dto.UserDTO;

public interface UserService {
    UserDetailsService userDetailsService();
    void save(UserDTO user);
}
