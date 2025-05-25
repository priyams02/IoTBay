<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
    String ctx = request.getContextPath();
    uts.isd.model.Shipment s = (uts.isd.model.Shipment) request.getAttribute("shipment");
    boolean edit = (s != null);

    // If we’ve been forwarded back with errors, pull the “old” values
    String streetNumber = request.getParameter("streetNumber");
    String streetName   = request.getParameter("streetName");
    String suburb       = request.getParameter("suburb");
    String postcode     = request.getParameter("postcode");
    String city         = request.getParameter("city");
    String status       = request.getParameter("status");
    String shippingOpts = request.getParameter("shippingOptions");

    // determine orderId: from existing shipment or from query-string
    String oid = edit
            ? String.valueOf(s.getOrderId())
            : request.getParameter("orderId");
    if (oid == null) {
        out.println("<p style='color:red;'>Error: missing orderId.</p>");
        return;
    }
    int orderId = Integer.parseInt(oid);

    // error message, if any
    String err = (String)request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><%= edit ? "Edit" : "New" %> Shipment</title>
    <link rel="stylesheet" href="<%= ctx %>/styles/IoTBayStyles.css">
</head>
<body>
<h1><%= edit ? "Edit" : "New" %> Shipment for Order #<%= orderId %></h1>

<% if (err != null) { %>
<p style="color:red;"><%= err %></p>
<% } %>

<form action="<%= ctx %>/Shipment/<%= edit ? "Update" : "Create" %>" method="post">
    <% if (edit) { %>
    <input type="hidden" name="id" value="<%= s.getShipmentId() %>">
    <% } %>
    <input type="hidden" name="orderId" value="<%= orderId %>">

    <label>Street Number:</label><br>
    <input
            name="streetNumber"
            value="<%= (streetNumber!=null?streetNumber:(edit?s.getDestination().getNumber():"")) %>"
            required
            pattern="\d+"
            title="Digits only"
    ><br><br>

    <label>Street Name:</label><br>
    <input
            name="streetName"
            value="<%= (streetName!=null?streetName:(edit?s.getDestination().getStreetName():"")) %>"
            required
            pattern="[A-Za-z\s]+"
            title="Letters & spaces only"
    ><br><br>

    <label>Suburb:</label><br>
    <input
            name="suburb"
            value="<%= (suburb!=null?suburb:(edit?s.getDestination().getSuburb():"")) %>"
            required
            pattern="[A-Za-z\s]+"
            title="Letters & spaces only"
    ><br><br>

    <label>Postcode:</label><br>
    <input
            name="postcode"
            value="<%= (postcode!=null?postcode:(edit?s.getDestination().getPostcode():"")) %>"
            required
            pattern="\d{4,5}"
            title="4-5 digit postcode"
    ><br><br>

    <label>City:</label><br>
    <input
            name="city"
            value="<%= (city!=null?city:(edit?s.getDestination().getCity():"")) %>"
            required
            pattern="[A-Za-z\s]+"
            title="Letters & spaces only"
    ><br><br>

    <label>Status:</label><br>
    <input
            name="status"
            value="<%= (status!=null?status:(edit?s.getStatus():"")) %>"
            required
    ><br><br>

    <label>Shipping Options:</label><br>
    <input
            name="shippingOptions"
            value="<%= (shippingOpts!=null?shippingOpts:(edit?s.getShippingOptions():"")) %>"
            required
    ><br><br>

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
