<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String ctx   = request.getContextPath();
    String err   = request.getParameter("err");
    String email = request.getParameter("Email");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login Page</title>
    <link rel="stylesheet" href="<%= ctx %>/styles/login.css">
</head>
<body>
<!-- Top menu bar -->
<nav class="navbar">
    <div class="navLinks left">
        <a href="<%= ctx %>/index.jsp">Home</a>
    </div>
</nav>

<div class="login-container">
    <p><strong>Please Login to your IoTBay Account</strong></p>
    <h2>Login</h2>

    <!-- Display any error -->
    <% if (err != null) { %>
    <p style="color:red;"><%= err %></p>
    <% } %>

    <!-- POST to your servlet -->
    <form class="login-form" action="<%= ctx %>/LoginServlet" method="post">
        <label for="Email">E-Mail:</label>
        <input
                class="box"
                type="email"
                id="Email"
                name="Email"
                value="<%= email != null ? email : "" %>"
                required
        >

        <label for="Password">Password:</label>
        <input
                class="box"
                type="password"
                id="Password"
                name="Password"
                required
                autocomplete="off"
        >

        <button type="submit">Login</button>
    </form>

    <div class="signup-link">
        Not Registered? <a href="<%= ctx %>/RegisterServlet">Register Now!</a>
    </div>
</div>
</body>
</html>
