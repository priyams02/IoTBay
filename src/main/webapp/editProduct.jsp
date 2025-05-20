<%@ page import="uts.isd.model.Product" %>
<%@ page import="uts.isd.model.Person.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String ctx = request.getContextPath();
    User user = (User) session.getAttribute("loggedInUser");
    if (user == null || user.getType() != User.UserType.STAFF) {
        response.sendRedirect(ctx + "/ListProducts?err=Not+authorized");
        return;
    }
    Product p = (Product) request.getAttribute("product");
    if (p == null) {
        response.sendRedirect(ctx + "/ListProducts?err=Product+not+found");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Product – <%= p.getName() %></title>
    <style>
        .container { width: 400px; margin: 40px auto; font-family: Arial, sans-serif; }
        .field { margin: 10px 0; }
        .field label { display: block; margin-bottom: 4px; font-weight: bold; }
        .field input { width: 100%; padding: 6px; }
        .actions { margin-top: 20px; }
        .actions button, .actions a { margin-right: 10px; padding: 6px 12px; border:none; border-radius:4px; text-decoration:none;   }
        .actions button { background: #28a745; color: #fff; }
        .actions a { background: #6c757d; color: #fff; }
    </style>
</head>
<body>
<div class="container">
    <h2>Edit Product</h2>
    <form method="post" action="<%= ctx %>/UpdateProduct">
        <!-- Preserve the ID -->
        <input type="hidden" name="ProductId" value="<%= p.getProductId() %>" />

        <div class="field">
            <label for="ProductName">Name</label>
            <input id="ProductName" name="ProductName" value="<%= p.getName() %>" required />
        </div>

        <div class="field">
            <label for="ProductCategory">Category</label>
            <input id="ProductCategory" name="ProductCategory" value="<%= p.getCategory() %>" required />
        </div>

        <div class="field">
            <label for="ProductPrice">Price</label>
            <input id="ProductPrice" name="ProductPrice" value="<%= p.getPrice() %>" required />
        </div>

        <div class="field">
            <label for="ProductStock">Stock</label>
            <input id="ProductStock" name="ProductStock" value="<%= p.getStock() %>" required />
        </div>

        <div class="actions">
            <button type="submit">Save Changes</button>
            <a href="<%= ctx %>/ViewProduct.jsp?id=<%= p.getProductId() %>">Cancel</a>
        </div>
    </form>
</div>
</body>
</html>
