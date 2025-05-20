<%@ page import="uts.isd.model.Product" %>
<%@ page import="uts.isd.model.Person.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String ctx = request.getContextPath();
    User user = (User) session.getAttribute("loggedInUser");
    Product p = (Product) request.getAttribute("product");
    if (p == null) {
        response.sendRedirect(ctx + "/ListProducts?err=Product+not+found");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>View Product – <%= p.getName() %></title>
    <style>
        .container { width: 400px; margin: 40px auto; font-family: Arial, sans-serif; }
        .field { margin: 8px 0; }
        .label { font-weight: bold; display: inline-block; width: 100px; }
        .actions { margin-top: 20px; }
        .actions a { margin-right: 10px; text-decoration: none; padding: 6px 12px; background:#007bff; color:#fff; border-radius:4px; }
    </style>
</head>
<body>
<div class="container">
    <h2>Product Details</h2>
    <div class="field"><span class="label">ID:</span> <%= p.getProductId() %></div>
    <div class="field"><span class="label">Name:</span> <%= p.getName() %></div>
    <div class="field"><span class="label">Category:</span> <%= p.getCategory() %></div>
    <div class="field"><span class="label">Price:</span> $<%= String.format("%.2f", p.getPrice()) %></div>
    <div class="field"><span class="label">Stock:</span> <%= p.getStock() %></div>
    <div class="actions">
        <a href="<%= ctx %>/ListProducts">Back to List</a>
        <% if (user != null && user.getType() == User.UserType.STAFF) { %>
        <a href="<%= ctx %>/UpdateProduct?id=<%= p.getProductId() %>">Edit</a>
        <% } %>
    </div>
</div>
</body>
</html>
