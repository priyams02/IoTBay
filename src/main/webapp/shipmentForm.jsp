<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
    String ctx = request.getContextPath();
    uts.isd.model.Shipment s = (uts.isd.model.Shipment) request.getAttribute("shipment");
    boolean edit = (s != null);

    // determine orderId: from existing shipment or from query-string
    String oid = edit
            ? String.valueOf(s.getOrderId())
            : request.getParameter("orderId");
    if (oid == null) {
        out.println("<p style='color:red;'>Error: missing orderId.</p>");
        return;
    }
    int orderId = Integer.parseInt(oid);
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><%= edit ? "Edit" : "Create" %> Shipment</title>
    <link rel="stylesheet" href="<%= ctx %>/styles/IoTBayStyles.css">
</head>
<body>
<h1><%= edit ? "Edit" : "New" %> Shipment for Order #<%= orderId %></h1>
<form action="<%= ctx %>/Shipment/<%= edit ? "Update" : "Create" %>" method="post">
    <% if (edit) { %>
    <input type="hidden" name="id" value="<%= s.getShipmentId() %>">
    <% } %>
    <input type="hidden" name="orderId" value="<%= orderId %>">

    <label>Street Number:</label>
    <input name="streetNumber"
           value="<%= edit ? s.getDestination().getNumber() : "" %>" required><br>

    <label>Street Name:</label>
    <input name="streetName"
           value="<%= edit ? s.getDestination().getStreetName() : "" %>" required><br>

    <label>Suburb:</label>
    <input name="suburb"
           value="<%= edit ? s.getDestination().getSuburb() : "" %>" required><br>

    <label>Postcode:</label>
    <input name="postcode"
           value="<%= edit ? s.getDestination().getPostcode() : "" %>" required><br>

    <label>City:</label>
    <input name="city"
           value="<%= edit ? s.getDestination().getCity() : "" %>" required><br>

    <label>Status:</label>
    <input name="status"
           value="<%= edit ? s.getStatus() : "" %>" required><br>

    <label>Shipping Options:</label>
    <input name="shippingOptions"
           value="<%= edit ? s.getShippingOptions() : "" %>" required><br>

    <button type="submit">
        <%= edit ? "Update Shipment" : "Create Shipment" %>
    </button>
</form>

<p>
    <a href="<%= ctx %>/Shipment/List?orderId=<%= orderId %>">
        &larr; Back to Shipments
    </a>
</p>
</body>
</html>
