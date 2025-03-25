<%--
  Created by IntelliJ IDEA.
  User: priyam
  Date: 18/3/2025
  Time: 7:52 pm
  To change this template use File | Settings | File Templates.
--%>
<!-- login.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login Page</title>
    <link rel="stylesheet" type="text/css" href="styles/login.css">
</head>
<body>
<div class="login-container">
    <p><strong>Please Log-In to your IoTBay Account</strong></p>
    <h2>Login</h2>
    <form action="loginServlet" method="post">
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required>

        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required>

        <button type="submit">Login</button>
    </form>
</div>
</body>
</html>

