<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
    String ctx = request.getContextPath();
    uts.isd.model.Shipment s = (uts.isd.model.Shipment)request.getAttribute("shipment");
    boolean edit = (s != null);
    int orderId = edit ? s.getOrderId()
            : Integer.parseInt(request.getParameter("orderId"));
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><%= edit ? "Edit" : "Create" %> Shipment</title>
    <link rel="stylesheet" href="<%=ctx%>/styles/IoTBayStyles.css">
</head>
<body>
<h1><%= edit ? "Edit" : "New" %> Shipment for Order #<%=orderId%></h1>
<form action="<%=ctx%>/Shipment/<%= edit ? "Update" : "Create" %>" method="post">
    <% if (edit) { %>
    <input type="hidden" name="id" value="<%=s.getShipmentId()%>">
    <% } %>
    <input type="hidden" name="orderId" value="<%=orderId%>">

    <label>Street Number:</label>
    <input name="streetNumber"
           value="<%= edit ? s.getDestination().getNumber() : "" %>"><br>

    <label>Street Name:</label>
    <input name="streetName"
           value="<%= edit ? s.getDestination().getStreetName() : "" %>"><br>

    <label>Suburb:</label>
    <input name="suburb"
           value="<%= edit ? s.getDestination().getSuburb() : "" %>"><br>

    <label>Postcode:</label>
    <input name="postcode"
           value="<%= edit ? s.getDestination().getPostcode() : "" %>"><br>

    <label>City:</label>
    <input name="city"
           value="<%= edit ? s.getDestination().getCity() : "" %>"><br>

    <label>Status:</label>
    <input name="status"
           value="<%= edit ? s.getStatus() : "" %>"><br>

    <label>Shipping Options:</label>
    <input name="shippingOptions"
           value="<%= edit ? s.getShippingOptions() : "" %>"><br>

    <button type="submit"><%= edit ? "Update" : "Create" %></button>
</form>
<p>
    <a href="<%=ctx%>/Shipment/List?orderId=<%=orderId%>">
        Back to Shipments
    </a>
</p>
</body>
</html>
