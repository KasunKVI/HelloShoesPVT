var emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
var namePattern = /^[a-zA-Z\s'-]+$/;
var postalCodePattern = /^[0-9]{5}$/;
var mobileNumberPattern = /^07[\d]{8}$/;

var supplier_id;

function validateSupplierForm() {

    let isValidSupplier = true;

    isValidSupplier &= validateNotEmpty($('#supplier_id_add'));
    isValidSupplier &= validateField($('#supplier_email_add'), emailPattern);
    isValidSupplier &= validateField($('#supplier_name_add'), namePattern);
    isValidSupplier &= validateNotEmpty($('#supplier_building_add'));
    isValidSupplier &= validateNotEmpty($('#supplier_lane_add'));
    isValidSupplier &= validateField($('#supplier_postal_code_add'),postalCodePattern);
    isValidSupplier &= validateField($('#supplier_contact_add'), mobileNumberPattern);
    isValidSupplier &= validateNotEmpty($('#supplier_brand_add'));
    isValidSupplier &= validateNotEmpty($('#supplier_category_add'));


    // Show custom alert if the form is invalid
    if (!isValidSupplier) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Please correct the highlighted fields'
        });
    }

    return isValidSupplier;

}

function generateNewSupplierId(latestId) {
    if (!latestId) {
        return "S001";
    }

    // Extract numeric part and increment
    let numericPart = parseInt(latestId.substring(1), 10);
    numericPart += 1;

    // Format the new ID with leading zeros
    let newId = "S" + String(numericPart).padStart(3, '0');
    return newId;
}

async function getNewSupplierId() {

    const accessToken = localStorage.getItem('accessToken');

    try {
        const response = await $.ajax({
            type: "GET",
            url: "http://localhost:8081/helloShoesPVT/api/v1/supplier/latest",
            headers: {
                "Authorization": "Bearer " + accessToken
            },
        });

        $('#supplier_id_add').val(generateNewSupplierId(response)) ;

    }catch (error) {
        console.error("Request failed:", error);
        Swal.fire({
            icon: "error",
            title: "Oops...",
            text:  "Error fetching supplier id. Please try again later"
        });
    }

}
$('#load_supplier_add_btn').click(async function() {
    getNewSupplierId();
});

function validateSupplierUpdateForm() {
    let isValidSupplierUpdate = true;

    isValidSupplierUpdate &= validateField($('#supplier_email_update'), emailPattern);
    isValidSupplierUpdate &= validateField($('#supplier_name_update'), namePattern);
    isValidSupplierUpdate &= validateNotEmpty($('#supplier_building_update'));
    isValidSupplierUpdate &= validateNotEmpty($('#supplier_lane_update'));
    isValidSupplierUpdate &= validateField($('#supplier_postal_code_update'), postalCodePattern);
    isValidSupplierUpdate &= validateField($('#supplier_contact_update'), mobileNumberPattern);
    isValidSupplierUpdate &= validateNotEmpty($('#supplier_brand_update'));
    isValidSupplierUpdate &= validateNotEmpty($('#supplier_category_update'));

    // Show custom alert if the form is invalid
    if (!isValidSupplierUpdate) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Please correct the highlighted fields'
        });
    }

    return isValidSupplierUpdate;
}

// Add event listeners to validate fields in real-time
$('#supplier_name_add').on('input', function() {
    validateField($(this), namePattern);
});

$('#supplier_email_add').on('input', function() {
    validateField($(this), emailPattern);
});

$('#supplier_building_add').on('input', function() {
    validateNotEmpty($(this));
});
$('#supplier_lane_add').on('input', function() {
    validateNotEmpty($(this));
});

$('#supplier_postal_code_add').on('input', function() {
    validateField($(this), postalCodePattern);
});

$('#supplier_contact_add').on('input', function() {
    validateField($(this),mobileNumberPattern);
});

$('#supplier_brand_add').on('input', function() {
    validateNotEmpty($(this));
});
$('#supplier_category_add').on('input', function() {
    validateNotEmpty($(this));
});

/////////

$('#supplier_name_update').on('input', function() {
    validateField($(this), namePattern);
});

$('#supplier_email_update').on('input', function() {
    validateField($(this), emailPattern);
});

$('#supplier_building_update').on('input', function() {
    validateNotEmpty($(this));
});
$('#supplier_lane_update').on('input', function() {
    validateNotEmpty($(this));
});

$('#supplier_postal_code_update').on('input', function() {
    validateField($(this), postalCodePattern);
});

$('#supplier_contact_update').on('input', function() {
    validateField($(this),mobileNumberPattern);
});

$('#supplier_brand_update').on('input', function() {
    validateNotEmpty($(this));
});
$('#supplier_category_update').on('input', function() {
    validateNotEmpty($(this));
});


$("#supplier_update_btn").click(async function (event) {

    if (validateSupplierUpdateForm()) {
        const supplier = new SupplierDTO(
            $('#supplier_id_Update').val().trim(),
            $('#supplier_name_update').val().trim(),
            $('#supplier_email_update').val(),
            $('#supplier_contact_update').val().trim(),
            $('#supplier_building_update').val().trim(),
            $('#supplier_lane_update').val().trim(),
            $('#supplier_city_update').val().trim(),
            $('#supplier_state_update').val().trim(),
            $('#supplier_postal_code_update').val().trim(),
            $('#supplier_brand_update').val().trim(),
            $('#supplier_category_update').val().trim(),

        );

        submitSupplierForm(supplier,"Update");
    }

});

async function submitSupplierForm(supplier, type) {

    const accessToken = localStorage.getItem('accessToken');

    if (type === "Save") {

        console.log("Supplier")
        try {
            const response = await $.ajax({
                type: "POST",
                url: "http://localhost:8081/helloShoesPVT/api/v1/supplier/save",
                headers: {
                    "Authorization": "Bearer " + accessToken
                },
                data: JSON.stringify(supplier),
                contentType: "application/json"
            });

            // Assuming the response has a 'message' property
            if (response.message === "Supplier saved successfully") {
                // Show a success message
                Swal.fire({
                    icon: "success",
                    title: response.message,
                    showConfirmButton: false,
                    timer: 1500
                });

                 loadSuppliers();
                 clearSupplierAddForm();

            } else {
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
                text: "You have no access to add new supplier."
            });
        }
    } else {
        try {
            const response = await $.ajax({
                type: "PUT",
                url: "http://localhost:8081/helloShoesPVT/api/v1/supplier",
                headers: {
                    "Authorization": "Bearer " + accessToken
                },
                data: JSON.stringify(supplier),
                contentType: "application/json"
            });

            if (response.message === "Supplier updated successfully") {
                Swal.fire({
                    icon: "success",
                    title: response.message,
                    showConfirmButton: false,
                    timer: 1500
                });

                loadSuppliers();
                $(".btn-close").click();

            } else {
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
                text: error.responseJSON ? error.responseJSON.message : "You have no access to Update this Supplier"
            });
        }
    }

}

function clearSupplierAddForm() {

    // Clear the input fields in the add form
    $('#supplier_id_add').val('');
    $('#supplier_email_add').val('');
    $('#supplier_name_add').val('');
    $('#supplier_building_add').val('');
    $('#supplier_city_add').val('');
    $('#supplier_lane_add').val('');
    $('#supplier_postal_code_add').val('');
    $('#supplier_contact_add').val('');
    $('#supplier_brand_add').val('');
    $('#supplier_category_add').val('');
}

$('#supplier_add_btn').click(function() {


    if (validateSupplierForm()) {
        console.log("Sfegrefe")
        const supplier = new SupplierDTO(
            $('#supplier_id_add').val(),
            $('#supplier_name_add').val().trim(),
            $('#supplier_email_add').val().trim(),
            $('#supplier_contact_add').val().trim(),
            $('#supplier_building_add').val().trim(),
            $('#supplier_lane_add').val().trim(),
            $('#supplier_city_add').val().trim(),
            $('#supplier_state_add').val().trim(),
            $('#supplier_postal_code_add').val().trim(),
            $('#supplier_brand_add').val().trim(),
            $('#supplier_category_add').val(),
        );

        submitSupplierForm(supplier,"Save");


    }
});



function validateField(field, pattern) {
    if (pattern.test(field.val())) {
        field.removeClass('is-invalid').addClass('is-valid');
        return true;
    } else {
        field.removeClass('is-valid').addClass('is-invalid');
        return false;
    }
}

function validateNotEmpty(field) {
    if (field.val().trim() !== '') {
        field.removeClass('is-invalid').addClass('is-valid');
        return true;
    } else {
        field.removeClass('is-valid').addClass('is-invalid');
        return false;
    }
}
async function searchSupplier(supplierId) {

    // Retrieve the access token from localStorage
    const accessToken = localStorage.getItem('accessToken');

    try {
        const response = await $.ajax({
            type: "GET",
            url: "http://localhost:8081/helloShoesPVT/api/v1/supplier/" + supplierId,
            headers: {
                "Authorization": "Bearer " + accessToken
            },
            contentType: "application/json"
        });


            $('#supplier_id_Update').val(response.supplier_id);
            $('#supplier_name_update').val(response.name);
            $('#supplier_email_update').val(response.email);
            $('#supplier_contact_update').val(response.contact);
            $('#supplier_building_update').val(response.building_no);
            $('#supplier_lane_update').val(response.lane);
            $('#supplier_city_update').val(response.city);
            $('#supplier_state_update').val(response.state);
            $('#supplier_postal_code_update').val(response.postal_code);

        // Clear existing options and add the new brand option
        $('#supplier_brand_update').empty().append('<option value="" selected></option>');

        // Add the brand to the dropdown
        if (response.brand) {
            $('#supplier_brand_update').append('<option value="' + response.brand + '" selected>' + response.brand + '</option>');
        }

            $('#supplier_category_update').val(response.category);



    } catch (error) {
        console.error("Request failed:", error);
        Swal.fire({
            icon: "error",
            title: "Oops...",
            text: "Supplier not found!",
        });

    }
};

function clearSupplierUpdateForm() {
    $(".btn-close").click();
}

$('#supplier_update_close_btn').click(function(){
    $(".btn-close").click();
});
function attachEventListenerSupplier() {

    // Event listener for view action
    $('.view-action-supplier').click(function (e) {

        e.preventDefault();
        const supplierId = $(this).data('supplier-id');


        // Change the modal title to "Supplier Info"
        $("#popupModalLabelSupplierUpdate").text("Supplier Info");

        // Hide the Update button
        $("#supplier_update_btn").hide();


        // Disable all input fields
        $("#supplier_id_Update, #supplier_name_update, #supplier_category_update,#supplier_brand_update,#supplier_building_update,#supplier_lane_update,#supplier_city_update, #supplier_state_update, #supplier_postal_code_update, #supplier_contact_update,#supplier_email_update").prop("disabled", true);

        searchSupplier(supplierId);


    });

    // Event listener for update action
    $('.update-action-supplier').click(function (e) {

        e.preventDefault();

        const supplierId = $(this).data('supplier-id');

        // Change the modal title to "Update Supplier"
        $("#popupModalLabelSupplierUpdate").text("Update Supplier");

        // Show the Update button
        $("#supplier_update_btn").show();

        // Enable all input fields
        $("#supplier_name_update, #supplier_category_update,#supplier_brand_update,#supplier_building_update,#supplier_lane_update,#supplier_city_update, #supplier_state_update, #supplier_postal_code_update, #supplier_contact_update,#supplier_email_update").prop("disabled", false);

        searchSupplier(supplierId);

    });

    // Event listener for delete action
    $('.delete-action-supplier').click(async function (e) {
        e.preventDefault();
        const supplierId = $(this).data('supplier-id');

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
                        url: "http://localhost:8081/helloShoesPVT/api/v1/supplier/" + supplierId,
                        headers: {
                            "Authorization": "Bearer " + accessToken
                        },
                        contentType: "application/json"
                    });

                    swalWithBootstrapButtons.fire({
                        title: "Deleted!",
                        text: "Supplier has been deleted.",
                        icon: "success"
                    });

                    loadSuppliers();
                    clearSupplierUpdateForm();

                } catch (error) {
                    console.error("Request failed:", error);

                    Swal.fire({
                        icon: "error",
                        title: "Oops...",
                        text: "You have no access to Delete this Supplier..!",
                    });
                }


            } else if (
                result.dismiss === Swal.DismissReason.cancel
            ) {
                swalWithBootstrapButtons.fire({
                    title: "Cancelled",
                    text: "Supplier is safe :)",
                    icon: "error"
                });
            }
        });


    });
}

function loadSuppliers() {
    // Retrieve the access token from localStorage
    const accessToken = localStorage.getItem('accessToken');

    $('#supplier_table_body').empty();

    $.ajax({
        type:"GET",
        url: "http://localhost:8081/helloShoesPVT/api/v1/supplier/all",
        headers: {
            "Authorization": "Bearer " + accessToken
        },
        contentType: "application/json",

        success: function (response) {

            console.log(response)


            response.map((supplier, index) => {

                let tbl_row = `<tr data-employee-id=${supplier.supplier_id}>
                    <td class="supplier_id"><p>${supplier.supplier_id}</p></td>
                    <td class="supplier_name"><p class="text-xs font-weight-bold mb-0">${supplier.name}</p></td>
                    <td class="supplier_designation align-middle text-center text-sm"><span class="badge badge-sm bg-gradient-success">${supplier.category}</span></td>
                    <td class="supplier_gender"><p class="text-center mb-0">${supplier.brand}</p></td>
                     <td class="supplier_address"><p class="text-center mb-0">${supplier.building_no + ", " + supplier.lane + ", " + supplier.city + ", " + supplier.state + ", " + supplier.postal_code}</p></td>
                    <td class="supplier_gender"><p class="text-center mb-0">${supplier.contact}</p></td>
                    <td class="supplier_status"><p class="text-center mb-0">${supplier.email}</p></td>
                    
                    <td class="align-middle white-space-nowrap text-end pe-0">
                        <div class="btn-reveal-trigger position-static align-middle">
                            <button class="btn btn-sm dropdown-toggle dropdown-caret-none transition-none btn-reveal fs-10" type="button" data-bs-toggle="dropdown" data-boundary="window" aria-haspopup="true" aria-expanded="false" data-bs-reference="parent">
                                <span class="fas fa-ellipsis-h fs-10"></span>
                            </button>
                            <div class="dropdown-menu dropdown-menu-end py-2">
                                <a class="dropdown-item view-action-supplier" href="#!" data-bs-toggle="modal" data-bs-target="#updateModelSupplier" data-supplier-id="${supplier.supplier_id}">View</a>
                                <a class="dropdown-item update-action-supplier text-danger" href="#!" data-bs-toggle="modal" data-bs-target="#updateModelSupplier" data-supplier-id="${supplier.supplier_id}">Update</a>
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item delete-action-supplier text-warning" href="#!" data-bs-target="#updateModelSupplier" id="supplier_delete_btn" data-supplier-id="${supplier.supplier_id}">Remove</a>
                            </div>
                        </div>
                    </td>
                </tr>`;
                $('#supplier_table_body').append(tbl_row);
            });

            attachEventListenerSupplier()

        },
        error: function (xhr, status, error) {
            console.error('Something Error');
        }
    });

}

$("#suppliers_link").click(function(event) {
    loadSuppliers();
});

const brandByCategory = {
    'LOCAL': ["DSI", "Bata", "Gihan Shoe Mart", "Redtape", "Hameedia", "DSI Premier", "Reborn", "DI", "Ranpa Footwear", "SADS Footwear",
            "ZigZag", "Dilly & Carlo", "Hitz", "Stoneridge", "UL Wikramasinghe & Company"],
    'INTERNATIONAL': ["Nike", "Adidas", "Puma", "Converse", "Vans", "Reebok", "New Balance", "Timberland", "Dr. Martens", "Asics",
        "Skechers", "Clarks", "Under Armour", "Fila", "Salomon"]
};

function populateCategory(categoryelement, brandelement) {
    const category = document.getElementById(categoryelement).value;
    const brand = document.getElementById(brandelement);
    brand.innerHTML = '<option value="" disabled selected>Select Brand</option>'; // Reset sizes

    if (category) {
        const brands = brandByCategory[category];
        brands.forEach(size => {
            const option = document.createElement('option');
            option.value = size;
            option.textContent = size;
            brand.appendChild(option);
        });
    }
}

document.getElementById('supplier_category_add').addEventListener('change', function() {
    populateCategory('supplier_category_add', 'supplier_brand_add');
});
document.getElementById('supplier_category_update').addEventListener('change', function() {
    populateCategory('supplier_category_update', 'supplier_brand_update');
});
