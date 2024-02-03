var map = L.map('map', {
    zoomControl: false,
    minZoom: 3,
}).setView([20, 0], 3);

var southWest = L.latLng(-90, -Infinity);
var northEast = L.latLng(90, Infinity);
var bounds = L.latLngBounds(southWest, northEast);

map.setMaxBounds(bounds);
map.on('drag', function() {
    map.panInsideBounds(bounds, { animate: false });
});

var markers = [];
var clickedMarker = null;
var chart;

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© ClearBreath'
}).addTo(map);

L.control.zoom({
    position: 'bottomright'
}).addTo(map);

map.on('click', function (e) {
    var lat = e.latlng.lat;
    var lng = e.latlng.lng;
    savePointToDatabase(lat, lng);
});

$(document).ready(function () {
    loadMarkers();
});

function addMarker(lat, lng, continent, countryName, city) {
    var marker = L.marker([lat, lng]).addTo(map);
    markers.push(marker);

    marker.on('click', function (e) {
        var markerCoord = e.latlng;
        clickedMarker = markerCoord;

        $('#markerInfoContent').html(`<h2>${continent}</h2><h5>${countryName}</h5><h6>${city}</h6>`);

        sendPollutionRequest(markerCoord.lat, markerCoord.lng);

        if (chart) {
            chart.destroy();
            chart = null;
        }

        $('#deleteMarkerBtn').off('click').on('click', function () {
            if (clickedMarker) {
                var index = markers.findIndex(m => m.getLatLng().equals(clickedMarker));

                if (index !== -1) {
                    map.removeLayer(markers[index]);
                    markers.splice(index, 1);
                    deletePointFromDatabase(clickedMarker.lat, clickedMarker.lng);
                    toggleMarkerInfoPanel();
                    clickedMarker = null;
                } else {
                    console.warn("Marker not found in the markers array.");
                }
            } else {
                console.warn("No marker clicked.");
            }
        });

        if ($('#togglePanelBtn').hasClass('active')) {
            openMarkerInfoPanel();
        } else {
            toggleMarkerInfoPanel();
        }
    });
}

function savePointToDatabase(lat, lng) {
    var token = localStorage.getItem('token');
    var xhrSavePoint = new XMLHttpRequest();
    xhrSavePoint.onreadystatechange = function () {
        if (xhrSavePoint.readyState == 4) {
            if (xhrSavePoint.status == 200) {
                var markerData = JSON.parse(xhrSavePoint.responseText);
                addMarker(lat, lng, markerData.continent, markerData.countryName, markerData.city);
            } else {
                console.error("Failed to save point to the database!");
            }
        }
    };
    var urlSavePoint = "http://localhost:8080/markers/lat/" + lat + "/lng/" + lng;
    xhrSavePoint.open("POST", urlSavePoint, true);
    xhrSavePoint.setRequestHeader('Authorization', 'Bearer ' + token);
    xhrSavePoint.send();
}

function loadMarkers() {
    var token = localStorage.getItem('token');
    var xhrLoadMarkers = new XMLHttpRequest();
    xhrLoadMarkers.onreadystatechange = function () {
        if (xhrLoadMarkers.readyState == 4) {
            if (xhrLoadMarkers.status == 200) {
                var markerData = JSON.parse(xhrLoadMarkers.responseText);
                markerData.forEach(function (data) {
                    addMarker(data.lat, data.lng, data.continent, data.countryName, data.city);
                });
            } else {
                console.error("Failed to load markers from the database!");
            }
        }
    };
    var urlLoadMarkers = "http://localhost:8080/markers";
    xhrLoadMarkers.open("GET", urlLoadMarkers, true);
    xhrLoadMarkers.setRequestHeader('Authorization', 'Bearer ' + token)
    xhrLoadMarkers.send();
}

function deletePointFromDatabase(lat, lng) {
    var token = localStorage.getItem('token');
    var xhrDeletePoint = new XMLHttpRequest();
    xhrDeletePoint.onreadystatechange = function () {
        if (xhrDeletePoint.readyState == 4) {
            if (xhrDeletePoint.status == 200) {
                console.log("Point deleted from the database successfully!");
            } else {
                console.error("Failed to delete point from the database!");
            }
        }
    };
    var urlDeletePoint = "http://localhost:8080/markers/lat/" + lat + "/lng/" + lng;
    xhrDeletePoint.open("DELETE", urlDeletePoint, true);
    xhrDeletePoint.setRequestHeader('Authorization', 'Bearer ' + token);
    xhrDeletePoint.send();
}