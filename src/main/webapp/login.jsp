<!-- login.jsp -->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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

    <!-- Display error message if 'error=invalid' -->
    <%
        String error = request.getParameter("error");
        if ("invalid".equals(error)) {
    %>
    <p style="color:red;">Invalid username or password!</p>
    <%
        }
    %>

    <!-- POST to LoginHandler.jsp -->
    <form class="login-form" action="LoginHandler.jsp" method="post">
        <label for="username">Username:</label>
        <input class="box" type="text" id="username" name="username" required>

        <label for="password">Password:</label>
        <input class="box" type="password" id="password" name="password" required
               autocomplete="off">

        <button type="submit">Login</button>
    </form>
</div>
</body>
</html>