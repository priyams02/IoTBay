<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List, uts.isd.model.Order, uts.isd.model.Person.User" %>
<%
  String msg    = request.getParameter("msg");
  String err    = request.getParameter("err");
  List<Order> orders = (List<Order>) request.getAttribute("orders");
  User user     = (User) session.getAttribute("loggedInUser");
  String email  = (user != null) ? user.getEmail() : request.getParameter("email");
  String ctx    = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Your Orders</title>
  <link rel="stylesheet" href="<%= ctx %>/styles/IoTBayStyles.css">
</head>
<body>
<h1>Your Orders</h1>

<% if (msg != null) { %>
<p style="color:green;"><%= msg %></p>
<% } %>
<% if (err != null) { %>
<p style="color:red;"><%= err %></p>
<% } %>

<% if (orders != null && !orders.isEmpty()) { %>
<table border="1" cellpadding="5" cellspacing="0">
  <tr>
    <th>Order ID</th>
    <th>Date</th>
    <th>Total Cost</th>
    <th>Status</th>
    <th>Shipments</th>
  </tr>
  <% for (Order o : orders) { %>
  <tr>
    <td><%= o.getId() %></td>
    <td><%= o.getFormattedPurchaseDate() %></td>
    <td>$<%= o.getTotalCost() %></td>
    <td><%= o.getStatus() %></td>
    <td>
      <a href="<%= ctx %>/Shipment/List?orderId=<%= o.getId() %>">
        View Shipments
      </a>
    </td>
  </tr>
  <% } %>
</table>

<h2>Update Order Status</h2>
<form action="<%= ctx %>/Update" method="post">
  <input type="hidden" name="email" value="<%= email %>">
  <label>Order ID:
    <input type="number" name="orderId" required>
  </label><br>
  <label>New Status:
    <select name="status" required>
      <option value="Pending">Pending</option>
      <option value="Shipped">Shipped</option>
      <option value="Delivered">Delivered</option>
      <option value="Cancelled">Cancelled</option>
    </select>
  </label><br>
  <button type="submit">Update</button>
</form>

<h2>Cancel Order</h2>
<form action="<%= ctx %>/CancelOrder" method="post">
  <input type="hidden" name="owner" value="<%= email %>">
  <label>Order ID to Cancel:
    <input type="number" name="oid" required>
  </label><br>
  <button type="submit">Cancel</button>
</form>

<% } else { %>
<p>You have no orders yet.</p>
<% } %>

<p><a href="<%= ctx %>/index.jsp">Return Home</a></p>
</body>
</html>
