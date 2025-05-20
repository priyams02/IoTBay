<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="uts.isd.model.Order" %>
<%@ page import="uts.isd.model.Person.User" %>
<%
  String ctx = request.getContextPath();
  String msg = request.getParameter("msg");
  String err = request.getParameter("err");
  @SuppressWarnings("unchecked")
  List<Order> orders = (List<Order>) request.getAttribute("orders");
  User user = (User) session.getAttribute("loggedInUser");
  // Support for guest users
  String email = (user != null) ? user.getEmail() : request.getParameter("email");
  if (email == null || email.isEmpty()) email = "";
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Your Orders</title>
  <link rel="stylesheet" href="<%= ctx %>/styles/IoTBayStyles.css">
  <style>
    table { border-collapse: collapse; }
    th, td { padding: 8px 12px; }
    th { background: #f3f3f3; }
    tr:nth-child(even) { background: #f9f9f9; }
    .msg-success { color: green; }
    .msg-error { color: red; }
  </style>
</head>
<body>
<h1>Your Orders</h1>

<% if (msg != null) { %>
<p class="msg-success"><%= msg %></p>
<% } %>
<% if (err != null) { %>
<p class="msg-error"><%= err %></p>
<% } %>

<p>
  <b>Instructions:</b> Use the dropdown to update order status, or press "Cancel" to cancel an order before it is confirmed.
</p>

<!-- Search form (by Order ID or Date) -->
<form method="get" action="<%= ctx %>/Orders">
  <input type="hidden" name="email" value="<%= email %>">
  <label>Order ID: <input type="number" name="orderId"></label>
  <label>Date: <input type="date" name="orderDate"></label>
  <button type="submit">Search</button>
  <a href="<%= ctx %>/Orders?email=<%= email %>">Show All</a>
</form>

<% if (orders != null && !orders.isEmpty()) { %>
<table border="1">
  <tr>
    <th>Order ID</th>
    <th>Date</th>
    <th>Total Cost</th>
    <th>Status</th>
    <th>Actions</th>
  </tr>
  <% for (Order o : orders) { %>
  <tr>
    <td><%= o.getId() %></td>
    <td><%= o.getFormattedPurchaseDate() %></td>
    <td>$<%= o.getTotalCost() %></td>
    <td><%= o.getStatus() %></td>
    <td>
      <!-- Update Order Status -->
      <form action="<%= ctx %>/Orders" method="post" style="display:inline;">
        <input type="hidden" name="action" value="update"/>
        <input type="hidden" name="email" value="<%= email %>"/>
        <input type="hidden" name="orderId" value="<%= o.getId() %>"/>
        <select name="status">
          <option value="Pending" <%= "Pending".equals(o.getStatus()) ? "selected" : "" %>>Pending</option>
          <option value="Confirmed" <%= "Confirmed".equals(o.getStatus()) ? "selected" : "" %>>Confirmed</option>
          <option value="Cancelled" <%= "Cancelled".equals(o.getStatus()) ? "selected" : "" %>>Cancelled</option>
        </select>
        <button type="submit">Update</button>
      </form>
      <!-- Cancel Order -->
      <form action="<%= ctx %>/Orders" method="post" style="display:inline;">
        <input type="hidden" name="action" value="cancel"/>
        <input type="hidden" name="owner" value="<%= email %>"/>
        <input type="hidden" name="orderID" value="<%= o.getId() %>"/>
        <button type="submit" onclick="return confirm('Are you sure you want to cancel this order?')">Cancel</button>
      </form>
    </td>
  </tr>
  <% } %>
</table>
<% } else { %>
<p>You haven't placed any orders yet.</p>
<% } %>

<p><a href="<%= ctx %>/index.jsp">Return Home</a></p>
</body>
</html>
