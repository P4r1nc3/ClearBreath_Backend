document.addEventListener('DOMContentLoaded', function() {
    var signInForm = document.getElementById('signInForm');
    signInForm.addEventListener('submit', function(event) {
        event.preventDefault();

        var email = document.getElementById('email').value;
        var password = document.getElementById('password').value;

        var formData = {
            "email": email,
            "password": password
        };

        var xhr = new XMLHttpRequest();
        xhr.open('POST', 'http://localhost:8080/auth/signin', true);
        xhr.setRequestHeader('Content-Type', 'application/json');

        xhr.onload = function() {
            if (xhr.status === 200) {
                var response = JSON.parse(xhr.responseText);
                var token = response.token;

                localStorage.setItem('token', token);

                window.location.href = 'index.html';
            } else {
                console.error('Error:', xhr.statusText);
                alert('Sign In failed. Please check your information and try again.');
            }
        };

        xhr.onerror = function() {
            console.error('Network error occurred');
            alert('Network error. Please try again later.');
        };

        xhr.send(JSON.stringify(formData));
    });
});
