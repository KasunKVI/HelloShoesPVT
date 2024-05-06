package software.kasunkavinda.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.Mapping;
import software.kasunkavinda.dao.UserRepo;
import software.kasunkavinda.dto.UserDTO;
import software.kasunkavinda.service.UserService;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
//    private final Mapping map;
    @Override
    public UserDetailsService userDetailsService() {
        return username ->
                userRepo.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public void save(UserDTO user) {
//        map.toUserDTO(userRepo.save(map.toUserEntity(user)));
    }

}
