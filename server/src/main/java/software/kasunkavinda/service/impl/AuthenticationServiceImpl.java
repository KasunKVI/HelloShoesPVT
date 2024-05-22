package software.kasunkavinda.service.impl;

import lombok.RequiredArgsConstructor;
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

    private final UserRepo userRepo;
    private final JwtService jwtService;
    private final EmployeeRepo employeeRepo;


    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final Mapping mapper;
    @Override
    public JwtAuthResponse signIn(SignIn signIn) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signIn.getEmail(),signIn.getPassword()));
        var userByEmail = userRepo.findByEmail(signIn.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var generatedToken = jwtService.generateToken(userByEmail);
        return JwtAuthResponse.builder().token(generatedToken).build() ;
    }

    @Override
    public JwtAuthResponse signUp(SignUp signUp) {

         boolean opt = userRepo.existsByEmail(signUp.getEmail());

            if (opt) {
                return JwtAuthResponse.builder().token("User already exists").build();
            }else {

                EmployeeEntity employeeEntity = employeeRepo.getReferenceById(signUp.getId());
                var buildUser = UserDTO.builder()
                        .id(UUID.randomUUID().toString())
                        .email(signUp.getEmail())
                        .password(passwordEncoder.encode(signUp.getPassword()))
                        .role(signUp.getRole())
                        .employeeDTO(mapper.toEmployeeDTO(employeeEntity))
                        .build();
                var savedUser = userRepo.save(mapper.toUserEntity(buildUser));
                var genToken = jwtService.generateToken(savedUser);
                return JwtAuthResponse.builder().token(genToken).build();
            }
    }

    @Override
    public JwtAuthResponse refreshToken(String accessToken) {
        var userName = jwtService.extractUsername(accessToken);
        var userEntity = userRepo.findByEmail(userName).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var refreshToken = jwtService.generateToken(userEntity);
        return JwtAuthResponse.builder().token(refreshToken).build();
    }
}
