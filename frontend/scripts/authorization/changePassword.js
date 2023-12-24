document.getElementById('changePasswordForm').addEventListener('submit', function(event) {
    event.preventDefault();

    var oldPassword = document.getElementById('currentPassword').value;
    var newPassword = document.getElementById('newPassword').value;
    var token = localStorage.getItem('token');

    var xhr = new XMLHttpRequest();
    xhr.open("PUT", "http://localhost:8080/users/change-password", true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.setRequestHeader('Authorization', 'Bearer ' + token);
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4) {
            if (xhr.status == 200) {
                alert("Password changed successfully.");
            } else {
                alert("Failed to change password.");
            }
        }
    };

    var data = JSON.stringify({
        oldPassword: oldPassword,
        newPassword: newPassword
    });
    xhr.send(data);
});