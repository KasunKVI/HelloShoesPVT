
var nicPattern = /^(?:\d{9}[Vv]|\d{12})$/;
let  InventoryDto = new InventoryDTO();
let subtotal = 0;

let shoes = [];
let accessories = [];

$('#item_code_sales').on('input', async function () {


    let itemPic;
    let item_code = $('#item_code_sales').val();

    if (item_code.length >= 6) {
        // Retrieve the access token from localStorage
        const accessToken = localStorage.getItem('accessToken');

        try {
            const response = await $.ajax({
                type: "GET",
                url: "http://localhost:8081/helloShoesPVT/api/v1/inventory/" + item_code,
                headers: {
                    "Authorization": "Bearer " + accessToken
                },
                contentType: "application/json"
            });

            itemPic = response.picture;

            InventoryDto.invt_id=item_code;
            InventoryDto.description=response.description;
            InventoryDto.picture=response.picture;
            InventoryDto.sell_price=response.sell_price;
            InventoryDto.qty = response.qty;

            // Populate the form fields with the retrieved Item details
            $("#itm_image_cart").attr('src', response.picture);

            $("#item_name_cart").text(response.description);
            $("#item_price_cart").text(response.sell_price);

            if (item_code.startsWith("ACC")) {

                $("#item_size_cart").text("Pre Sized");

            } else {

                // Get the last two digits of the item code
                let size = item_code.slice(-2);
                $("#item_size_cart").text(size);


            }

        } catch (error) {
            console.error("Request failed:", error);
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "Item not found!",
            });

        }
    }
});


function validateAddToCart() {
    let isValidItem = true;

    isValidItem &= validateNumberField($('#item_qty_sales'));

    if (!isValidItem) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Please correct the highlighted fields'
        });
    }

    return isValidItem;
}

function updateSummery() {


    // Calculate subtotal
    $('#cart_table_body tr').each(function () {
        const total = parseFloat($(this).find('.item_total').text());
        subtotal += total;
    });

    // Update subtotal
    $('#subtotal').text("Rs " + subtotal.toFixed(2));

    // Get discount value
    const discount = parseFloat($('#discount').val());


    // Update total after discount
    if (!isNaN(discount)){

        $('#total').text("Rs " + (subtotal - discount).toFixed(2));
    }else {
        $('#total').text("Rs " + (subtotal).toFixed(2))
    }

}

function applyRemain() {
    // Get customer paid amount and total
    const customerPaid = parseFloat($('#customer_paid_amount').val());
    const total = parseFloat($('#total').text().replace("Rs ", ""));

    // Calculate remaining amount
    const remain = customerPaid - total;

    // Update remaining amount
    $('#remain').text("Rs " + remain.toFixed(2));
}

$('#customer_paid_amount').keypress(function(event) {
    // Check if the Enter key is pressed
    if (event.keyCode === 13) {
        // Call your function here
        applyRemain();
    }
});
$("#customer_paid_btn").click(async function (event) {

    applyRemain()
});
$("#add_to_cart_btn").click(async function (event) {

    $('#cart_table_body').empty();

    if (validateAddToCart()) {

        if (InventoryDto.qty < $('#item_qty_sales').val()) {

            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "Not enough stock available!",
            });

        } else {

            InventoryDto.qty = $('#item_qty_sales').val();


            const verity = InventoryDto.description.split(" ")[0];
            const gender = InventoryDto.description.split(" ")[2];
            const total = InventoryDto.qty * InventoryDto.sell_price;

            let tbl_row = `
            <tr data-customer-id="${InventoryDto.invt_id}">
                <td class="item_id border-bottom-0"><h6 class="fw-semibold mb-0">${InventoryDto.invt_id}</h6></td>
                <td class="item_name border-bottom-0">
                    <h6 class="fw-semibold mb-1">${verity}</h6>
                    <span class="fw-normal">${gender}</span>
                </td>
                <td class="item_price border-bottom-0"><p class="mb-0 fw-normal">${InventoryDto.sell_price}</p></td>
                <td class="item_qty border-bottom-0">
                    <div class="d-flex align-items-center gap-2">
                        <span class="badge bg-primary rounded-3 fw-semibold">${InventoryDto.qty}</span>
                    </div>
                </td>
                <td class="item_total border-bottom-0"><h6 class="fw-semibold mb-0 fs-4">${total}</h6></td>    
            </tr>`;

            $('#cart_table_body').append(tbl_row);
            updateSummery()
        }
    }
});


$('#billing-nic').on('input', async function () {

        let nic = $('#billing-nic').val();

        if (validateField($('#billing-nic'), nicPattern)) {

            // Retrieve the access token from localStorage
            const accessToken = localStorage.getItem('accessToken');

            try {
                const response = await $.ajax({
                    type: "GET",
                    url: "http://localhost:8081/helloShoesPVT/api/v1/customer/" + nic,
                    headers: {
                        "Authorization": "Bearer " + accessToken
                    },
                    contentType: "application/json"
                });


                $("#billing-name").val(response.name);
                $("#billing-points").val(response.points);
                $("#billing-address").val(response.building_no+", "+response.lane+", "+response.city+", "+response.state+", "+response.postal_code);
                $("#billing_email").val(response.email);
                $("#billing-level").val(response.level);
                $("#billing_gender").val(response.gender);

            } catch (error) {
                console.error("Request failed:", error);
                Swal.fire({
                    icon: "error",
                    title: "Oops...",
                    text: "Customer not found!",
                });

            }
        }

});

function generateNewOrderId(latestId) {
    if (!latestId) {
        return "R001";
    }

    // Extract numeric part and increment
    let numericPart = parseInt(latestId.substring(1), 10);
    numericPart += 1;

    // Format the new ID with leading zeros
    return "R" + String(numericPart).padStart(3, '0');
}

async function getNewOrderId() {

    const accessToken = localStorage.getItem('accessToken');

    try {
        const response = await $.ajax({
            type: "GET",
            url: "http://localhost:8081/helloShoesPVT/api/v1/order/latest",
            headers: {
                "Authorization": "Bearer " + accessToken
            },
        });

        $('#order_id').text(generateNewOrderId(response));
    }catch (error) {
        console.error("Request failed:", error);
        Swal.fire({
            icon: "error",
            title: "Oops...",
            text:  "Error fetching order id. Please try again later"
        });

        await refreshAccessToken();
        await getNewOrderId();
    }

}

$("#sales_link").click( function (event){
    getNewOrderId()

});

function validCheckoutForm() {
    let isValidOrder = true;

    const selectedPaymentMethod = $("input[name='pay-method']:checked").val();
    const customerPaid = parseFloat($('#customer_paid_amount').val());

    if (!selectedPaymentMethod) {
        isValidOrder = false;
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Please select the payment method'
        });
    }

    if (isNaN(customerPaid) || customerPaid < parseFloat($('#total').text().replace("Rs ", ""))) {
        isValidOrder = false;
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Please enter a valid amount'
        });
    }

    return isValidOrder;
}

function clearPlaceOrderForm() {
// Clear Item Selector fields
    $('#item_code_sales').val('');
    $('#item_qty_sales').val('');
    $('#item_name_cart').text('');
    $('#item_price_cart').text('');
    $('#item_size_cart').text('');
    $('#itm_image_cart').attr('src', 'assets/img/pngwing.com.png'); // Reset to default image or an empty image

    // Clear Customer Info fields
    $('#billing-nic').val('');
    $('#billing-name').val('');
    $('#billing-points').val('');
    $('#billing-address').val('');
    $('#billing_email').val('');
    $('#billing-level').val('');
    $('#billing_gender').val('');

    // Clear Payment Method selection
    $("input[name='pay-method']").prop('checked', false);

    // Clear Order Summary fields
    $('#order_id').text('');
    $('#subtotal').text('');
    $('#discount').text('');
    $('#total').text('');
    $('#remain').text('');
    $('#customer_paid_amount').val('');

    // Clear Cart Table
    $('#cart_table_body').empty();
}

$("#checkout_btn").click(async function (event){

    const accessToken = localStorage.getItem('accessToken');

    if (validCheckoutForm()) {

        shoes = [];
        accessories = [];

        const orderId = $('#order_id').text();
        let point = 0;
        if (subtotal >= 800) {
            point = 1;
        }

        const branchId = localStorage.getItem('branchId');
        const employeeId = localStorage.getItem('employeeId');

        $('#cart_table_body tr').each(function () {
            // Extract item ID and quantity from the current row
            const id = $(this).find('.item_id').text();
            const qty = parseInt($(this).find('.item_qty .fw-semibold').text());

            if (id.startsWith("ACC")) {
                accessories.push({id, qty});
            } else {
                shoes.push({id, qty});
            }

        });

        // Get the selected payment method
        const selectedPaymentMethod = $("input[name='pay-method']:checked").val();

        const order = new OrderDTO(orderId, new Date().toISOString(), parseFloat($('#total').text().replace("Rs ", "")), selectedPaymentMethod, point, $('#billing-nic').val(), employeeId, branchId, shoes, accessories);

        try {

            const response = await $.ajax({
                type: "POST",
                url: "http://localhost:8081/helloShoesPVT/api/v1/order/save",
                headers: {
                    "Authorization": "Bearer " + accessToken
                },
                data: JSON.stringify(order),
                contentType: "application/json"
            });


            // Assuming the response has a 'message' property
            if (response.message === "Order placed successfully") {
                // Show a success message
                Swal.fire({
                    icon: "success",
                    title: response.message,
                    showConfirmButton: false,
                    timer: 1500
                });

                clearPlaceOrderForm();


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
                text: "Something Error."
            });

            await refreshAccessToken();
            await $("#checkout_btn").click();
        }
    }

});