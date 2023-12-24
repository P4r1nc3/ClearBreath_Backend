function fetchUserData() {
    var token = localStorage.getItem('token');
    var xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4) {
            if (xhr.status == 200) {
                var userData = JSON.parse(xhr.responseText);
                document.getElementById('userFirstName').textContent = userData.firstName;
                document.getElementById('userLastName').textContent = userData.lastName;
                document.getElementById('userEmail').textContent = userData.email;
                document.getElementById('userCreatedAt').textContent = new Date(userData.createdAt).toLocaleDateString();
            } else {
                console.error("Failed to fetch user data!");
            }
        }
    };

    var url = "http://localhost:8080/users";
    xhr.open("GET", url, true);
    xhr.setRequestHeader('Authorization', 'Bearer ' + token);
    xhr.send();
}

document.addEventListener('DOMContentLoaded', fetchUserData);

function deleteUser() {
    if (!confirm("Are you sure you want to delete your profile?")) {
        return;
    }

    var token = localStorage.getItem('token');
    var xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4) {
            if (xhr.status == 200) {
                console.log("Profile deleted successfully.");
                localStorage.removeItem('token');
                window.location.href = 'index.html';
            } else {
                console.error("Failed to delete user profile!");
            }
        }
    };

    var url = "http://localhost:8080/users";
    xhr.open("DELETE", url, true);
    xhr.setRequestHeader('Authorization', 'Bearer ' + token);
    xhr.send();
}

document.addEventListener('DOMContentLoaded', function() {
    fetchUserData();
    document.querySelector('.delete-btn').addEventListener('click', deleteUser);
});