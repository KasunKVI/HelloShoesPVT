package software.kasunkavinda.controller;

import lombok.RequiredArgsConstructor;
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

    private final AuthenticationService authenticationService;
    private final EmployeeService employeeService;
    private final EmailService emailService;
    private final BranchService branchService;

    @PostMapping("/signIn")
    public ResponseEntity<JwtAuthResponse> signIn (@RequestBody SignIn signIn){
        return ResponseEntity.ok(authenticationService.signIn(signIn));
    }

    @PostMapping("/signUp")
    public ResponseEntity<JwtAuthResponse> signUp(@RequestBody SignUp signUpReq) {
        return ResponseEntity.ok(authenticationService.signUp(signUpReq));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthResponse> refreshToken(@RequestParam ("refreshToken") String refreshToken) {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshToken));
    }

    @GetMapping("/{id}")
    public EmployeeDTO getEmployeeById(@PathVariable String id){
        return employeeService.getSelectedEmployee(id);
    }

    @PostMapping("/{email}")
    public String sendVerification(@PathVariable String email){
        return emailService.sendVerificationCode(email);
    }

    @GetMapping("/getBranches")
    public List<String> getAllBranches(){
        return branchService.getAllBranchNames();
    }

}
