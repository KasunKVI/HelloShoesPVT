
$('#login_section').css('display', 'none');
$('#signup_section').css('display', 'none');
$('#main_dashboard').css('display', 'block');
$('#sales_section').css('display','none');
$('#suppliers_section').css('display','none');
$('#customer_section').css('display','none');
$('#employee_section').css('display','none');


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

$('#create_new_account_btn').on('click', () => {

    $('#login_section').fadeOut('slow', function() {
        $('#signup_section').fadeIn('slow');
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
};

dashboardLink.addEventListener("click", function(event) {

    event.preventDefault();
    doActivateLinks(dashboardLink);

    doHideSections();
    $('#main_dashboard').css('display', 'block');

});

salesLink.addEventListener("click",  function (event){

    event.preventDefault();
    doActivateLinks(salesLink);

    doHideSections();
    $('#sales_section').css('display', 'block');

});
suppliersLink.addEventListener("click",  function (event){

    event.preventDefault();
    doActivateLinks(suppliersLink);

    doHideSections();
    $('#suppliers_section ').css('display', 'block');


});
customersLink.addEventListener("click",  function (event){

    event.preventDefault();
    doActivateLinks(customersLink);

    doHideSections();
    $('#customer_section').css('display','block');

});
employeesLink.addEventListener("click",  function (event){

    event.preventDefault();
    doActivateLinks(employeesLink);

    doHideSections();
    $('#employee_section').css('display','block');
});
inventoryLink.addEventListener("click",  function (event){

    event.preventDefault();
    doActivateLinks(inventoryLink);

});
profileLInk.addEventListener("click",  function (event){

    event.preventDefault();
    doActivateLinks(profileLInk);

});
signInLink.addEventListener("click",  function (event){

    event.preventDefault();
    doActivateLinks(signInLink)



});
signUpLink.addEventListener("click",  function (event){

    event.preventDefault();
    doActivateLinks(signUpLink)

});