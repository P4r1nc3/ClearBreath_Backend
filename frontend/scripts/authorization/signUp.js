document.addEventListener('DOMContentLoaded', function() {
    var signUpForm = document.getElementById('signUpForm');
    signUpForm.addEventListener('submit', function(event) {
        event.preventDefault();

        var firstName = document.getElementById('firstName').value;
        var lastName = document.getElementById('lastName').value;
        var email = document.getElementById('email').value;
        var password = document.getElementById('password').value;

        var formData = {
            "firstName": firstName,
            "lastName": lastName,
            "email": email,
            "password": password
        };

        var xhr = new XMLHttpRequest();
        xhr.open('POST', 'http://localhost:8080/auth/signup', true);
        xhr.setRequestHeader('Content-Type', 'application/json');

        xhr.onload = function() {
            if (xhr.status === 200) {
                var response = JSON.parse(xhr.responseText);
                var token = response.token;

                localStorage.setItem('token', token);

                window.location.href = 'index.html';
            } else {
                console.error('Error:', xhr.statusText);
                alert('Sign Up failed. Please check your information and try again.');
            }
        };

        xhr.onerror = function() {
            console.error('Network error occurred');
            alert('Network error. Please try again later.');
        };

        xhr.send(JSON.stringify(formData));
    });
});
