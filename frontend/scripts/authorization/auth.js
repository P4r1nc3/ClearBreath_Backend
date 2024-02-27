document.addEventListener('DOMContentLoaded', function() {
    var loggedIn = localStorage.getItem('token') ? true : false;

    function updateNavButtons() {
        var navButtons = document.getElementById("navButtons");
        if (loggedIn) {
            navButtons.innerHTML =
                '<li class="nav-item"><a class="nav-link" href="markers.html">Markers</a></li>' +
                '<li class="nav-item"><a class="nav-link" href="map.html">Map</a></li>' +
                '<li class="nav-item dropdown">' +
                '<a class="nav-link dropdown-toggle" href="#" id="accountDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Account</a>' +
                '<div class="dropdown-menu" aria-labelledby="accountDropdown">' +
                '<a class="dropdown-item" href="profile.html">Profile</a>' +
                '<a class="dropdown-item" href="#" id="logoutBtn">Logout</a>' +
                '</div></li>';
        } else {
            navButtons.innerHTML =
                '<li class="nav-item"><a class="nav-link" href="signIn.html">SignIn</a></li>' +
                '<li class="nav-item"><a class="nav-link" href="signUp.html">SignUp</a></li>';
        }
    }

    function logout() {
        localStorage.removeItem('token');
        loggedIn = false;
        updateNavButtons();
        alert('Logout successful!');
        window.location.href = 'index.html';
    }

    var originalSend = XMLHttpRequest.prototype.send;
    XMLHttpRequest.prototype.send = function() {
        this.addEventListener('load', function() {
            if (this.status === 401) {
                try {
                    var response = JSON.parse(this.responseText);
                    if (response.cause === "Your session has expired due to an expired JWT token.") {
                        logout();
                    }
                } catch (e) {
                    console.error("Error parsing JSON response from server.");
                }
            }
        });
        originalSend.apply(this, arguments);
    };

    updateNavButtons();

    document.getElementById('navButtons').addEventListener('click', function(event) {
        if (event.target.id === 'logoutBtn') {
            logout();
        }
    });
});