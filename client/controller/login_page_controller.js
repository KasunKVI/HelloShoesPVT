var emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
var passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^\da-zA-Z]).{8,}$/;

$("#login_btn").click(async function(event) {
    var userName = $("#login_email").val();
    var password = $("#login_password").val();

    let isEmailValid = emailPattern.test(userName);

    if (userName && isEmailValid) {
        let isPasswordValid = passwordPattern.test(password);

        if (password && isPasswordValid) {
            var hashPass = hashPassword(password);

            try {
                const response = await $.ajax({
                    type: "POST",
                    url: "http://localhost:8080/helloShoesPVT/api/v1/auth/signIn",
                    data: JSON.stringify({
                        email: userName,
                        password: hashPass
                    }),
                    contentType: "application/json"
                });

                const tokenString = response.token;
                const [accessToken, refreshToken] = tokenString.split(':').map(token => token.trim());

                localStorage.setItem('accessToken', accessToken);
                localStorage.setItem('refreshToken', refreshToken);

                $('#login_section').fadeOut('slow', function() {
                    $('#main_dashboard').fadeIn('slow');
                    $('#sidenav-main').fadeIn('slow');
                    $('#navbarBlur').fadeIn('slow');
                });
            } catch (error) {
                console.error("Login failed:", error);
            }
        }else {
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "Your Password or Email is invalid!",
            });
        }
    }else{

        Swal.fire({
            icon: "error",
            title: "Oops...",
            text: "Your Password or Email is invalid!",
        });

    }

});

function hashPassword(password){
    return sha256(password);
}