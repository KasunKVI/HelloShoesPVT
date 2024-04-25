package software.kasunkavinda.service;

import software.kasunkavinda.reqAndresp.response.JwtAuthResponse;
import software.kasunkavinda.reqAndresp.secure.SignIn;
import software.kasunkavinda.reqAndresp.secure.SignUp;

public interface AuthenticationService {
    JwtAuthResponse signIn(SignIn signIn);
    JwtAuthResponse signUp(SignUp signUp);
}
