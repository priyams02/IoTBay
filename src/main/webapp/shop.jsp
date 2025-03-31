<%@ page import="main.classes.User" %>
<%@ page import="main.classes.Address" %>
<%@ page import="main.classes.Staff" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Retrieve the logged-in user from session
    User user = (User) session.getAttribute("loggedInUser");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>IoTBay - Home</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="styles/IoTBayStyles.css">
</head>
<body>
<nav class="navbar">
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
<header>
    <h1>IoTBay</h1>
    <p>Your one-stop shop for IoT products</p>
</header>

<div class="container">
    <h2>Featured Products</h2>
    <div class="product-list">
        <!-- Product 1 -->
        <div class="product">
            <img src="product1.jpg" alt="Smart Sensor">
            <h3>Smart Sensor</h3>
            <p>High precision sensor for your IoT applications.</p>
            <button>Add to Cart</button>
        </div>
        <!-- Product 2 -->
        <div class="product">
            <img src="product2.jpg" alt="IoT Hub">
            <h3>IoT Hub</h3>
            <p>Centralized device management and connectivity.</p>
            <button>Add to Cart</button>
        </div>
        <!-- Product 3 -->
        <div class="product">
            <img src="product3.jpg" alt="Smart Light">
            <h3>Smart Light</h3>
            <p>Adjustable smart lighting for energy savings.</p>
            <button>Add to Cart</button>
        </div>
        <!-- Product 4 -->
        <div class="product">
            <img src="product4.jpg" alt="Security Camera">
            <h3>Security Camera</h3>
            <p>High definition camera with remote monitoring.</p>
            <button>Add to Cart</button>
        </div>
    </div>
</div>

</body>
</html>

