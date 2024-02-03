function sendPollutionRequest(lat, lng) {
    var xhrPollution = new XMLHttpRequest();
    xhrPollution.onreadystatechange = function () {
        if (xhrPollution.readyState === 4) {
            if (xhrPollution.status === 200) {
                var pollutionData = JSON.parse(xhrPollution.responseText);
                createPollutionChart(pollutionData);
            } else {
                console.error("Error sending pollution request. Status:", xhrPollution.status);
            }
        }
    };
    var urlPollution = "http://localhost:8080/pollution/lat/" + lat + "/lng/" + lng;
    xhrPollution.open("GET", urlPollution, true);
    xhrPollution.send();
}

function createPollutionChart(pollutionData) {
    createSubChart('pm25Chart', 'PM 2.5 Pollution Data', 'rgba(75, 192, 192, 0.2)', 'rgba(75, 192, 192, 1)', pollutionData.data.forecast.daily.pm25);
    createSubChart('pm10Chart', 'PM 10 Pollution Data', 'rgba(255, 99, 132, 0.2)', 'rgba(255, 99, 132, 1)', pollutionData.data.forecast.daily.pm10);
    createSubChart('o3Chart', 'O3 Pollution Data', 'rgba(54, 162, 235, 0.2)', 'rgba(54, 162, 235, 1)', pollutionData.data.forecast.daily.o3);
}

function createSubChart(canvasId, label, backgroundColor, borderColor, data) {
    var ctx = document.createElement('canvas');
    ctx.id = canvasId;
    document.getElementById('markerInfoContent').appendChild(ctx);

    var subChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: data.map(day => day.day),
            datasets: [{
                label: label,
                data: data.map(day => day.avg),
                backgroundColor: backgroundColor,
                borderColor: borderColor,
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}
