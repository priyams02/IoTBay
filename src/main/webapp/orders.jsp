<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
  String msg = request.getParameter("msg");
  Integer lastOrderId = (Integer) session.getAttribute("lastOrderId");
%>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Your Orders</title></head>
<body>
<h1>Your Orders</h1>
<% if (msg != null) { %>
<p style="color:green;"><%= msg %></p>
<% } %>
<% if (lastOrderId != null) { %>
<p>Most recent Order ID: <%= lastOrderId %></p>
<p>
  <a href="<%= request.getContextPath() %>/Shipment/List?orderId=<%= lastOrderId %>">
    View Shipments
  </a>
</p>
<% } %>
</body>
</html>
