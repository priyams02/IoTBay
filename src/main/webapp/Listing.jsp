<%@ page import="java.util.List" %>
<%@ page import="uts.isd.model.Product" %>
<%@ page import="uts.isd.model.Person.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>IoTBay | Product Listing</title>
    <style>
        table { width: 80%; margin: auto; border-collapse: collapse; }
        th, td { padding: 10px; border: 1px solid #ccc; text-align: center; }
        .message { text-align: center; color: green; font-weight: bold; }
        .error { text-align: center; color: red; font-weight: bold; }
        .search-box { text-align: center; margin: 20px; }
        .search-box input[type="text"] { padding: 6px; width: 250px; }
        .search-box input[type="submit"] { padding: 6px 12px; }
    </style>
</head>
<body>

<h2 style="text-align: center;">Available IoT Devices</h2>

<!-- Search Form -->
<div class="search-box">
    <form method="post" action="<%= request.getContextPath() %>/SearchProduct">
        <input type="text" name="Params" placeholder="Search by name..." value="<%= request.getParameter("param") != null ? request.getParameter("param") : "" %>" />
        <input type="submit" value="Search" />
        <a href="<%= request.getContextPath() %>/ListProducts">Clear</a>
    </form>
</div>

<!-- Display messages -->
<%
    String msg = request.getParameter("msg");
    String err = request.getParameter("err");
    if (msg != null) {
%>
<div class="message"><%= msg %></div>
<%
} else if (err != null) {
%>
<div class="error"><%= err %></div>
<%
    }

    List<Product> products = (List<Product>) session.getAttribute("searchedProducts");
    if (products == null) {
        products = (List<Product>) request.getAttribute("products");
    }

    User user = (User) session.getAttribute("loggedInUser");
    boolean isStaff = user != null && user.getType() == User.UserType.STAFF;
%>

<!-- Product Table -->
<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Category</th>
        <th>Price</th>
        <th>Stock</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <%
        if (products != null && !products.isEmpty()) {
            for (Product p : products) {
    %>
    <%
        System.out.println("Listing.jsp - products size: " + (products == null ? "null" : products.size()));
    %>

    <tr>
        <td><%= p.getProductId() %></td>
        <td><%= p.getName() %></td>
        <td><%= p.getCategory() %></td>
        <td>$<%= String.format("%.2f", p.getPrice()) %></td>
        <td><%= p.getStock() %></td>
        <td>
            <a href="<%= request.getContextPath() %>/ViewProduct?id=<%= p.getProductId() %>">View</a>
            <%
                if (isStaff) {
            %>
            | <a href="<%= request.getContextPath() %>/UpdateProduct?id=<%= p.getProductId() %>">Edit</a>
            <form action="<%= request.getContextPath() %>/DeleteProduct" method="post" style="display:inline;">
                <input type="hidden" name="ProductID" value="<%= p.getProductId() %>" />
                <input type="submit" value="Delete" onclick="return confirm('Are you sure you want to delete this product?');" />
            </form>
            <%
                }
            %>
        </td>
    </tr>
    <%
        }
    } else {
    %>
    <tr>
        <td colspan="6">No products found.</td>
    </tr>
    <%
        }
    %>
    </tbody>
</table>

</body>
</html>
