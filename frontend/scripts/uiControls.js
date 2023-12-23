function toggleMarkerInfoPanel() {
    var panel = document.getElementById('markerInfoPanel');
    var button = document.getElementById('togglePanelBtn');
    var isOpen = panel.style.left === '0px';

    if (isOpen) {
        closeMarkerInfoPanel(button);
    } else {
        openMarkerInfoPanel(button);
    }
}

function openMarkerInfoPanel(button) {
    document.getElementById('markerInfoPanel').style.left = '0';

    button.style.left = '400px';
    button.classList.add('active');
}

function closeMarkerInfoPanel(button) {
    document.getElementById('markerInfoPanel').style.left = '-400px';
    button.style.left = '0';
    button.classList.remove('active');
}