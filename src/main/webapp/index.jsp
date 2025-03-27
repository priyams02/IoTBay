<%@ page import="main.classes.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Retrieve the logged-in user from session
    User user = (User) session.getAttribute("loggedInUser");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>IoTBay</title>
    <link rel="stylesheet" href="styles/IoTBayStyles.css">
</head>
<body>
<div class="IndexDivMain">
    <!-- Top Menu Bar -->
    <!-- I think when we finish up the initial design let's make the nav bar horizontal instead of vertical -->
    <nav>
        <div class="navLinks left">
            <a href="index.jsp">Home</a>
        </div>
        <div class="navLinks right">
            <a href="shop.jsp">Shop</a>
            <% if (user == null) { %>
            <a href="login.jsp">Login</a>
            <a href="register.jsp">Register</a>
            <% } else { %>
            <a href="LogoutHandler.jsp">Logout</a>
            <% } %>
        </div>
    </nav>

    <h1 class="IndexH1">IoTBay</h1>

    <!-- Content Area -->
    <div>
        <p class="paragraph textarea">
            IoTBay | Introduction to Software Development Assignment 1: R0
            <br><br>
            The internet of Things Store (IoTBay) is a small company based in Sydney, Australia.
            IoTBay wants to develop an online IoT devices ordering application to allow their customers
            to purchase IoT devices.
        </p>
    </div>

    <!-- Logged-in User Information -->
    <div class="login">
        <p>Logged in user:
            <% if (user == null) { %>
            No one
            <% } else { %>
            <%= user.getEmail() %>
            <% } %>
        </p>
    </div>
</div>
</body>
</html>