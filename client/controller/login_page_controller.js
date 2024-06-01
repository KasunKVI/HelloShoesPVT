var emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
var passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^\da-zA-Z]).{8,}$/;

function validLogin() {
    let isValidLogins = true;

    isValidLogins &= validateField($('#login_email'), emailPattern);
    isValidLogins &= validateField($('#login_password'), passwordPattern);

    // Show custom alert if the form is invalid
    if (!isValidLogins) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Please correct the highlighted fields'
        });
    }

    return isValidLogins;
}
// function validateField(field, pattern) {
//     if (!pattern.test(field.val().trim())) {
//         field.removeClass('is-valid').addClass('is-invalid');
//         return false;
//     } else {
//         field.removeClass('is-invalid').addClass('is-valid');
//         return true;
//     }
// }

$("#login_btn").click(async function(event) {

    var userName = $("#login_email").val();
    var password = $("#login_password").val();
    var hashPass = hashPassword(password);

    if(validLogin()) {
        try {
            const response = await $.ajax({
                type: "POST",
                url: "http://localhost:8081/helloShoesPVT/api/v1/auth/signIn",
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

            // Store the branch name
            const branchId = response.branchId;
            localStorage.setItem('branchId', branchId);

            $('#login_section').fadeOut('slow', function () {
                $('#main_dashboard').fadeIn('slow');
                $('#sidenav-main').fadeIn('slow');
                $('#navbarBlur').fadeIn('slow');
            });
        } catch (error) {
            console.error("Login failed:", error);
        }
    }

});

function hashPassword(password){
    return sha256(password);
}

$('#login_email').on('input', function() {
    validateField($(this), emailPattern);
});

$('#login_password').on('input', function() {
    validateField($(this), passwordPattern);
});