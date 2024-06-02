// Function to scroll to the specified element
function scrollToElement(element) {
    element.scrollIntoView({ behavior: "smooth", block: "center" });
}

// Function to handle search action
function handleSearch() {
    var liText = document.querySelector("#section").textContent;
    console.log("Breadcrumb text:", liText); // Check the value of liText

    const id = $('#search_input').val().trim(); // Trim whitespace from input
    let row;
    console.log("Searching for ID:", id);

    if (liText === "Customers") {
        console.log("Searching in Customers table...");
        $('#customer_table_body tr').removeClass('highlighted'); // Remove highlight from previous search results
        row = $(`#customer_table_body tr[data-customer-id="${id}"]`);
    } else if (liText === "Suppliers") {
        console.log("Searching in Suppliers table...");
        $('#supplier_table_body tr').removeClass('highlighted'); // Remove highlight from previous search results
        row = $(`#supplier_table_body tr[data-supplier-id="${id}"]`);
    } else if (liText === "Inventory") {
        console.log("Searching in Inventory table...");
        $('#inventory_table_body tr').removeClass('highlighted'); // Remove highlight from previous search results
        row = $(`#inventory_table_body tr[data-inventory-id="${id}"]`);
    } else if (liText === "Employees") {
        console.log("Searching in Employees table...");
        $('#employee_table_body tr').removeClass('highlighted'); // Remove highlight from previous search results
        row = $(`#employee_table_body tr[data-employee-id="${id}"]`);
    } else {
        console.log("Invalid breadcrumb text:", liText);
    }

    if (row && row.length > 0) {
        // Highlight the found row
        row.addClass('highlighted');
        scrollToElement(row[0]);
    } else {
        alert('ID not found');
    }
}


// Event listener for Enter key press on the input field
$('#search_input').keypress(function (e) {
    if (e.which === 13) {
        handleSearch();
    }
});