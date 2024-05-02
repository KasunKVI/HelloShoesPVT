var emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
var passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^\da-zA-Z]).{8,}$/;

$("#login_btn").click(function (event) {



    var userName = $("#login_email").val();
    var password = $("#login_password").val();

    let isEmailValid =emailPattern.test(userName)

    if (userName && isEmailValid){
        let isPasswordValid = passwordPattern.test(password);


        if (password && isPasswordValid){
            var hashPass = hashPassword(password);

            $.ajax({
                type: "POST",
                url: "http://localhost:8080/helloShoesPVT/api/v1/auth/signIn",
                data: JSON.stringify({
                    email: userName,
                    password: hashPass
                }),
                contentType: "application/json",
                success: function(response) {
                    // Handle successful response from the backend
                    console.log("Login successful");

                    $('#login_section').fadeOut('slow', function() {
                        $('#main_dashboard').fadeIn('slow');
                        $('#sidenav-main').fadeIn('slow');
                        $('#navbarBlur').fadeIn('slow');
                    });
                },
                error: function(xhr, status, error) {
                    // Handle error response from the backend
                    console.error("Login failed:", error);
                }
            });
        }
    }



});

function hashPassword(password){
    return sha256(password);
}