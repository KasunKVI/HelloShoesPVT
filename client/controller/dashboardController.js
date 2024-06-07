let supplierChart;
let customerChart;
async function loadLabels() {
    // Retrieve the access token from localStorage
    const accessToken = localStorage.getItem('accessToken');
    const branchId = localStorage.getItem('branchId');

    try {
        const response = await $.ajax({
            type: "GET",
            url: "http://localhost:8081/helloShoesPVT/api/v1/order/total-sales-balance/" + branchId,
            headers: {
                "Authorization": "Bearer " + accessToken
            },
            contentType: "application/json"
        });

        const total = response;

        document.getElementById('total_sales').innerText = `Rs. ${total.toLocaleString()}`;
    } catch (error) {
        console.error("Request failed:", error);
        console.error('There has been a problem with your fetch operation:', error);

    }
    try {
        const response = await $.ajax({
            type: "GET",
            url: "http://localhost:8081/helloShoesPVT/api/v1/order/today-sales/"+branchId,
            headers: {
                "Authorization": "Bearer " + accessToken
            },
            contentType: "application/json"
        });

        const total = response;
        console.log(total)

        document.getElementById('today_sales').innerText = `Rs. ${total.toLocaleString()}`;
    } catch (error) {
        console.error("Request failed:", error);
        console.error('There has been a problem with your fetch operation:', error);

    }

     try {
            const response = await $.ajax({
                type: "GET",
                url: "http://localhost:8081/helloShoesPVT/api/v1/customer/get-today-count",
                headers: {
                    "Authorization": "Bearer " + accessToken
                },
                contentType: "application/json"
            });

            const todayCustomers = response;

            console.log(todayCustomers);

            // Update the HTML with the fetched customers count
            document.getElementById('today_customers').innerText = todayCustomers.toLocaleString();

    } catch (error) {
        console.error('There has been a problem with your fetch operation:', error);
    }

    try {
        const response = await $.ajax({
            type: "GET",
            url: "http://localhost:8081/helloShoesPVT/api/v1/customer/get-weekly-count",
            headers: {
                "Authorization": "Bearer " + accessToken
            },
            contentType: "application/json"
        });

        const weeklyCustomers = await response;

        console.log(weeklyCustomers);

        const labels = weeklyCustomers.map(item => item[0]);
        const data = weeklyCustomers.map(item => item[1]);

        var ctx = document.getElementById("chart-bars").getContext("2d");

        if (customerChart) {
            customerChart.destroy(); // Destroy the previous chart instance
        }

        customerChart = new Chart(ctx, {
            type: "bar",
            data: {
                labels: labels,
                datasets: [{
                    label: "Customers",
                    tension: 0.4,
                    borderWidth: 0,
                    borderRadius: 4,
                    borderSkipped: false,
                    backgroundColor: "rgba(255, 255, 255, .8)",
                    data: data,
                    maxBarThickness: 6
                }],
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: false,
                    }
                },
                interaction: {
                    intersect: false,
                    mode: 'index',
                },
                scales: {
                    y: {
                        grid: {
                            drawBorder: false,
                            display: true,
                            drawOnChartArea: true,
                            drawTicks: false,
                            borderDash: [5, 5],
                            color: 'rgba(255, 255, 255, .2)'
                        },
                        ticks: {
                            suggestedMin: 0,
                            suggestedMax: 500,
                            beginAtZero: true,
                            padding: 10,
                            font: {
                                size: 14,
                                weight: 300,
                                family: "Roboto",
                                style: 'normal',
                                lineHeight: 2
                            },
                            color: "#fff"
                        },
                    },
                    x: {
                        grid: {
                            drawBorder: false,
                            display: true,
                            drawOnChartArea: true,
                            drawTicks: false,
                            borderDash: [5, 5],
                            color: 'rgba(255, 255, 255, .2)'
                        },
                        ticks: {
                            display: true,
                            color: '#f8f9fa',
                            padding: 10,
                            font: {
                                size: 14,
                                weight: 300,
                                family: "Roboto",
                                style: 'normal',
                                lineHeight: 2
                            },
                        }
                    },
                },
            },
        });


    } catch (error) {
        console.error('There has been a problem with your fetch operation:', error);
    }
    try {
        const response = await $.ajax({
            type: "GET",
            url: "http://localhost:8081/helloShoesPVT/api/v1/supplier/count-by-category",
            headers: {
                "Authorization": "Bearer " + accessToken
            },
            contentType: "application/json"
        });

        const counts = response;



        const ctx3 = document.getElementById("chart-line-tasks").getContext("2d");

        if (supplierChart) {
            supplierChart.destroy(); // Destroy the previous chart instance
        }
        supplierChart = new Chart(ctx3, {
            type: 'line',
            data: {
                labels: ["International", "Local"],
                datasets: [{
                    label: "Suppliers",
                    tension: 0,
                    borderWidth: 0,
                    pointRadius: 5,
                    pointBackgroundColor: "rgba(255, 255, 255, .8)",
                    pointBorderColor: "transparent",
                    borderColor: "rgba(255, 255, 255, .8)",
                    borderWidth: 4,
                    backgroundColor: "transparent",
                    fill: true,
                    data: [counts.international, counts.local],
                    maxBarThickness: 6
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: false,
                    }
                },
                interaction: {
                    intersect: false,
                    mode: 'index',
                },
                scales: {
                    y: {
                        grid: {
                            drawBorder: false,
                            display: true,
                            drawOnChartArea: true,
                            drawTicks: false,
                            borderDash: [5, 5],
                            color: 'rgba(255, 255, 255, .2)'
                        },
                        ticks: {
                            display: true,
                            padding: 10,
                            color: '#f8f9fa',
                            font: {
                                size: 14,
                                weight: 300,
                                family: "Roboto",
                                style: 'normal',
                                lineHeight: 2
                            },
                        }
                    },
                    x: {
                        grid: {
                            drawBorder: false,
                            display: false,
                            drawOnChartArea: false,
                            drawTicks: false,
                            borderDash: [5, 5]
                        },
                        ticks: {
                            display: true,
                            color: '#f8f9fa',
                            padding: 10,
                            font: {
                                size: 14,
                                weight: 300,
                                family: "Roboto",
                                style: 'normal',
                                lineHeight: 2
                            },
                        }
                    }
                }
            }
        });

    } catch (error) {
        console.error('There has been a problem with your fetch operation:', error);
    }


}
$('#dashboard_link').click(function () {
    loadLabels();
    // loadCharts();
});

