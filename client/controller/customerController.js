
var points;
var level;
var joindate;

$("#customer_add_btn").click(async function (event) {

    // Retrieve the access token from localStorage
    const accessToken = localStorage.getItem('accessToken');

    var nic = $("#customer_nic_add").val();
    var email = $("#customer_email_add").val();
    var name = $("#customer_name_add").val();
    var building_no = $("#customer_address_add_building").val();
    var lane = $("#customer_address_add_lane").val();
    var state = $("#customer_address_add_state").val();
    var city = $("#customer_address_add_city").val();
    var postal_code = $("#customer_address_add_postal_code").val();
    var dob = $("#customer_dob_add").val();
    var gender = $("#customer_gender_add").val();
    var joindate = new Date().toISOString().split('T')[0];
    var level = "NEW";
    var points = 0;

    try {
        const response = await $.ajax({
            type: "POST",
            url: "http://localhost:8080/helloShoesPVT/api/v1/customer",
            headers: {
                "Authorization": "Bearer " + accessToken
            },
            data: JSON.stringify({
                customer_id: nic,
                name: name,
                gender: gender,
                joined_date: joindate,
                dob: dob,
                level: level,
                points: points,
                building_no: building_no,
                lane: lane,
                city: city,
                state: state,
                postal_code:postal_code,
                email: email
            }),
            contentType: "application/json"
        });
        Swal.fire({
            icon: "success",
            title: "Customer has been saved",
            showConfirmButton: false,
            timer: 1500
        });

        loadCustomers();
        clearAddForm();

    } catch (error) {
        console.error("Request failed:", error);
    }
});

$("#customer_search_btn").click(async function (event) {

    // Retrieve the access token from localStorage
    const accessToken = localStorage.getItem('accessToken');

    var nic = $("#customer_nic_search").val();


    try {
        const response = await $.ajax({
            type: "GET",
            url: "http://localhost:8080/helloShoesPVT/api/v1/customer/" + nic,
            headers: {
                "Authorization": "Bearer " + accessToken
            },
            contentType: "application/json"
        });

        Swal.fire({
            icon: "success",
            title: "Customer Founded!",
            showConfirmButton: false,
            timer: 1500
        });
        // Populate the form fields with the retrieved customer details
        $("#customer_name_update").val(response.name);
        $("#customer_email_update").val(response.email);
        $("#customer_address_update").val(response.building_no+ ", " + response.lane + ", " + response.city + ", " + response.state + ", " + response.postal_code);
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

        if (updateButtonText === "Delete") {

        }else {
            // Display the customer details section
            $("#customer_details_section").show();
        }


    } catch (error) {
        console.error("Request failed:", error);
        Swal.fire({
            icon: "error",
            title: "Oops...",
            text: "Customer not found!",
        });


        $("#customer_details_section").hide();
    }
});
$("#customer_btn_update").click(async function (event) {

    // Get the text of the Update button
    var updateButtonText = document.getElementById("customer_btn_update").textContent;

    // Retrieve the access token from localStorage
    const accessToken = localStorage.getItem('accessToken');

    var nic = $("#customer_nic_search").val();

    if (updateButtonText === "Delete") {

        Swal.fire({
            title: "Please Search first for ensure that the customer is in the database",
            showClass: {
                popup: `
      animate__animated
      animate__fadeInUp
      animate__faster
    `
            },
            hideClass: {
                popup: `
      animate__animated
      animate__fadeOutDown
      animate__faster
    `
            }
        });

        try {
            const response = await $.ajax({
                type: "DELETE",
                url: "http://localhost:8080/helloShoesPVT/api/v1/customer/" + nic,
                headers: {
                    "Authorization": "Bearer " + accessToken
                },
                contentType: "application/json"
            });
            Swal.fire({
                icon: "success",
                title: "Customer has been deleted",
                showConfirmButton: false,
                timer: 1500
            });

            loadCustomers();

        } catch (error) {
            console.error("Request failed:", error);

            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "Customer not found!",
            });
        }

    }else if(updateButtonText === "Update"){


        var email = $("#customer_email_update").val();
        var name = $("#customer_name_update").val();
        var address = $("#customer_address_update").val();
        var dob = $("#customer_dob_update").val();
        var gender = $("#customer_gender_update").val();


        try {
            const response = await $.ajax({
                type: "PUT",
                url: "http://localhost:8080/helloShoesPVT/api/v1/customer",
                headers: {
                    "Authorization": "Bearer " + accessToken
                },
                data: JSON.stringify({
                    customer_id: nic,
                    name: name,
                    gender: gender,
                    joined_date: joindate,
                    dob: dob,
                    level: level,
                    points: points,
                    address: address,
                    email: email
                }),
                contentType: "application/json"
            });
            Swal.fire({
                icon: "success",
                title: "Customer has been updated",
                showConfirmButton: false,
                timer: 1500
            });
            loadCustomers();
            clearUpdateForm();
        } catch (error) {
            console.error("Request failed:", error);
        }

    }




});

// Define the clear update form function
function clearUpdateForm() {
    // Clear the input values of all form fields
    $("#customer_name_update").val("");
    $("#customer_email_update").val("");
    $("#customer_address_update").val("");
    $("#customer_dob_update").val("");
    $("#customer_gender_update").val(""); // Assuming this should be reset to the default value

    // Hide the customer details section
    $("#customer_details_section").hide();
}

$("#customer_btn_clear").click(function() {
    // Call the clear update form function
    clearUpdateForm();
});

function clearAddForm() {
    // Clear the input fields in the add form
    $("#customer_nic_add").val("");
    $("#customer_name_add").val("");
    $("#customer_email_add").val("");
    $("#customer_address_add_building").val("");
    $("#customer_address_add_lane").val("");
    $("#customer_address_add_state").val("");
    $("#customer_address_add_city").val("");
    $("#customer_address_add_postal_code").val("");
    $("#customer_dob_add").val("");
    $("#customer_gender_add").val("MALE"); // Assuming Male is the default gender option

}

// Attach the clearAddForm function to the Clear button click event
$("#customer_btn_clear_add").click(function() {
    clearAddForm();
});

$("#customer_info_btn").click(function() {

    // Change the modal title to "Customer Info"
    $("#popupModalLabelCustomerUpdate").text("Customer Info");

    // Hide the Update button
    $("#customer_btn_update").hide();

    // Hide the Clear button
    $("#customer_btn_clear_update").hide();

    // Disable all input fields
    $("#customer_name_update, #customer_email_update, #customer_address_update, #customer_dob_update, #customer_gender_update").prop("disabled", true);
});
$("#customer_update_btn").click(function() {

    // Change the modal title to "Update Customer"
    $("#popupModalLabelCustomerUpdate").text("Update Customer");

    // Show the Update button
    $("#customer_btn_update").show();

    // Hide the customer details section
    $("#customer_details_section").hide();

    // Enable all input fields
    $("#customer_name_update, #customer_email_update, #customer_address_update, #customer_dob_update, #customer_gender_update").prop("disabled", false);
});

$("#customer_delete_btn").click(function(event) {

    event.preventDefault()

    // Change the modal title to "Update Customer"
    $("#popupModalLabelCustomerUpdate").text("Delete Customer");

    // Change the label of the Clear button to "Close"
    $("#customer_btn_update").text("Delete");

    // Hide the customer details section
    $("#input_section_update").hide();
    //
    // // Show the Update button
    // $("#customer_btn_update").show();

    // // Enable all input fields
    // $("#customer_name_update, #customer_email_update, #customer_address_update, #customer_dob_update, #customer_gender_update").hide();
});

$("#customers_link").click(function(event) {
    console.log("Customer link clicked");
    loadCustomers();
});
const loadCustomers = () => {

    // Retrieve the access token from localStorage
    const accessToken = localStorage.getItem('accessToken');

    $('#customer_table_body').empty();

    $.ajax({
            type:"GET",
            url: "http://localhost:8080/helloShoesPVT/api/v1/customer",
            headers: {
                "Authorization": "Bearer " + accessToken
            },
            contentType: "application/json",

            success: function (response) {


                response.map((customer, index) => {
                    let tbl_row = `<tr><td class="customer_id"><p>${customer.customer_id}</p></td><td class="customer_name"><p class="text-xs font-weight-bold mb-0">${customer.name}</p></td><td class="customer_contact_no"><p class="text-center  mb-0">${customer.gender}</p></td><td class="customer_dob"><p class="text-center  mb-0">${customer.joined_date}</p></td><td class="customer_email align-middle text-center text-sm"><span>${customer.dob}</span></td><td class="customer_gender align-middle text-center"><span class="badge badge-sm bg-gradient-success">${customer.level}</span></td><td class="customer_address"><p class="text-center  mb-0">${customer.points}</p></td><td class="customer_address"><p class="text-center  mb-0">${customer.building_no+ ", " + customer.lane + ", " + customer.city + ", " + customer.state + ", " + customer.postal_code}</p></td><<td class="customer_email"><p class="text-center  mb-0">${customer.email}</p></td>/tr>`;
                    $('#customer_table_body').append(tbl_row);
                });


            },
            error: function (xhr, status, error) {
                console.error('Something Error');
            }
        });


};



