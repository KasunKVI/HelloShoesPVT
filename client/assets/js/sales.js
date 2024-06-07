async function fetchSalesData() {

  const accessToken = localStorage.getItem('accessToken');
  const branchId = localStorage.getItem('branchId');

  try {
    const response = await fetch('http://localhost:8081/helloShoesPVT/api/v1/order/sales/'+branchId, {
      method: 'GET',
      headers: {
        'Authorization': 'Bearer ' + accessToken,
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      throw new Error('Network response was not ok ' + response.statusText);
    }

    const salesData = await response.json();
    updateSalesChart(salesData);

  } catch (error) {
    console.error('There has been a problem with your fetch operation:', error);
  }

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
    document.getElementById('totalSales').innerText = `Rs. ${total.toLocaleString()}`;
  } catch (error) {
    console.error('There has been a problem with your fetch operation:', error);
  }

  try {
    const response = await $.ajax({
      type: "GET",
      url: "http://localhost:8081/helloShoesPVT/api/v1/order/total-profit/"+branchId,
      headers: {
        "Authorization": "Bearer " + accessToken
      },
      contentType: "application/json"
    });
    console.log(response)
    const total = response;
    document.getElementById('totalProfit').innerText = `Rs. ${total.toLocaleString()}`;
  } catch (error) {
    console.error('There has been a problem with your fetch operation:', error);
  }
  try {
    const response = await $.ajax({
      type: "GET",
      url: "http://localhost:8081/helloShoesPVT/api/v1/order/most-saled-item",
      headers: {
        "Authorization": "Bearer " + accessToken
      },
      contentType: "application/json"
    });
    console.log(response)
    // Update the description and quantity in the HTML elements
    document.getElementById('mostSaleItem').innerText = response.description;
    document.getElementById('mostSaleItemQty').innerText = response.quantity;

// Check if there is a picture available
    if (response.picture) {
      // Set the source attribute of the image element to the URL of the picture
      document.getElementById('pictureOfMostSaleItem').src = response.picture;
    } else {
      // If there is no picture available, you can hide the image element or display a placeholder image
      document.getElementById('pictureOfMostSaleItem').style.display = 'none';
    }
  } catch (error) {
    console.error('There has been a problem with your fetch operation:', error);
  }
}

async function updateSalesChart(salesData) {
  // Format dates to display only date and year
  const formattedDates = salesData.dates.map(date => {
    const dateTime = new Date(date);
    return `${dateTime.getDate()}/${dateTime.getMonth() + 1}/${dateTime.getFullYear()}`;
  });

  const chartOptions = {
    series: [
      {name: "Total Sales", data: salesData.totals}
    ],
    chart: {
      type: "bar",
      height: 345,
      offsetX: -15,
      toolbar: {show: true},
      foreColor: "#adb0bb",
      fontFamily: 'inherit',
      sparkline: {enabled: false},
    },
    colors: ["#5D87FF"],
    plotOptions: {
      bar: {
        horizontal: false,
        columnWidth: "35%",
        borderRadius: [6],
        borderRadiusApplication: 'end',
        borderRadiusWhenStacked: 'all'
      },
    },
    markers: {size: 0},
    dataLabels: {enabled: false},
    legend: {show: false},
    grid: {
      borderColor: "rgba(0,0,0,0.1)",
      strokeDashArray: 3,
      xaxis: {lines: {show: false}},
    },
    xaxis: {
      type: "category",
      categories: formattedDates, // Use formatted dates here
      labels: {style: {cssClass: "grey--text lighten-2--text fill-color"}},
    },
    yaxis: {
      show: true,
      labels: {
        style: {cssClass: "grey--text lighten-2--text fill-color"},
      },
    },
    stroke: {
      show: true,
      width: 3,
      lineCap: "butt",
      colors: ["transparent"],
    },
    tooltip: {theme: "light"},
    responsive: [
      {
        breakpoint: 600,
        options: {
          plotOptions: {
            bar: {borderRadius: 3}
          }
        }
      }
    ]
  };

  const salesChart = new ApexCharts(document.querySelector("#salesChart"), chartOptions);
  salesChart.render();

}

// Fetch sales data and update the chart
$('#profile_link').click(function () {
  fetchSalesData();
});
