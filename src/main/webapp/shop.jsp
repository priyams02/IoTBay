<%@ page import="uts.isd.model.Person.User" %>
<%@ page import="uts.isd.model.Person.Staff" %>
<%@ page import="uts.isd.model.Product" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String ctx = request.getContextPath();
    User user = (User) session.getAttribute("loggedInUser");
    Integer lastOrderId = (Integer) session.getAttribute("lastOrderId");
    List<Product> products = (List<Product>)request.getAttribute("productList");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>IoTBay - Shop</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<%= ctx %>/styles/IoTBayStyles.css">
    <style>
        .navDisabled {
            color: #666;
            cursor: not-allowed;
            text-decoration: none;
            padding: 0 10px;
        }
    </style>
</head>
<body>
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
        <% if (lastOrderId != null) { %>
        <a href="<%= ctx %>/Shipment/List?orderId=<%= lastOrderId %>">Shipments</a>
        <% } else { %>
        <span class="navDisabled">Shipments</span>
        <% } %>
        <a href="<%= ctx %>/LogoutServlet">Logout</a>
        <% } %>
    </div>
</nav>

<header>
    <h1>IoTBay</h1>
    <p>Your one-stop shop for IoT products</p>

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