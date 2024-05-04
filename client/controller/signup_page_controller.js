var emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
var passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^\da-zA-Z]).{8,}$/;
var nicPattern = /^(19[2-9]\d|20[0-1]\d)\d{8}|^[2-9]\d{8}V$/;

$("#register_new_user_btn").click(async function (event) {

    var nic = $("#signUp_nic").val();
    var email = $("#signUp_email").val();
    var password = $("#signUp_pass").val();
    var re_password = $("#signUp_pass_repeat").val();
    var role = $("#job_role_select").val();

    let isNicValid = nicPattern.test(nic);

    if (nic && isNicValid){

        let isEmailValid = emailPattern.test(email);

        if (email && isEmailValid){

            let isPasswordValid = passwordPattern.test(password);

            if (password && isPasswordValid){

                if (password === re_password){

                    if (role){

                        var hashPass = hashPassword(password);

                        try {
                            const response = await $.ajax({
                                type: "POST",
                                url: "http://localhost:8080/helloShoesPVT/api/v1/auth/signUp",
                                data: JSON.stringify({
                                    id: nic,
                                    email: email,
                                    password: hashPass,
                                    role: role
                                }),
                                contentType: "application/json"
                            });

                            const tokenString = response.token;
                            const [accessToken, refreshToken] = tokenString.split(':').map(token => token.trim());

                            localStorage.setItem('accessToken', accessToken);
                            localStorage.setItem('refreshToken', refreshToken);

                            $('#signup_section').fadeOut('slow', function() {
                                $('#main_dashboard').fadeIn('slow');
                                $('#sidenav-main').fadeIn('slow');
                                $('#navbarBlur').fadeIn('slow');
                            });
                        } catch (error) {
                            console.error("SignUp failed:", error);
                        }


                    }else {
                        Swal.fire({
                            icon: "error",
                            title: "Oops...",
                            text: "Role Not Selected!",
                        });
                    }
                }else {
                    Swal.fire({
                        icon: "error",
                        title: "Oops...",
                        text: "Your Password ReEnter Not Correct!",
                    });
                }
            }else {
                Swal.fire({
                    icon: "error",
                    title: "Oops...",
                    text: "Your Password must contain at least one Uppercase letter, Lowercase letter, Symbol, Number and more than 8 characters!",
                });
            }
        }else {
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "Your Email is invalid!",
            });
        }
    }else {
        Swal.fire({
            icon: "error",
            title: "Oops...",
            text: "Your Nic is invalid!",
        });
    }

});

function hashPassword(password) {
    return sha256(password);
}