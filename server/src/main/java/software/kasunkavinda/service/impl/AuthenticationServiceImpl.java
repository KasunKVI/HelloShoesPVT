package software.kasunkavinda.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.kasunkavinda.dao.EmployeeRepo;
import software.kasunkavinda.dao.UserRepo;
import software.kasunkavinda.dto.UserDTO;
import software.kasunkavinda.entity.EmployeeEntity;
import software.kasunkavinda.entity.UserEntity;
import software.kasunkavinda.reqAndresp.response.JwtAuthResponse;
import software.kasunkavinda.reqAndresp.secure.SignIn;
import software.kasunkavinda.reqAndresp.secure.SignUp;
import software.kasunkavinda.service.AuthenticationService;
import software.kasunkavinda.service.JwtService;
import software.kasunkavinda.util.Mapping;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private final UserRepo userRepo;
    private final JwtService jwtService;
    private final EmployeeRepo employeeRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final Mapping mapper;

    @Override
    public JwtAuthResponse signIn(SignIn signIn) {
        logger.info("Attempting to authenticate user: {}", signIn.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signIn.getEmail(), signIn.getPassword()));
        var userByEmail = userRepo.findByEmail(signIn.getEmail())
                .orElseThrow(() -> {
                    logger.warn("User not found: {}", signIn.getEmail());
                    return new UsernameNotFoundException("User not found");
                });
        var generatedToken = jwtService.generateToken(userByEmail);
        logger.info("Token generated for user: {}", signIn.getEmail());

        // Fetch branch information
        String branchId = null;
        String userId = null;
        if (userByEmail.getEmployee() != null && userByEmail.getEmployee().getBranch() != null) {
            branchId = userByEmail.getEmployee().getBranch().getBranch_id();
            userId = userByEmail.getEmployee().getEmployee_id();
        }

        logger.info("User signed in: {}, Branch ID: {}, Employee ID: {}", signIn.getEmail(), branchId, userId);

        // Return response with token and branch name
        return JwtAuthResponse.builder()
                .token(generatedToken)
                .branchId(branchId)
                .employeeId(userId)
                .build();
    }

    @Override
    public JwtAuthResponse signUp(SignUp signUp) {
        logger.info("Attempting to sign up user: {}", signUp.getEmail());
        boolean opt = userRepo.existsByEmail(signUp.getEmail());

        if (opt) {
            logger.warn("User already exists: {}", signUp.getEmail());
            return JwtAuthResponse.builder().token("User already exists").build();
        } else {
            EmployeeEntity employeeEntity = employeeRepo.getReferenceById(signUp.getId());
            var buildUser = UserEntity.builder()
                    .id(UUID.randomUUID().toString())
                    .email(signUp.getEmail())
                    .password(passwordEncoder.encode(signUp.getPassword()))
                    .role(signUp.getRole())
                    .employee(employeeEntity)
                    .build();
            var savedUser = userRepo.save(buildUser);
            var genToken = jwtService.generateToken(savedUser);

            // Fetch branch information
            String branchId = null;
            String userId = null;
            if (savedUser.getEmployee() != null && savedUser.getEmployee().getBranch() != null) {
                branchId = savedUser.getEmployee().getBranch().getBranch_id();
                userId = savedUser.getEmployee().getEmployee_id();
            }

            logger.info("User signed up: {}, Branch ID: {}, Employee ID: {}", signUp.getEmail(), branchId, userId);

            // Return response with token and branch name
            return JwtAuthResponse.builder()
                    .token(genToken)
                    .branchId(branchId)
                    .employeeId(userId)
                    .build();
        }
    }

    @Override
    public JwtAuthResponse refreshToken(String accessToken) {
        logger.info("Attempting to refresh token for access token: {}", accessToken);
        var userName = jwtService.extractUsername(accessToken);
        var userEntity = userRepo.findByEmail(userName).orElseThrow(() -> {
            logger.warn("User not found: {}", userName);
            return new UsernameNotFoundException("User not found");
        });
        var refreshToken = jwtService.generateToken(userEntity);
        logger.info("Token refreshed for user: {}", userName);

        // Fetch branch information
        String branchId = null;
        String userId = null;
        if (userEntity.getEmployee() != null && userEntity.getEmployee().getBranch() != null) {
            branchId = userEntity.getEmployee().getBranch().getBranch_id();
            userId = userEntity.getEmployee().getEmployee_id();
        }

        logger.info("Token refreshed for user: {}, Branch ID: {}, Employee ID: {}", userName, branchId, userId);

        // Return response with token and branch name
        return JwtAuthResponse.builder()
                .token(refreshToken)
                .branchId(branchId)
                .employeeId(userId)
                .build();
    }
}
