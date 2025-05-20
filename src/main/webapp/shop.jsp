<%@ page import="uts.isd.model.Person.User" %>
<%@ page import="uts.isd.model.Person.Staff" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String ctx = request.getContextPath();
    User user = (User) session.getAttribute("loggedInUser");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>IoTBay - Shop</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<%= ctx %>/styles/IoTBayStyles.css">
</head>
<body>
<!-- Top Menu Bar -->
<nav class="navbar">
    <div class="navLinks left">
        <a href="<%= ctx %>/index.jsp">Home</a>
    </div>
    <div class="navLinks right">
        <a href="<%= ctx %>/shop.jsp">Shop</a>
        <% if (user == null) { %>
        <a href="<%= ctx %>/LoginServlet">Login</a>
        <a href="<%= ctx %>/RegisterServlet">Register</a>
        <% } else { %>
        <!-- Hard-coded Profile links -->
        <a href="<%= ctx %>/Profile.jsp">Update Profile</a>
        <a href="<%= ctx %>/ViewProfile.jsp">View Profile</a>
        <!-- Hard-coded Shipments link always pointing to orderId=1 -->
        <a href="<%= ctx %>/Shipment/List?orderId=1">Shipments</a>
        <a href="<%= ctx %>/LogoutServlet">Logout</a>
        <% } %>
    </div>
</nav>

<!-- Page Header -->
<header>
    <h1>IoTBay</h1>
    <p>Your one-stop shop for IoT products</p>
</header>

<!-- Featured Products -->
<div class="container">
    <h2>Featured Products</h2>
    <div class="product-list">
        <!-- Product 1 -->
        <div class="product">
            <img src="<%= ctx %>/Images/SmartSensor.png" alt="Smart Sensor">
            <h3>Smart Sensor</h3>
            <p>High precision sensor for your IoT applications.</p>
            <button>Add to Cart</button>
        </div>
        <!-- Product 2 -->
        <div class="product">
            <img src="<%= ctx %>/Images/IoTHubM3.png" alt="IoT Hub">
            <h3>IoT Hub</h3>
            <p>Centralized device management and connectivity.</p>
            <button>Add to Cart</button>
        </div>
        <!-- Product 3 -->
        <div class="product">
            <img src="<%= ctx %>/Images/LEDLight.png" alt="Smart Light">
            <h3>Smart Light</h3>
            <p>Adjustable smart lighting for energy savings.</p>
            <button>Add to Cart</button>
        </div>
        <!-- Product 4 -->
        <div class="product">
            <img src="<%= ctx %>/Images/SecurityCamera.png" alt="Security Camera">
            <h3>Security Camera</h3>
            <p>High definition camera with remote monitoring.</p>
            <button>Add to Cart</button>
        </div>
    </div>
</div>
</body>
</html>
