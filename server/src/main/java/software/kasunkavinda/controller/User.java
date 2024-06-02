package software.kasunkavinda.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.kasunkavinda.dto.BranchDTO;
import software.kasunkavinda.dto.EmployeeDTO;
import software.kasunkavinda.reqAndresp.response.JwtAuthResponse;
import software.kasunkavinda.reqAndresp.secure.SignIn;
import software.kasunkavinda.reqAndresp.secure.SignUp;
import software.kasunkavinda.service.AuthenticationService;
import software.kasunkavinda.service.BranchService;
import software.kasunkavinda.service.EmailService;
import software.kasunkavinda.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class User {

    private static final Logger logger = LoggerFactory.getLogger(User.class);

    private final AuthenticationService authenticationService;
    private final EmployeeService employeeService;
    private final EmailService emailService;
    private final BranchService branchService;

    @PostMapping("/signIn")
    public ResponseEntity<JwtAuthResponse> signIn(@RequestBody SignIn signIn) {
        logger.info("SignIn request received for user: {}", signIn.getEmail());
        try {
            JwtAuthResponse response = authenticationService.signIn(signIn);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error during signIn: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/signUp")
    public ResponseEntity<JwtAuthResponse> signUp(@RequestBody SignUp signUpReq) {
        logger.info("SignUp request received for user: {}", signUpReq.getEmail());
        try {
            JwtAuthResponse response = authenticationService.signUp(signUpReq);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error during signUp: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthResponse> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        logger.info("Refresh token request received");
        try {
            JwtAuthResponse response = authenticationService.refreshToken(refreshToken);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error during token refresh: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable String id) {
        logger.info("Fetching employee with ID: {}", id);
        try {
            EmployeeDTO employee = employeeService.getSelectedEmployee(id);
            return ResponseEntity.ok(employee);
        } catch (Exception e) {
            logger.error("Error fetching employee by ID: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{email}")
    public ResponseEntity<String> sendVerification(@PathVariable String email) {
        logger.info("Sending verification code to email: {}", email);
        try {
            String response = emailService.sendVerificationCode(email);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error sending verification code: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getBranches")
    public ResponseEntity<List<String>> getAllBranches() {
        logger.info("Fetching all branch names");
        try {
            List<String> branchNames = branchService.getAllBranchNames();
            return ResponseEntity.ok(branchNames);
        } catch (Exception e) {
            logger.error("Error fetching branch names: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
