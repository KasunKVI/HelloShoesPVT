
const type = 0;
let itemPic;
let itemId;

const sizesByGender = {
    'M': [36, 37, 38, 39, 40, 41, 42, 43, 44, 45],
    'W': [34, 35, 36, 37, 38, 39, 40, 41, 42, 43]
};

function populateSizes(genderAdd, size) {
    const gender = document.getElementById(genderAdd).value;
    const sizeSelect = document.getElementById(size);
    sizeSelect.innerHTML = '<option value="" disabled selected>Select Size</option>'; // Reset sizes

    if (gender && sizesByGender[gender]) {
        const sizes = sizesByGender[gender];
        sizes.forEach(size => {
            const option = document.createElement('option');
            option.value = size;
            option.textContent = size;
            sizeSelect.appendChild(option);
        });
    }
}
document.getElementById('shoe_gender_add').addEventListener('change', function() {
    populateSizes('shoe_gender_add', 'shoe_size_add');
});
document.getElementById('shoe_gender_update').addEventListener('change', function() {
    populateSizes('shoe_gender_update', 'shoe_size_update');
});

// Clear form function
function clearShoeAddForm() {
    // Clear image preview
    $('#shoe_preview').attr('src', '');

    // Reset file input
    $('#shoe_picture').val('');

    // Reset select fields to default option
    $('#shoe_supplier_add').val('').change();
    $('#shoe_gender_add').val('').change();
    $('#shoe_occasion_add').val('').change();
    $('#shoe_verity_add').val('').change();
    $('#shoe_size_add').val('').change();

    // Clear text and number inputs
    $('#shoe_bought_price_add').val('');
    $('#shoe_selling_price').val('');
    $('#shoe_qty_add').val('');
}

function validateShoeAddForm() {
    let isValidShoe = true;

    isValidShoe &= validateNotEmpty($('#shoe_qty_add'));
    isValidShoe &= validateNotEmpty($('#shoe_size_add'));
    isValidShoe &= validateNotEmpty($('#shoe_occasion_add'));
    isValidShoe &= validateNotEmpty($('#shoe_picture'));
    isValidShoe &= validateNotEmpty($('#shoe_bought_price_add'));
    isValidShoe &= validateNotEmpty($('#shoe_verity_add'));
    isValidShoe &= validateNotEmpty($('#shoe_selling_price'));
    isValidShoe &= validateNotEmpty($('#shoe_gender_add'));

    // Show custom alert if the form is invalid
    if (!isValidShoe) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Please correct the highlighted fields'
        });
    }

    return isValidShoe;
}


function loadInventoryTable() {


    // Retrieve the access token from localStorage
    const accessToken = localStorage.getItem('accessToken');

    let status;
    $('#inventory_table_body').empty();

    $.ajax({
        type: "GET",
        url: "http://localhost:8081/helloShoesPVT/api/v1/inventory",
        headers: {
            "Authorization": "Bearer " + accessToken
        },
        contentType: "application/json",

        success: function (response) {



            response.map((inventoryItem, index) => {

                if (inventoryItem.qty > 10){
                    status = "Available"
                }else if (inventoryItem.qty < 10){
                    status = "Low"
                }else {
                    status = "Not Available"
                }
                // Calculate profit and profit margin
                const profit = inventoryItem.sell_price - inventoryItem.bought_price;
                const profitMargin = ((profit / inventoryItem.bought_price) * 100).toFixed(2);

                let tbl_row = `<tr data-inventory-id=${inventoryItem.invt_id}> 
                    <td class="invt_id"><p>${inventoryItem.invt_id}</p></td>
                    <td class="description"><p class="text-xs font-weight-bold mb-0">${inventoryItem.description}</p></td>
                    <td class="picture"><p class="text-center  mb-0"><img src="${inventoryItem.picture}" alt="Item Picture" width="50"></p></td>
                    <td class="qty"><p class="text-center  mb-0">${inventoryItem.qty}</p></td>
                    <td class="category"><p class="text-center  mb-0">${inventoryItem.type}</p></td>
                    <td class="supplier"><p class="text-center  mb-0">${inventoryItem.supplier_id}</p></td>
                    <td class="bought_price"><p class="text-center  mb-0">${inventoryItem.bought_price}</p></td>
                    <td class="sell_price"><p class="text-center  mb-0">${inventoryItem.sell_price}</p></td>
                    <td class="profit"><p class="text-center  mb-0">${profit}</p></td>
                    <td class="profit_margin"><p class="text-center  mb-0">${profitMargin}%</p></td>
                    <td class="status"><p class="text-center  mb-0">${status}</p></td>
                    <td class="align-middle white-space-nowrap text-end pe-0">
                        <div class="btn-reveal-trigger position-static align-middle">
                            <button class="btn btn-sm dropdown-toggle dropdown-caret-none transition-none btn-reveal fs-10" type="button" data-bs-toggle="dropdown" data-boundary="window" aria-haspopup="true" aria-expanded="false" data-bs-reference="parent"><span class="fas fa-ellipsis-h fs-10"></span></button>
                            <div class="dropdown-menu dropdown-menu-end py-2">
                                <a class="dropdown-item view-action-invt" href="#!" data-bs-toggle="modal" data-bs-target="#updateModelShoe" data-inventory-id="${inventoryItem.invt_id}">View</a>
                                <a class="dropdown-item update-action-invt text-danger" href="#!" data-bs-toggle="modal" data-bs-target="#updateModelShoe" data-inventory-id="${inventoryItem.invt_id}">Update</a>
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item delete-action-invt text-warning" href="#!" data-bs-target="#updateModelShoe" id="inventory_delete_btn" data-inventory-id="${inventoryItem.invt_id}">Remove</a>
                            </div>
                        </div>
                    </td>
                </tr>`;
                $('#inventory_table_body').append(tbl_row);
            });

             attachEventListenersInventory();

        },
        error: function (xhr, status, error) {
            console.error('Error loading inventory:', error);
        }
    });
}

function selectVerityAndOccasion(description) {
    const verity = description.split(" ")[0];
    const gender = description.split(" ")[2];

    $('#shoe_gender_update').empty().append('<option value="" selected></option>');

    $('#shoe_Description').val(verity);
    if (gender === 'Male') {
        $('#shoe_gender_update').append('<option value="M" selected>' + gender + '</option>');
        $('#shoe_gender_update').append('<option value="W" >Female</option>');
    }else {
        $('#shoe_gender_update').append('<option value="W" selected>' + gender + '</option>');
        $('#shoe_gender_update').append('<option value="M" >Male</option>');
    }


}

async function searchInventory(invt_id) {

    // Retrieve the access token from localStorage
    const accessToken = localStorage.getItem('accessToken');

    try {
        const response = await $.ajax({
            type: "GET",
            url: "http://localhost:8081/helloShoesPVT/api/v1/inventory/" + invt_id,
            headers: {
                "Authorization": "Bearer " + accessToken
            },
            contentType: "application/json"
        });

        // Populate the form fields with the retrieved customer details
        $("#shoe_preview_update").attr('src', response.picture);

        // Clear existing options and add the new brand option
        $('#shoe_supplier_update').empty().append('<option value="" selected></option>');

        // Add the brand to the dropdown
        if (response.supplier_id) {
            $('#shoe_supplier_update').append('<option value="' + response.supplier_id + '" selected>' + response.supplier_id + '</option>');
        }

        $('#supplier_category_update').val(response.category);


        $("#shoe_bought_price_update").val(response.bought_price);
        $("#shoe_selling_update").val(response.sell_price);
        selectVerityAndOccasion(response.description);
        const size = invt_id.slice(-2);

        $('#shoe_size_update').empty().append('<option value="" selected></option>');
        $('#shoe_size_update').append('<option value="'+size+'" selected>' + size + '</option>');
        $("#shoe_qty_update").val(response.qty);
        itemPic = response.picture;






    } catch (error) {
        console.error("Request failed:", error);
        Swal.fire({
            icon: "error",
            title: "Oops...",
            text: "Item not found!",
        });


        $("#customer_details_section").hide();
    }
}

function clearInventoryUpdateForm() {
    $(".btn-close").click();
}

const attachEventListenersInventory = () => {
    // Event listener for view action
    $('.view-action-invt').click(function (e) {
        e.preventDefault();
        const invt_id = $(this).data('inventory-id');

        // Change the modal title to "Item Info"
        $("#popupModalLabelShoeUpdate").text("Item Info");

        // Hide the Update button
        $("#shoe_update_btn").hide();


        // Disable all input fields
        $("#shoe_picture_update, #shoe_supplier_update, #shoe_bought_price_update,#shoe_selling_update,#shoe_gender_update,#shoe_Description, #shoe_size_update, #shoe_qty_update").prop("disabled", true);

        searchInventory(invt_id);

    });

    // Event listener for update action
    $('.update-action-invt').click(function (e) {

        e.preventDefault();

        const invt_id = $(this).data('inventory-id');

        // Change the modal title to "Update Customer"
        $("#popupModalLabelShoeUpdate").text("Update Item");

        // Show the Update button
        $("#shoe_update_btn").show();

        // Enable all input fields
        $("#shoe_picture_update, #shoe_supplier_update, #shoe_bought_price_update,#shoe_selling_update,#shoe_gender_update, #shoe_size_update, #shoe_qty_update").prop("disabled", false);

        itemId = invt_id;

        const accessToken = localStorage.getItem('accessToken');

        $.ajax({
            type:"GET",
            url: "http://localhost:8081/helloShoesPVT/api/v1/supplier/ids",
            headers: {
                "Authorization": "Bearer " + accessToken
            },
            contentType: "application/json",


            success: function (response) {
                console.log('Supplier IDs:', response);
                const select = $('#shoe_supplier_update');
                response.forEach(id => {
                    const option = $('<option></option>').val(id).text(id);
                    select.append(option);
                });
            },
            error: function(error) {
                console.error('Error fetching Item IDs:', error);
            }
        });

        searchInventory(invt_id);

    });

    // Event listener for delete action
    $('.delete-action-invt').click(async function (e) {

        e.preventDefault();
        const invt_id = $(this).data('inventory-id');
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
                        url: "http://localhost:8081/helloShoesPVT/api/v1/inventory/" + invt_id,
                        headers: {
                            "Authorization": "Bearer " + accessToken
                        },
                        contentType: "application/json"
                    });

                    swalWithBootstrapButtons.fire({
                        title: "Deleted!",
                        text: "Item has been deleted.",
                        icon: "success"
                    });

                    loadInventoryTable();
                    clearInventoryUpdateForm();

                } catch (error) {
                    console.error("Request failed:", error);

                    Swal.fire({
                        icon: "error",
                        title: "Oops...",
                        text: "You have no access to Delete this Item!",
                    });
                }


            } else if (
                result.dismiss === Swal.DismissReason.cancel
            ) {
                swalWithBootstrapButtons.fire({
                    title: "Cancelled",
                    text: "Item is safe :)",
                    icon: "error"
                });
            }
        });


    });
};

async function submitInventoryForm(inventory, type) {
    const accessToken = localStorage.getItem('accessToken');

    if (type === "Save_Shoe") {

        try {
            const response = await $.ajax({
                type: "POST",
                url: "http://localhost:8081/helloShoesPVT/api/v1/inventory/save",
                headers: {
                    "Authorization": "Bearer " + accessToken
                },
                data: JSON.stringify(inventory),
                contentType: "application/json"
            });

            // Assuming the response has a 'message' property
            if (response.message === "Item saved successfully") {
                // Show a success message
                Swal.fire({
                    icon: "success",
                    title: response.message,
                    showConfirmButton: false,
                    timer: 1500
                });

                loadInventoryTable();
                clearShoeAddForm();

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
                text: "You have no access to add new item."
            });
        }
    }else if (type === "Update_Shoe") {

        console.log(inventory);
        try {
            const response = await $.ajax({
                type: "PUT",
                url: "http://localhost:8081/helloShoesPVT/api/v1/inventory/update",
                headers: {
                    "Authorization": "Bearer " + accessToken
                },
                data: JSON.stringify(inventory),
                contentType: "application/json"
            });

            // Assuming the response has a 'message' property
            if (response.message === "Item updated successfully") {
                // Show a success message
                Swal.fire({
                    icon: "success",
                    title: response.message,
                    showConfirmButton: false,
                    timer: 1500
                });

                loadInventoryTable();
                clearInventoryUpdateForm();

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
                text: "You have no access to add new item."
            });
        }
    }
    }
$('#addNewShoeButton').click(function () {

    const accessToken = localStorage.getItem('accessToken');

    $.ajax({
        type:"GET",
        url: "http://localhost:8081/helloShoesPVT/api/v1/supplier/ids",
        headers: {
            "Authorization": "Bearer " + accessToken
        },
        contentType: "application/json",


        success: function (response) {
            const select = $('#shoe_supplier_add');
            response.forEach(id => {
                const option = $('<option></option>').val(id).text(id);
                select.append(option);
            });
        },
        error: function(error) {
            console.error('Error fetching Item IDs:', error);
        }
    });


});




    $('#shoe_add_btn').click(function () {


        if (validateShoeAddForm()) {

            let itemId = $('#shoe_occasion_add').val() + $('#shoe_verity_add').val() + $('#shoe_gender_add').val() + "00" + $('#shoe_size_add').val();
            let shoePicBase64 = $('#shoe_picture').data('base64') || "";

            const inventoryDTO = new InventoryDTO(
                itemId,
                $('#shoe_occasion_add').find('option:selected').text() + $('#shoe_verity_add').find('option:selected').text() + " for " + $('#shoe_gender_add').find('option:selected').text(),
                shoePicBase64,
                $('#shoe_qty_add').val().trim(),
                $('#shoe_bought_price_add').val(),
                $('#shoe_selling_price').val().trim(),
                "Shoe",
                $('#shoe_supplier_add').find('option:selected').text(),
            );


            submitInventoryForm(inventoryDTO, "Save_Shoe");


        }
    });

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
    $('#shoe_picture').on('change', async function () {
        const file = this.files[0];
        if (file) {
            const base64 = await toBase64(file);
            $('#shoe_preview').attr('src', base64); // Set image preview
            $('#shoe_picture').data('base64', base64); // Store base64 string in data attribute
        }
    });


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

$("#inventory_link").click(function(event) {
    loadInventoryTable();
});

$('#shoe_update_close_btn').click(function () {
    clearInventoryUpdateForm();
});

function validateShoeUpdateForm() {
    let isValidShoeUpdate = true;

    isValidShoeUpdate &= validateNotEmpty($('#shoe_qty_update'));
    isValidShoeUpdate &= validateNotEmpty($('#shoe_size_update'));
    // isValidShoeUpdate &= validateNotEmpty($('#shoe_occasion_add'));
    // isValidShoeUpdate &= validateNotEmpty($('#shoe_picture_update'));
    isValidShoeUpdate &= validateNotEmpty($('#shoe_bought_price_update'));
    isValidShoeUpdate &= validateNotEmpty($('#shoe_Description'));
    isValidShoeUpdate &= validateNotEmpty($('#shoe_selling_update'));
    isValidShoeUpdate &= validateNotEmpty($('#shoe_gender_update'));

    // Show custom alert if the form is invalid
    if (!isValidShoeUpdate) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Please correct the highlighted fields'
        });
    }

    return isValidShoeUpdate;
}

$('#shoe_update_btn').click(function () {


    let profilePicUpdateBase64 = $('#shoe_picture_update').data('base64') || "";

    if (profilePicUpdateBase64 === "") {
        profilePicUpdateBase64 = itemPic;
    }

    if (validateShoeUpdateForm()) {
        const inventory = new InventoryDTO(
            itemId,
            $('#shoe_Description').val().trim()+" for "+$('#shoe_gender_update').val().trim(),
            itemPic,
            $('#shoe_qty_update').val().trim(),
            $('#shoe_bought_price_update').val().trim(),
            $('#shoe_selling_update').val().trim(),
            "Shoe",
            $('#shoe_supplier_update').find('option:selected').text(),
        );

        submitInventoryForm(inventory,"Update_Shoe");
    }

});

$('#shoe_add_clear_btn').click(function () {
    clearShoeAddForm();
});