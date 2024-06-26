var emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
var nicPattern = /^(?:\d{9}[Vv]|\d{12})$/;
var namePattern = /^[a-zA-Z\s'-]+$/;
var postalCodePattern = /^[0-9]{5}$/;


var points;
var level;
var joindate;

function validateCustomerForm() {
    let isValid = true;

    isValid &= validateField($('#customer_nic_add'), nicPattern);
    isValid &= validateField($('#customer_email_add'), emailPattern);
    isValid &= validateField($('#customer_name_add'), namePattern);
    isValid &= validateNotEmpty($('#customer_address_add_building'));
    isValid &= validateNotEmpty($('#customer_address_add_lane'));
    isValid &= validateField($('#customer_address_add_postal_code'), postalCodePattern);
    isValid &= validateNotEmpty($('#customer_dob_add'));
    isValid &= validateNotEmpty($('#customer_gender_add'));

    // Show custom alert if the form is invalid
    if (!isValid) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Please correct the highlighted fields'
        });
    }

    return isValid;
}

// Add event listeners to validate fields in real-time
$('#customer_nic_add').on('input', function() {
    validateField($(this), nicPattern);
});

$('#customer_email_add').on('input', function() {
    validateField($(this), emailPattern);
});

$('#customer_name_add').on('input', function() {
    validateField($(this), namePattern);
});

$('#customer_address_add_building').on('input', function() {
    validateNotEmpty($(this));
});

$('#customer_address_add_lane').on('input', function() {
    validateNotEmpty($(this));
});

$('#customer_address_add_postal_code').on('input', function() {
    validateField($(this), postalCodePattern);
});

$('#customer_dob_add').on('input', function() {
    validateNotEmpty($(this));
});

$('#customer_gender_add').on('input', function() {
    validateNotEmpty($(this));
});

//////

$('#customer_nic_update').on('input', function() {
    validateField($(this), nicPattern);
});
$('#customer_name_update').on('input', function() {
    validateField($(this), namePattern);
});
$('#customer_email_update').on('input', function() {
    validateField($(this), emailPattern);
});
$('#customer_building_no_update').on('input', function() {
    validateNotEmpty($(this));
});
$('#customer_postcode_update').on('input', function() {
    validateField($(this),postalCodePattern);
});
$('#customer_lane_update').on('input', function() {
    validateNotEmpty($(this));
});
$('#customer_dob_update').on('input', function() {
    validateNotEmpty($(this));
});
$('#customer_gender_update').on('input', function() {
    validateNotEmpty($(this));
});

$('#customer_add_btn').click(function() {
    if (validateCustomerForm()) {
        const customer = new CustomerDTO(
            $('#customer_nic_add').val().trim(),
            $('#customer_name_add').val().trim(),
            $('#customer_gender_add').val(),
            new Date().toISOString().split('T')[0],
            $('#customer_dob_add').val().trim(),
            "NEW",
            0,
            $('#customer_address_add_building').val().trim(),
            $('#customer_address_add_lane').val().trim(),
            $('#customer_address_add_city').val().trim(),
            $('#customer_address_add_state').val().trim(),
            $('#customer_address_add_postal_code').val().trim(),
            $('#customer_email_add').val().trim(),

        );

        // Check and set empty string if city and state are null or undefined
        if (!customer.city) {
            customer.city = "";
        }
        if (!customer.state) {
            customer.state = "";
        }


        submitCustomerForm(customer,"Save");


    }
});

async function submitCustomerForm(customer, type) {

    const accessToken = localStorage.getItem('accessToken');

    if (type === "Save") {
        try {
            const response = await $.ajax({
                type: "POST",
                url: "http://localhost:8081/helloShoesPVT/api/v1/customer/save",
                headers: {
                    "Authorization": "Bearer " + accessToken
                },
                data: JSON.stringify(customer),
                contentType: "application/json"
            });

            // Assuming the response has a 'message' property
            if (response.message === "Customer saved successfully") {
                // Show a success message
                Swal.fire({
                    icon: "success",
                    title: response.message,
                    showConfirmButton: false,
                    timer: 1500
                });
                loadCustomers();
                clearCustomerAddForm();
                 loadLabels()

                $(".btn-close").click();

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
                text: error.responseJSON ? error.responseJSON.message : "You Have No Access to add new Customer."
            });
        }
    }else {
        try {
            const response = await $.ajax({
                type: "PUT",
                url: "http://localhost:8081/helloShoesPVT/api/v1/customer",
                headers: {
                    "Authorization": "Bearer " + accessToken
                },
                data: JSON.stringify(customer),
                contentType: "application/json"
            });

            if (response.message === "Customer updated successfully") {
                Swal.fire({
                    icon: "success",
                    title: response.message,
                    showConfirmButton: false,
                    timer: 1500
                });
                loadCustomers();
                await searchCustomer(customer.customer_id);
                loadLabels();
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
                text: error.responseJSON ? error.responseJSON.message : "You have no access to Update this Customer"
            });
        }
    }

}

async function searchCustomer(customerId) {

    // Retrieve the access token from localStorage
    const accessToken = localStorage.getItem('accessToken');

    try {
        const response = await $.ajax({
            type: "GET",
            url: "http://localhost:8081/helloShoesPVT/api/v1/customer/" + customerId,
            headers: {
                "Authorization": "Bearer " + accessToken
            },
            contentType: "application/json"
        });

        // Populate the form fields with the retrieved customer details
        $("#customer_nic_update").val(response.customer_id);
        $("#customer_name_update").val(response.name);
        $("#customer_email_update").val(response.email);
        $("#customer_building_no_update").val(response.building_no);
        $("#customer_lane_update").val(response.lane);
        $("#customer_city_update").val(response.city);
        $("#customer_state_update").val(response.state);
        $("#customer_postcode_update").val(response.postal_code);
        $("#customer_gender_update").val(response.gender);
        points = response.points;
        level = response.level;
        joindate = response.joined_date;

        // Assuming response.dob contains the date of birth in YYYY-MM-DD format
        const dob = new Date(response.dob);

        // Format the date as YYYY-MM-DD to set it in the input field
        const formattedDob = dob.toISOString().split('T')[0];

        // Set the formatted date in the DOB input field
        $("#customer_dob_update").val(formattedDob);

        var updateButtonText = document.getElementById("customer_btn_update").textContent;

            // Display the customer details section
            $("#customer_details_section").show();


    } catch (error) {
        console.error("Request failed:", error);
        Swal.fire({
            icon: "error",
            title: "Oops...",
            text: "Customer not found!",
        });


        $("#customer_details_section").hide();
    }
};

function validateCustomerUpdateForm() {

    let isValid = true;

    isValid &= validateField($('#customer_nic_update'), nicPattern);
    isValid &= validateField($('#customer_email_update'), emailPattern);
    isValid &= validateField($('#customer_name_update'), namePattern);
    isValid &= validateNotEmpty($('#customer_building_no_update'));
    isValid &= validateNotEmpty($('#customer_lane_update'));
    isValid &= validateField($('#customer_postcode_update'), postalCodePattern);
    isValid &= validateNotEmpty($('#customer_dob_update'));
    isValid &= validateNotEmpty($('#customer_gender_update'));

    // Show custom alert if the form is invalid
    if (!isValid) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Please correct the highlighted fields'
        });
    }

    return isValid;
}

$("#customer_btn_update").click(async function (event) {

    if (validateCustomerUpdateForm()) {
        const customer = new CustomerDTO(
            $('#customer_nic_update').val().trim(),
            $('#customer_name_update').val().trim(),
            $('#customer_gender_update').val(),
            joindate,
            $('#customer_dob_update').val().trim(),
            level,
            points,
            $('#customer_building_no_update').val().trim(),
            $('#customer_lane_update').val().trim(),
            $('#customer_city_update').val().trim(),
            $('#customer_state_update').val().trim(),
            $('#customer_postcode_update').val().trim(),
            $('#customer_email_update').val().trim(),

        );

        submitCustomerForm(customer,"Update");
    }

});

// Define the clear update form function
function clearCustomerUpdateForm() {
    $(".btn-close").click();

}

$("#customer_btn_clear_add").click(function() {
    // Call the clear update form function
    clearCustomerAddForm();
});

function clearCustomerAddForm() {

    // Clear the input fields in the add form
    $('#customer_nic_add').val('');
    $('#customer_name_add').val('');
    $('#customer_email_add').val('');
    $('#customer_address_add_building').val('');
    $('#customer_address_add_lane').val('');
    $('#customer_address_add_city').val('');
    $('#customer_address_add_state').val('');
    $('#customer_address_add_postal_code').val('');
    $('#customer_dob_add').val('');
    $('#customer_gender_add').val('');



}

// Attach the clearAddForm function to the Clear button click event
$("#customer_btn_close_update").click(function() {
    clearCustomerUpdateForm();
});

$("#customers_link").click(function(event) {
    loadCustomers();
});
const loadCustomers = () => {

    // Retrieve the access token from localStorage
    const accessToken = localStorage.getItem('accessToken');



    $.ajax({
            type:"GET",
            url: "http://localhost:8081/helloShoesPVT/api/v1/customer",
            headers: {
                "Authorization": "Bearer " + accessToken
            },
            contentType: "application/json",

            success: function (response) {

                console.log(response);

                $('#customer_table_body').empty();

                response.map((customer, index) => {

                    // Parse the joined_date to extract year, month, and date
                    const joinedDate = new Date(customer.joined_date);
                    const formattedJoinedDate = `${joinedDate.getFullYear()}-${joinedDate.getMonth() + 1}-${joinedDate.getDate()}`;

                    // Parse the dob to extract year, month, and date
                    const dobDate = new Date(customer.dob);
                    const formattedDobDate = `${dobDate.getFullYear()}-${dobDate.getMonth() + 1}-${dobDate.getDate()}`;

                    let tbl_row = `<tr data-customer-id=${customer.customer_id}> <td class="customer_id"><p>${customer.customer_id}</p></td><td class="customer_name"><p class="text-xs font-weight-bold mb-0">${customer.name}</p></td><td class="customer_contact_no"><p class="text-center  mb-0">${customer.gender}</p></td><td class="customer_dob"><p class="text-center  mb-0">${formattedJoinedDate}</p></td><td class="customer_email align-middle text-center text-sm"><span>${formattedDobDate}</span></td><td class="customer_gender align-middle text-center"><span class="badge badge-sm bg-gradient-success">${customer.level}</span></td><td class="customer_address"><p class="text-center  mb-0">${customer.points}</p></td><td class="customer_address"><p class="text-center  mb-0">${customer.building_no+ ", " + customer.lane + ", " + customer.city + ", " + customer.state + ", " + customer.postal_code}</p></td><<td class="customer_email"><p class="text-center  mb-0">${customer.email}</p></td>
            <td class="align-middle white-space-nowrap text-end pe-0">
            <div class="btn-reveal-trigger position-static align-middle" >
            <button class="btn btn-sm dropdown-toggle dropdown-caret-none transition-none btn-reveal fs-10" type="button" data-bs-toggle="dropdown" data-boundary="window" aria-haspopup="true" aria-expanded="false" data-bs-reference="parent"><span class="fas fa-ellipsis-h fs-10"></span></button>
              <div class="dropdown-menu dropdown-menu-end py-2">
              <a class="dropdown-item view-action" href="#!" data-bs-toggle="modal" data-bs-target="#updateModelCustomer" data-customer-id="${customer.customer_id}">View</a>
              <a class="dropdown-item  update-action text-danger" href="#!" data-bs-toggle="modal" data-bs-target="#updateModelCustomer" data-customer-id="${customer.customer_id}">Update</a>
              <div class="dropdown-divider"></div>
              <a class="dropdown-item delete-action text-warning" href="#!" data-bs-target="#updateModelCustomer" id="customer_delete_btn" data-customer-id="${customer.customer_id}">Remove</a>
              </div>
            </div>
          </td>
                         </tr>`;
                    $('#customer_table_body').append(tbl_row);
                });

                attachEventListeners();

            },
            error: function (xhr, status, error) {
                console.error('Something Error');
            }
        });


};

// Function to attach event listeners
const attachEventListeners = () => {
    // Event listener for view action
    $('.view-action').click(function (e) {
        e.preventDefault();
        const customerId = $(this).data('customer-id');

        // Change the modal title to "Customer Info"
        $("#popupModalLabelCustomerUpdate").text("Customer Info");

        // Hide the Update button
        $("#customer_btn_update").hide();

        // Hide the Clear button
        $("#customer_btn_clear_update").hide();

        // Disable all input fields
        $("#customer_name_update, #customer_email_update, #customer_postcode_update,#customer_state_update,#customer_lane_update,#customer_building_no_update,#customer_city_update, #customer_dob_update, #customer_gender_update, #customer_nic_update").prop("disabled", true);

        searchCustomer(customerId);



    });

    // Event listener for update action
    $('.update-action').click(function (e) {

        e.preventDefault();

        const customerId = $(this).data('customer-id');

        // Change the modal title to "Update Customer"
        $("#popupModalLabelCustomerUpdate").text("Update Customer");

        // Show the Update button
        $("#customer_btn_update").show();

        // Enable all input fields
        $("#customer_name_update, #customer_email_update, #customer_postcode_update,#customer_state_update,#customer_lane_update,#customer_building_no_update,#customer_city_update, #customer_dob_update, #customer_gender_update").prop("disabled", false);

        searchCustomer(customerId);

    });

    // Event listener for delete action
    $('.delete-action').click(async function (e) {
        e.preventDefault();
        const customerId = $(this).data('customer-id');
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
                        url: "http://localhost:8081/helloShoesPVT/api/v1/customer/" + customerId,
                        headers: {
                            "Authorization": "Bearer " + accessToken
                        },
                        contentType: "application/json"
                    });

                    swalWithBootstrapButtons.fire({
                        title: "Deleted!",
                        text: "Customer has been deleted.",
                        icon: "success"
                    });

                    loadCustomers();
                    clearCustomerUpdateForm();
                    loadLabels();

                } catch (error) {
                    console.error("Request failed:", error);

                    Swal.fire({
                        icon: "error",
                        title: "Oops...",
                        text: "You have no access to Delete this Customer!",
                    });
                }


            } else if (
                result.dismiss === Swal.DismissReason.cancel
            ) {
                swalWithBootstrapButtons.fire({
                    title: "Cancelled",
                    text: "Customer is safe :)",
                    icon: "error"
                });
            }
        });


    });
};



 async function refreshAccessToken() {
    try {
        const refreshToken = localStorage.getItem('refreshToken');
        const response = await $.ajax({
            type: "POST",
            url: "http://localhost:8081/helloShoesPVT/api/v1/auth/refresh",
            data: JSON.stringify({
                refreshToken: refreshToken
            }),
            contentType: "application/json"
        });

        const newTokenString = response.token;
        const [newAccessToken, newRefreshToken] = newTokenString.split(':').map(token => token.trim());

        localStorage.setItem('accessToken', newAccessToken);
        localStorage.setItem('refreshToken', newRefreshToken);
    } catch (error) {
        console.error("Refreshing access token failed:", error);
        // Redirect to login page or handle the error appropriately
    }
}