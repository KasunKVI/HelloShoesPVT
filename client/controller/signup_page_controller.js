var emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
var passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^\da-zA-Z]).{8,}$/;
var nicPattern = /^(?:\d{9}[Vv]|\d{12})$/;

var verification;
function validateUser() {
    let isValidUser = true;

    isValidUser &= validateField($('#signUp_nic'), nicPattern);
    isValidUser &= validateField($('#signUp_email'), emailPattern);
    isValidUser &= validateField($('#employee_name_add'), namePattern);
    isValidUser &= validateNotEmpty($('#signUp_pass'),passwordPattern);

    var password = $("#signUp_pass").val();
    var re_password = $("#signUp_pass_repeat").val();

    if (password !== re_password) {
        $("#signUp_pass_repeat").removeClass('valid').addClass('invalid');
        isValidUser = false;
    }else {
        $("#signUp_pass_repeat").removeClass('invalid').addClass('valid');
        isValidUser = true;
    }

    // Show custom alert if the form is invalid
    if (!isValidUser) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Please correct the highlighted fields'
        });
    }

    return isValidUser;
}

async function verifyUser(event) {


    $('#signup_form').css('display', 'none');
    $('#verification_form').css('display', 'block');


    try {
        const response = await $.ajax({
            type: "POST",
            url: "http://localhost:8081/helloShoesPVT/api/v1/auth/" + $("#signUp_email").val(),
            contentType: "application/json"
        });

        $('#register_new_user_btn').hide();
        verification = response;

        Swal.fire({
            icon: "success",
            title: "Verification code sent...!",
            showConfirmButton: false,
            timer: 1500
        });


    } catch (error) {
        console.error("SignUp failed:", error);
        Swal.fire({
            icon: "error",
            title: "Verification Not sent...",
            text: "Please try again later!",
        });

        return false;
    }


}
$("#verify_code_btn").click( async function () {


    if ($("#verification_code").val() === verification) {

        Swal.fire({
            icon: "success",
            title: "Verification successful...!",
            showConfirmButton: false,
            timer: 1500
        });

        $('#signup_section').fadeOut('slow', function () {
            $('#login_section').fadeIn('slow');
        });

    } else {
        Swal.fire({
            icon: "error",
            title: "Verification code incorrect...",
            text: "Please try again later!",
        });

        return false;
    }
});

$("#register_new_user_btn").click(async function (event) {

    if (validateUser()) {
        var password = $("#signUp_pass").val();

        var hashPass = hashPassword(password);
        let val = $("#signUp_role").val();
        var role;
        if (val === "Manager") {
            role="ADMIN";
        }else {
            role="USER";
        }

        const user = new UserDTO(
            $("#signUp_nic").val(),
            $("#signUp_email").val(),
            hashPass,
            role
        );

        try {
            const response = await $.ajax({
                type: "POST",
                url: "http://localhost:8081/helloShoesPVT/api/v1/auth/signUp",
                data: JSON.stringify(user),
                contentType: "application/json"
            });
            const tokenString = response.token;
            if (tokenString === "User already exists") {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'User already exists. Please try logging in.'
                });
            } else {
                const [accessToken, refreshToken] = tokenString.split(':').map(token => token.trim());
                localStorage.setItem('accessToken', accessToken);
                localStorage.setItem('refreshToken', refreshToken);

                await verifyUser();

            }

        } catch (error) {
            console.error("SignUp failed:", error);
        }





    }
});
$("#create_new_account_btn").click(async function (event) {

        await loadBranches();

});

function hashPassword(password) {
    return sha256(password);
}
function validateField(field, pattern) {
    if (!pattern.test(field.val().trim())) {
        field.removeClass('valid').addClass('invalid');
        return false;
    } else {
        field.removeClass('invalid').addClass('valid');
        return true;
    }
}
function validateNotEmpty(field) {
    if (!field.val().trim()) {
        field.removeClass('valid').addClass('invalid');
        return false;
    } else {
        field.removeClass('invalid').addClass('valid');
        return true;
    }
}
$('#signUp_nic').on('input', async function (event) {

    let isValid = validateField($(this), nicPattern);

    if (isValid) {

        let employee_id = $(this).val();


        try {
            const response = await $.ajax({
                type: "GET",
                url: "http://localhost:8081/helloShoesPVT/api/v1/auth/" + employee_id,

                contentType: "application/json"
            });

            $("#signUp_role").val(response.designation);
            $("#signUp_email").val(response.email);


        } catch (error) {
            console.error("Request failed:", error);
            $("#signUp_role").val("");
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "Employee not found!",
            });
        }

    }else {
        $("#signUp_role").val("");
    }

});

async function loadBranches() {
    try {

        $.ajax({
            url: 'http://localhost:8081/helloShoesPVT/api/v1/auth/getBranches',
            method: 'GET',
            dataType: 'json',
            success: function(data) {
                var branchSelect = $('#branch_signup');
                branchSelect.empty();
                branchSelect.append('<option value="S">Please Select the Branch</option>');
                $.each(data, function(index, branchName) {
                    branchSelect.append($('<option>', {
                        value: branchName,
                        text: branchName
                    }));
                });
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.error('Error fetching branches:', jqXHR, textStatus, errorThrown);
            }
        });
    } catch (error) {
        console.error('Error fetching branches:', error);
    }
}