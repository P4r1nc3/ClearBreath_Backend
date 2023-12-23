document.addEventListener('DOMContentLoaded', function() {
    var loggedIn = localStorage.getItem('token') ? true : false;

    function updateNavButtons() {
        var navButtons = document.getElementById("navButtons");
        if (loggedIn) {
            navButtons.innerHTML = '<li class="nav-item"><a class="nav-link" href="map.html">Map</a></li>' +
                '<li class="nav-item"><a class="nav-link" href="#" id="logoutBtn">Logout</a></li>';
        } else {
            navButtons.innerHTML = '<li class="nav-item"><a class="nav-link" href="signIn.html">SignIn</a></li>' +
                '<li class="nav-item"><a class="nav-link" href="signUp.html">SignUp</a></li>';
        }
    }

    updateNavButtons();

    document.getElementById('navButtons').addEventListener('click', function(event) {
        if (event.target.id === 'logoutBtn') {
            localStorage.removeItem('token');
            loggedIn = false;
            updateNavButtons();
            alert('Logout successful!');
            window.location.href = 'index.html';
        }
    });
});
