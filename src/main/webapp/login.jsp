<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login Page</title>
    <link rel="stylesheet" type="text/css" href="styles/login.css">
</head>
<body>
<div class="login-container">
    <p><strong>Please Login to your IoTBay Account</strong></p>
    <h2>Login</h2>

    <form class="login-form">
        <label for="username">Username:</label>
        <input class="box" type="text" id="username" name="username" required>

        <label for="password">Password:</label>
        <input class="box" type="password" id="password" name="password" required
               autocomplete="off"
               pattern="^(?=.*[A-Z])(?=.*[\W_]).{8,}$"
               title="Password must be at least 8 characters long, include at least one uppercase letter, and one special character.">

        <button type="submit">Login</button>
    </form>
</div>
</body>
</html>
