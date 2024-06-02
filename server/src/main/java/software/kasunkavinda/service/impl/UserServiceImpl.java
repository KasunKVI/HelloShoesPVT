package software.kasunkavinda.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import software.kasunkavinda.dao.UserRepo;
import software.kasunkavinda.dto.UserDTO;
import software.kasunkavinda.service.UserService;
import software.kasunkavinda.util.Mapping;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final Mapping map;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserDetailsService userDetailsService() {
        return username -> {
            logger.info("Attempting to load user by username: {}", username);
            return userRepo.findByEmail(username)
                    .orElseThrow(() -> {
                        logger.warn("User not found with username: {}", username);
                        return new UsernameNotFoundException("User not found");
                    });
        };
    }

    @Override
    public void save(UserDTO user) {
        logger.info("Attempting to save user with email: {}", user.getEmail());
        userRepo.save(map.toUserEntity(user));
        logger.info("User saved successfully with email: {}", user.getEmail());
    }
}
