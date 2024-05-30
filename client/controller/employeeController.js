var emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
var nicPattern = /^(?:\d{9}[Vv]|\d{12})$/;
var namePattern = /^[a-zA-Z\s'-]+$/;
var postalCodePattern = /^[0-9]{5}$/;
var mobileNumberPattern = /^07[\d]{8}$/;


var joindate;
var profilepic;
$("#employees_link").click(function(event) {
    loadEmployees();
});
const loadEmployees = () => {

    // Retrieve the access token from localStorage
    const accessToken = localStorage.getItem('accessToken');

    $('#employee_table_body').empty();

    $.ajax({
        type:"GET",
        url: "http://localhost:8081/helloShoesPVT/api/v1/employee/getAll/" + localStorage.getItem('branchId'),
        headers: {
            "Authorization": "Bearer " + accessToken
        },
        contentType: "application/json",


        success: function (response) {

            console.log(response)

            response.map((employee, index) => {

                // Parse the joined_date to extract year, month, and date
                const joinedDate = new Date(employee.joined_date);
                const formattedJoinedDate = `${joinedDate.getFullYear()}-${joinedDate.getMonth() + 1}-${joinedDate.getDate()}`;

                // Parse the dob to extract year, month, and date
                const dobDate = new Date(employee.dob);
                const formattedDobDate = `${dobDate.getFullYear()}-${dobDate.getMonth() + 1}-${dobDate.getDate()}`;


                let tbl_row = `<tr data-employee-id=${employee.employee_id}>
                    <td class="employee_id"><p>${employee.employee_id}</p></td>
                    <td class="employee_name"><p class="text-xs font-weight-bold mb-0">${employee.name}</p></td>
                    <td class="employee_profile_pic text-center"><img src="${employee.profile_pic}" alt="Profile Pic" style="width: 50px; height: 50px; border-radius: 50%;"></td>
                    <td class="employee_gender"><p class="text-center mb-0">${employee.gender}</p></td>
                    <td class="employee_status"><p class="text-center mb-0">${employee.status}</p></td>
                    <td class="employee_designation align-middle text-center text-sm"><span class="badge badge-sm bg-gradient-success">${employee.designation}</span></td>
                    <td class="employee_joined_date"><p class="text-center mb-0">${formattedJoinedDate}</p></td>
                    <td class="employee_dob align-middle text-center"><span>${formattedDobDate}</span></td>
                    <td class="employee_address"><p class="text-center mb-0">${employee.building_no + ", " + employee.lane + ", " + employee.city + ", " + employee.state + ", " + employee.postal_code}</p></td>
                    <td class="employee_contact"><p class="text-center mb-0">${employee.contact}</p></td>
                    <td class="employee_email"><p class="text-center mb-0">${employee.email}</p></td>
                    <td class="employee_emergency_contact"><p class="text-center mb-0">${employee.emergency_contact}</p></td>
                    <td class="employee_guardian_name"><p class="text-center mb-0">${employee.guardian_name}</p></td>
                    <td class="align-middle white-space-nowrap text-end pe-0">
                        <div class="btn-reveal-trigger position-static align-middle">
                            <button class="btn btn-sm dropdown-toggle dropdown-caret-none transition-none btn-reveal fs-10" type="button" data-bs-toggle="dropdown" data-boundary="window" aria-haspopup="true" aria-expanded="false" data-bs-reference="parent">
                                <span class="fas fa-ellipsis-h fs-10"></span>
                            </button>
                            <div class="dropdown-menu dropdown-menu-end py-2">
                                <a class="dropdown-item view-action-employee" href="#!" data-bs-toggle="modal" data-bs-target="#updateModelEmployee" data-employee-id="${employee.employee_id}">View</a>
                                <a class="dropdown-item update-action-employee text-danger" href="#!" data-bs-toggle="modal" data-bs-target="#updateModelEmployee" data-employee-id="${employee.employee_id}">Update</a>
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item delete-action-employee text-warning" href="#!" data-bs-target="#updateModelEmployee" id="employee_delete_btn" data-employee-id="${employee.employee_id}">Remove</a>
                            </div>
                        </div>
                    </td>
                </tr>`;
                $('#employee_table_body').append(tbl_row);
            });

            attachEventListenersEmployee()

        },
        error: function (xhr, status, error) {
            console.error('Something Error');
        }
    });


};

// Function to convert image file to base64 string
function toBase64(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = error => reject(error);
    });
}

// Event listener for profile picture input
$('#employee_profile_pic').on('change', async function () {
    const file = this.files[0];
    if (file) {
        const base64 = await toBase64(file);
        $('#profile_pic_preview').attr('src', base64); // Set image preview
        $('#employee_profile_pic').data('base64', base64); // Store base64 string in data attribute
    }
});
$('#employee_profile_pic_update').on('change', async function () {
    const file = this.files[0];
    if (file) {
        const base64 = await toBase64(file);
        $('#profile_pic_preview_update').attr('src', base64); // Set image preview
        $('#employee_profile_pic_update').data('base64', base64); // Store base64 string in data attribute
    }
});

$('#employee_add_btn').click(function() {

    const profilePicBase64 = $('#employee_profile_pic').data('base64') || "";
    const branchId = localStorage.getItem('branchId');

    if (validateEmployeeForm()) {

        const employee = new EmployeeDTO(
            $('#employee_id_add').val().trim(),
            $('#employee_name_add').val().trim(),
            profilePicBase64,
            $('#employee_gender_add').val(),
            $('#employee_address_add_building').val(),
            $('#employee_address_add_lane').val(),
            $('#employee_address_add_city').val(),
            $('#employee_address_add_state').val(),
            $('#employee_address_add_postal_code').val(),
            $('#employee_status_add').val(),
            $('#employee_email_add').val(),
            $('#employee_designation_add').val(),
            $('#employee_contact_add').val(),
            $('#employee_emergency_contact').val(),
            new Date().toISOString().split('T')[0],
            $('#employee_dob_add').val().trim(),
            $('#employee_guardian_add').val().trim(),
            branchId,

        );

        // Check and set empty string if city and state are null or undefined
        if (!employee.city) {
            employee.city = "";
        }
        if (!employee.state) {
            employee.state = "";
        }


        submitEmployeeForm(employee,"Save");

    }
});

// Add event listeners to validate fields in real-time
$('#employee_id_add').on('input', function() {
    validateField($(this), nicPattern);
});

$('#employee_email_add').on('input', function() {
    validateField($(this), emailPattern);
});

$('#employee_name_add').on('input', function() {
    validateField($(this), namePattern);
});

$('#employee_address_add_building').on('input', function() {
    validateNotEmpty($(this));
});
$('#employee_profile_pic').on('input', function() {
    validateNotEmpty($(this));
});

$('#employee_address_add_lane').on('input', function() {
    validateNotEmpty($(this));
});

$('#employee_address_add_postal_code').on('input', function() {
    validateField($(this), postalCodePattern);
});

$('#employee_dob_add').on('input', function() {
    validateNotEmpty($(this));
});

$('#employee_gender_add').on('input', function() {
    validateNotEmpty($(this));
});
$('#employee_guardian_add').on('input', function() {
    validateField($(this),namePattern);
});
$('#employee_contact_add').on('input', function() {
    validateField($(this),mobileNumberPattern);
});
$('#employee_emergency_contact').on('input', function() {
    validateField($(this),mobileNumberPattern);
});
$('#employee_status_add').on('input', function() {
    validateNotEmpty($(this));
});
$('#employee_designation_add').on('input', function() {
    validateNotEmpty($(this));
});

/////

$('#employee_id_update').on('input', function() {
    validateField($(this), nicPattern);
});

$('#employee_email_update').on('input', function() {
    validateField($(this), emailPattern);
});

$('#employee_name_update').on('input', function() {
    validateField($(this), namePattern);
});

$('#employee_address_update_building').on('input', function() {
    validateNotEmpty($(this));
});

$('#employee_address_update_lane').on('input', function() {
    validateNotEmpty($(this));
});

$('#employee_address_update_postal_code').on('input', function() {
    validateField($(this), postalCodePattern);
});

$('#employee_dob_update').on('input', function() {
    validateNotEmpty($(this));
});

$('#employee_gender_update').on('input', function() {
    validateNotEmpty($(this));
});
$('#employee_guardian_update').on('input', function() {
    validateField($(this),namePattern);
});
$('#employee_contact_update').on('input', function() {
    validateField($(this),mobileNumberPattern);
});
$('#employee_emergency_contact_update').on('input', function() {
    validateField($(this),mobileNumberPattern);
});
$('#employee_status_update').on('input', function() {
    validateNotEmpty($(this));
});
$('#employee_designation_update').on('input', function() {
    validateNotEmpty($(this));
});

function validateEmployeeForm() {

    let isValidEmployee = true;

    isValidEmployee &= validateField($('#employee_id_add'), nicPattern);
    isValidEmployee &= validateField($('#employee_email_add'), emailPattern);
    isValidEmployee &= validateField($('#employee_name_add'), namePattern);
    isValidEmployee &= validateNotEmpty($('#employee_address_add_building'));
    isValidEmployee &= validateNotEmpty($('#employee_status_add'));
    isValidEmployee &= validateNotEmpty($('#employee_designation_add'));
    isValidEmployee &= validateNotEmpty($('#employee_profile_pic'));
    isValidEmployee &= validateField($('#employee_contact_add'), mobileNumberPattern);
    isValidEmployee &= validateField($('#employee_emergency_contact'), mobileNumberPattern);
    isValidEmployee &= validateField($('#employee_guardian_add'), namePattern);
    isValidEmployee &= validateNotEmpty($('#employee_address_add_lane'));
    isValidEmployee &= validateField($('#employee_address_add_postal_code'), postalCodePattern);
    isValidEmployee &= validateNotEmpty($('#employee_dob_add'));
    isValidEmployee &= validateNotEmpty($('#employee_gender_add'));

    // Show custom alert if the form is invalid
    if (!isValidEmployee) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Please correct the highlighted fields'
        });
    }

    return isValidEmployee;
}

function validateField(field, pattern) {
    if (!pattern.test(field.val())) {
        field.removeClass('valid').addClass('invalid');
        return false;
    } else {
        field.removeClass('invalid').addClass('valid');
        return true;
    }
}

function validateNotEmpty(field) {
    if (!field.val()) {
        field.removeClass('valid').addClass('invalid');
        return false;
    } else {
        field.removeClass('invalid').addClass('valid');
        return true;
    }
}
$("#employee_btn_clear_add").click(function() {
    // Call the clear update form function
    clearEmployeeAddForm();
});
function clearEmployeeAddForm() {

    $('#employee_id_add').val('');
    $('#employee_name_add').val('');
    $('#employee_profile_pic').val('');
    $('#profile_pic_preview').attr('src', '/assets/img/pngwing.com.png');
    $('#employee_email_add').val('');
    $('#employee_contact_add').val('');
    $('#employee_gender_add').val('');
    $('#employee_designation_add').val('');
    $('#employee_status_add').val('');
    $('#employee_guardian_add').val('');
    $('#employee_emergency_contact').val('');
    $('#employee_dob_add').val('');
    $('#employee_address_add_building').val('');
    $('#employee_address_add_lane').val('');
    $('#employee_address_add_city').val('');
    $('#employee_address_add_state').val('');
    $('#employee_address_add_postal_code').val('');

}

async function submitEmployeeForm(employee, type) {

    const accessToken = localStorage.getItem('accessToken');

    if (type === "Save") {

        if (employee.designation === "Manager") {
            employee.role = "ADMIN";
        }else {
            employee.role = "USER";
        }



        try {
            console.log(employee);
            const response = await $.ajax({
                type: "POST",
                url: "http://localhost:8081/helloShoesPVT/api/v1/employee/save",
                headers: {
                    "Authorization": "Bearer " + accessToken
                },
                data: JSON.stringify(employee),
                contentType: "application/json"
            });


            // Assuming the response has a 'message' property
            if (response.message === "Employee saved successfully") {
                // Show a success message
                Swal.fire({
                    icon: "success",
                    title: response.message,
                    showConfirmButton: false,
                    timer: 1500
                });

                loadEmployees();
                clearEmployeeAddForm();

            }else {
                Swal.fire({
                    icon: "error",
                    title: response.message,
                    showConfirmButton: false,
                    timer: 1500
                });
            }
        } catch (error) {
            console.error("Request failed:", error);
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text:  "You have no access to add new employee."
            });
        }
    }else {
        try {

            const response = await $.ajax({
                type: "PUT",
                url: "http://localhost:8081/helloShoesPVT/api/v1/employee/update",
                headers: {
                    "Authorization": "Bearer " + accessToken
                },
                data: JSON.stringify(employee),
                contentType: "application/json"
            });

            if (response.message === "Employee updated successfully") {
                Swal.fire({
                    icon: "success",
                    title: response.message,
                    showConfirmButton: false,
                    timer: 1500
                });
                 loadEmployees();
                 await searchEmployee(employee.employee_id);

            }else {
                Swal.fire({
                    icon: "error",
                    title: response.message,
                    showConfirmButton: false,
                    timer: 1500
                });

            }

        } catch (error) {
            console.error("Request failed:", error);
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: error.responseJSON ? error.responseJSON.message : "You have no access to Update this Employee"
            });
        }
    }

}

async function searchEmployee(employeeId) {

    // Retrieve the access token from localStorage
    const accessToken = localStorage.getItem('accessToken');

    try {
        const response = await $.ajax({
            type: "GET",
            url: "http://localhost:8081/helloShoesPVT/api/v1/employee/" + employeeId,
            headers: {
                "Authorization": "Bearer " + accessToken
            },
            contentType: "application/json"
        });

        // Populate the form fields with the retrieved customer details
        $("#employee_id_update").val(response.employee_id);
        $("#employee_name_update").val(response.name);
        $("#profile_pic_preview_update").attr('src', response.profile_pic);
        profilepic = response.profile_pic;
        $("#employee_gender_add").val(response.gender);
        $("#employee_address_update_building").val(response.building_no);
        $("#employee_address_update_lane").val(response.lane);
        $("#employee_address_update_city").val(response.city);
        $("#employee_address_update_state").val(response.state);
        $("#employee_address_update_postal_code").val(response.postal_code);
        $("#employee_status_update").val(response.status);
        $("#employee_guardian_update").val(response.guardian_name);
        $("#employee_emergency_contact_update").val(response.emergency_contact);
        $("#employee_designation_update").val(response.designation);
        $("#employee_email_update").val(response.email);
        $("#employee_contact_update").val(response.contact);


        joindate= response.joined_date;
        // Assuming response.dob contains the date of birth in YYYY-MM-DD format
        const dob = new Date(response.dob);

        // Format the date as YYYY-MM-DD to set it in the input field
        const formattedDob = dob.toISOString().split('T')[0];

        // Set the formatted date in the DOB input field
        $("#employee_dob_update").val(formattedDob);

    } catch (error) {
        console.error("Request failed:", error);
        Swal.fire({
            icon: "error",
            title: "Oops...",
            text: "Employee not found!",
        });

    }

}

const attachEventListenersEmployee = () => {

    // Event listener for view action
    $('.view-action-employee').click(function (e) {

        e.preventDefault();
        const employeeId = $(this).data('employee-id');

        console.log(employeeId);
        // Change the modal title to "Customer Info"
        $("#popupModalLabelEmployeeUpdate").text("Employee Info");

        // Hide the Update button
        $("#employee_add_Update").hide();


        // Disable all input fields
        $("#employee_profile_pic_update, #employee_id_update, #employee_name_update,#employee_email_update,#employee_contact_update,#employee_gender_update,#employee_designation_update, #employee_status_update, #employee_guardian_update, #employee_emergency_contact_update,#employee_dob_update,#employee_address_update_building,#employee_address_update_lane,#employee_address_update_city,#employee_address_update_state,#employee_address_update_postal_code").prop("disabled", true);
        $("#employee_profile_pic_update").hide()
        searchEmployee(employeeId);



    });

    // Event listener for update action
    $('.update-action-employee').click(function (e) {

        e.preventDefault();

        const employeeId = $(this).data('employee-id');

        // Change the modal title to "Update Customer"
        $("#popupModalLabelEmployeeUpdate").text("Employee Customer");

        // Show the Update button
        $("#employee_add_Update").show();

        // Enable all input fields
        $("#employee_profile_pic_update, #employee_id_update, #employee_name_update,#employee_email_update,#employee_contact_update,#employee_gender_update,#employee_designation_update, #employee_status_update, #employee_guardian_update, #employee_emergency_contact_update,#employee_dob_update,#employee_address_update_building,#employee_address_update_lane,#employee_address_update_city,#employee_address_update_state,#employee_address_update_postal_code").prop("disabled", false);
        $("#employee_profile_pic_update").show()

        searchEmployee(employeeId);

    });

    // Event listener for delete action
    $('.delete-action-employee').click(async function (e) {
        e.preventDefault();
        const employeeId = $(this).data('employee-id');

        // Retrieve the access token from localStorage
        const accessToken = localStorage.getItem('accessToken');


        const swalWithBootstrapButtons = Swal.mixin({
            customClass: {
                confirmButton: "btn btn-success",
                cancelButton: "btn btn-danger"
            },
            buttonsStyling: false
        });
        swalWithBootstrapButtons.fire({
            title: "Are you sure?",
            text: "You won't be able to revert this!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonText: "Yes, delete it!",
            cancelButtonText: "No, cancel!",
            reverseButtons: true
        }).then(async (result) => {

            if (result.isConfirmed) {

                try {
                    const response = await $.ajax({
                        type: "DELETE",
                        url: "http://localhost:8081/helloShoesPVT/api/v1/employee/" + employeeId,
                        headers: {
                            "Authorization": "Bearer " + accessToken
                        },
                        contentType: "application/json"
                    });

                    swalWithBootstrapButtons.fire({
                        title: "Deleted!",
                        text: "Employee has been deleted.",
                        icon: "success"
                    });

                    loadEmployees();
                    clearEmployeeUpdateForm()

                } catch (error) {
                    console.error("Request failed:", error);

                    Swal.fire({
                        icon: "error",
                        title: "Oops...",
                        text: "You have no access to Delete this Employee!",
                    });
                }


            } else if (
                result.dismiss === Swal.DismissReason.cancel
            ) {
                swalWithBootstrapButtons.fire({
                    title: "Cancelled",
                    text: "Employee is safe :)",
                    icon: "error"
                });
            }
        });


    });
};

function clearEmployeeUpdateForm() {
    $(".btn-close").click();
}

$("#employee_btn_close_Update").click(function() {
    // Call the clear update form function
    clearEmployeeUpdateForm();
});

function validateEmployeeUpdateForm() {

        let isValidEmployee = true;

        isValidEmployee &= validateField($('#employee_id_update'), nicPattern);
        isValidEmployee &= validateField($('#employee_email_update'), emailPattern);
        isValidEmployee &= validateField($('#employee_name_update'), namePattern);
        isValidEmployee &= validateNotEmpty($('#employee_address_update_building'));
        isValidEmployee &= validateNotEmpty($('#employee_status_update'));
        isValidEmployee &= validateNotEmpty($('#employee_designation_update'));
        isValidEmployee &= validateField($('#employee_contact_update'), mobileNumberPattern);
        isValidEmployee &= validateField($('#employee_emergency_contact_update'), mobileNumberPattern);
        isValidEmployee &= validateField($('#employee_guardian_update'), namePattern);
        isValidEmployee &= validateNotEmpty($('#employee_address_update_lane'));
        isValidEmployee &= validateField($('#employee_address_update_postal_code'), postalCodePattern);
        isValidEmployee &= validateNotEmpty($('#employee_dob_update'));
        // isValidEmployee &= validateNotEmpty($('#employee_profile_pic_update'));


    // Show custom alert if the form is invalid
    if (!isValidEmployee) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Please correct the highlighted fields'
        });
    }

    return isValidEmployee;
}

$("#employee_add_Update").click(async function (event) {

    let profilePicUpdateBase64 = $('#employee_profile_pic_update').data('base64') || "";

    if (profilePicUpdateBase64 === "") {
        profilePicUpdateBase64 = profilepic;
    }

    let branchId = localStorage.getItem('branchId');

    if (validateEmployeeUpdateForm()) {
        const employee = new EmployeeDTO(
            $('#employee_id_update').val().trim(),
            $('#employee_name_update').val().trim(),
            profilePicUpdateBase64,
            $('#employee_gender_update').val(),
            $('#employee_address_update_building').val(),
            $('#employee_address_update_lane').val(),
            $('#employee_address_update_city').val(),
            $('#employee_address_update_state').val(),
            $('#employee_address_update_postal_code').val(),
            $('#employee_status_update').val(),
            $('#employee_email_update').val(),
            $('#employee_designation_update').val(),
            $('#employee_contact_update').val(),
            $('#employee_emergency_contact_update').val(),
            joindate,
            $('#employee_dob_update').val().trim(),
            $('#employee_guardian_update').val().trim(),
            branchId,

        );

        submitEmployeeForm(employee,"Update");
    }

});