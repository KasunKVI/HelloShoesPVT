
$('#signup_section').css('display', 'none');
$('#main_dashboard').css('display', 'none');
$('#sales_section').css('display','none');
$('#suppliers_section').css('display','none');
$('#customer_section').css('display','none');
$('#employee_section').css('display','none');
$('#inventory_section').css('display','none');
$('#sidenav-main').css('display','none');
$('#navbarBlur').css('display','none');
$('#login_section').css('display', 'none');
// $('#customer_add_form').css('display','none');
$(document).ready(async function () {
    const refreshTokenLogin = localStorage.getItem('refreshToken');

    try {
        const response = await $.ajax({
            type: "POST",
            url: "http://localhost:8080/helloShoesPVT/api/v1/auth/refresh",
            data: {
                refreshToken: refreshTokenLogin // Use refreshTokenLogin variable here
            },
            contentType: "application/x-www-form-urlencoded"
        });

        const tokenString = response.token;
        const [accessToken, newRefreshToken] = tokenString.split(':').map(token => token.trim());

        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('refreshToken', newRefreshToken);

        $('#login_section').css('display', 'none');
        $('#main_dashboard').fadeIn('slow');
        $('#sidenav-main').fadeIn('slow');
        $('#navbarBlur').fadeIn('slow');
    } catch (error) {
        console.error("Login failed:", error);
        $('#login_section').css('display', 'block');
        $('#main_dashboard').css('display', 'none');
    }
});

window.onload = async function () {
    const refreshTokenWindow = localStorage.getItem('refreshToken');

    try {
        const response = await $.ajax({
            type: "POST",
            url: "http://localhost:8080/helloShoesPVT/api/v1/auth/refresh",
            data: {
                refreshToken: refreshTokenWindow // Use refreshTokenWindow variable here
            },
            contentType: "application/x-www-form-urlencoded"
        });

        const tokenString = response.token;
        const [accessToken, newRefreshToken] = tokenString.split(':').map(token => token.trim());

        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('refreshToken', newRefreshToken);

        $('#login_section').css('display', 'none');
        $('#main_dashboard').fadeIn('slow');
        $('#sidenav-main').fadeIn('slow');
        $('#navbarBlur').fadeIn('slow');
    } catch (error) {
        console.error("Login failed:", error);
        $('#login_section').css('display', 'block');
        $('#main_dashboard').css('display', 'none');
    }
}


var dashboardLink = document.getElementById("dashboard_link");
var salesLink     = document.getElementById("sales_link");
var suppliersLink = document.getElementById("suppliers_link");
var customersLink = document.getElementById("customers_link");
var employeesLink = document.getElementById("employees_link");
var inventoryLink = document.getElementById("inventory_link");
var profileLInk = document.getElementById("profile_link");
var signInLink = document.getElementById("signIn_link");
var signUpLink = document.getElementById("signUp_link");

dashboardLink.classList.add("active");
dashboardLink.classList.add("bg-gradient-primary");

var secondLi = document.querySelector(".breadcrumb-item:nth-child(2)");

$('#create_new_account_btn').on('click', () => {

    $('#login_section').fadeOut('slow', function() {
        $('#signup_section').fadeIn('slow');
        $("#signUp_role").prop('disabled', true);
        $("#signUp_email").prop('disabled', true);
    });

});

function doActivateLinks(clickedLink){

    // Remove the "active" class from all nav-links
    var navLinks = document.querySelectorAll(".nav-link");
    navLinks.forEach(function(link) {
        link.classList.remove("active");
        link.classList.remove("bg-gradient-primary");
    });

    // Add the "active" class to the clicked link
    clickedLink.classList.add("active");

    // Add the "bg-gradient-primary" class to the clicked link
    clickedLink.classList.add("bg-gradient-primary");
}

function doHideSections(){
    $('#main_dashboard').css('display', 'none');
    $('#suppliers_section').css('display', 'none');
    $('#login_section').css('display', 'none');
    $('#signup_section').css('display', 'none');
    $('#sales_section').css('display','none');
    $('#customer_section').css('display','none');
    $('#employee_section').css('display','none');
    $('#inventory_section').css('display','none');
};

dashboardLink.addEventListener("click", function(event) {

    event.preventDefault();
    doActivateLinks(dashboardLink);

    doHideSections();
    $('#main_dashboard').css('display', 'block');

    secondLi.textContent = "Dashboard";

});

salesLink.addEventListener("click",  function (event){

    event.preventDefault();
    doActivateLinks(salesLink);

    doHideSections();
    $('#sales_section').css('display', 'block');

    secondLi.textContent = "Sales";


});
suppliersLink.addEventListener("click",  function (event){

    event.preventDefault();
    doActivateLinks(suppliersLink);

    doHideSections();
    $('#suppliers_section ').css('display', 'block');

    secondLi.textContent = "Suppliers";


});
customersLink.addEventListener("click",  function (event){

    event.preventDefault();
    doActivateLinks(customersLink);

    doHideSections();
    $('#customer_section').css('display','block');

    secondLi.textContent = "Customers";

});
employeesLink.addEventListener("click",  function (event){

    event.preventDefault();
    doActivateLinks(employeesLink);

    doHideSections();
    $('#employee_section').css('display','block');

    secondLi.textContent = "Employees";

});
inventoryLink.addEventListener("click",  function (event){

    event.preventDefault();
    doActivateLinks(inventoryLink);

    doHideSections();
    $('#inventory_section').css('display','block');

    secondLi.textContent = "Inventory";

});
profileLInk.addEventListener("click",  function (event){

    event.preventDefault();
    doActivateLinks(profileLInk);

    doHideSections();

    secondLi.textContent = "Profile";

});
signInLink.addEventListener("click",  function (event){

    event.preventDefault();
    doActivateLinks(signInLink)

    doHideSections();
    $('#sidenav-main').css('display','none');
    $('#navbarBlur').css('display','none');
    $('#login_section').fadeIn('slow');

});
signUpLink.addEventListener("click",  function (event){

    event.preventDefault();
    doActivateLinks(signUpLink)

    doHideSections();
    $('#sidenav-main').css('display','none');
    $('#navbarBlur').css('display','none');
    $('#signup_section').fadeIn('slow');

});

document.getElementById("add_customer_btn").addEventListener("click", function() {
    var formContainer = document.getElementById("customer_add_form");
    formContainer.style.display = formContainer.style.display === "none" ? "block" : "none";
});
