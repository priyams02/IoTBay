<%@ page import="java.util.List" %>
<%@ page import="uts.isd.model.Product" %>
<%@ page import="uts.isd.model.Person.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String ctx = request.getContextPath();
    User user = (User) session.getAttribute("loggedInUser");
    Integer lastOrderId = (Integer) session.getAttribute("lastOrderId");
    // Get products from the ShopServlet
    List<Product> products = (List<Product>) request.getAttribute("products");
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
        .product-list { display: flex; flex-wrap: wrap; justify-content: center; }
        .product {
            border: 1px solid #ccc;
            border-radius: 8px;
            width: 220px;
            margin: 10px;
            padding: 12px;
            text-align: center;
        }
        .product img {
            max-width: 100%;
            height: auto;
        }
        .product button, .product a {
            margin-top: 8px;
            display: inline-block;
            padding: 6px 12px;
            border: none;
            border-radius: 4px;
            text-decoration: none;
            background-color: #007bff;
            color: white;
        }
    </style>
</head>
<body>
<nav class="navbar">
    <div class="navLinks left">
        <a href="<%= ctx %>/index.jsp">Home</a>
    </div>
    <div class="navLinks right">
        <a href="<%= ctx %>/shop">Shop</a>
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
    <h2>Available Products</h2>

    <div class="product-list">
        <%
            if (products == null || products.isEmpty()) {
        %>
        <p style="text-align:center;">No products available at the moment.</p>
        <%
        } else {
            for (Product p : products) {
        %>
        <div class="product">
            <!-- If you have image URLs, use p.getImageUrl(); otherwise use a placeholder -->
            <img src="<%= ctx %>/Images/placeholder.png" alt="<%= p.getName() %>">
            <h3><%= p.getName() %></h3>
            <p>Category: <%= p.getCategory() %></p>
            <p>Price: $<%= String.format("%.2f", p.getPrice()) %></p>
            <p>Stock: <%= p.getStock() %></p>
            <a href="<%= ctx %>/ViewProduct?id=<%= p.getProductId() %>">View Details</a>
            <button>Add to Cart</button>
        </div>
        <%
                }
            }
        %>
    </div>
</div>
</body>
</html>
