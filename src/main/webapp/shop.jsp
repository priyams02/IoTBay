<%@ page import="uts.isd.model.Person.User" %>
<%@ page import="uts.isd.model.Person.Staff" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String ctx = request.getContextPath();
    User user = (User) session.getAttribute("loggedInUser");
    Integer lastOrderId = (Integer) session.getAttribute("lastOrderId");
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
<nav class="navbar">
    <div class="navLinks left">
        <a href="<%= ctx %>/index.jsp">Home</a>
    </div>
    <div class="navLinks right">
        <a href="<%= ctx %>/shop.jsp">Shop</a>
        <a href="<%= ctx %>/Orders?action=history">My Orders</a>
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
</header>

<div class="container">
    <h2>Featured Products</h2>
    <div class="product-list">
        <!-- Product 1 -->
        <div class="product">
            <img src="<%= ctx %>/Images/SmartSensor.png" alt="Smart Sensor">
            <h3>Smart Sensor</h3>
            <p>High precision sensor for your IoT applications.</p>
            <form action="<%= ctx %>/Checkout" method="post">
                <input type="hidden" name="action" value="order"/>
                <input type="hidden" name="productID" value="1" />
                <label>Quantity:</label>
                <input type="number" name="quantity" value="1" min="1" />
                <button type="submit">Order Now</button>
            </form>
        </div>
        <!-- Product 2 -->
        <div class="product">
            <img src="<%= ctx %>/Images/IoTHubM3.png" alt="IoT Hub">
            <h3>IoT Hub</h3>
            <p>Centralized device management and connectivity.</p>
            <form action="<%= ctx %>/Checkout" method="post">
                <input type="hidden" name="action" value="order"/>
                <input type="hidden" name="productID" value="2" />
                <label>Quantity:</label>
                <input type="number" name="quantity" value="1" min="1" />
                <button type="submit">Order Now</button>
            </form>
        </div>
        <!-- Product 3 -->
        <div class="product">
            <img src="<%= ctx %>/Images/LEDLight.png" alt="Smart Light">
            <h3>Smart Light</h3>
            <p>Adjustable smart lighting for energy savings.</p>
            <form action="<%= ctx %>/Checkout" method="post">
                <input type="hidden" name="action" value="order"/>
                <input type="hidden" name="productID" value="3" />
                <label>Quantity:</label>
                <input type="number" name="quantity" value="1" min="1" />
                <button type="submit">Order Now</button>
            </form>
        </div>
        <!-- Product 4 -->
        <div class="product">
            <img src="<%= ctx %>/Images/SecurityCamera.png" alt="Security Camera">
            <h3>Security Camera</h3>
            <p>High definition camera with remote monitoring.</p>
            <form action="<%= ctx %>/Checkout" method="post">
                <input type="hidden" name="action" value="order"/>
                <input type="hidden" name="productID" value="4" />
                <label>Quantity:</label>
                <input type="number" name="quantity" value="1" min="1" />
                <button type="submit">Order Now</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>
