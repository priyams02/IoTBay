<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
    String ctx = request.getContextPath();

    // 1) Pull the attribute (an Integer), or fallback to the parameter
    Object oidObj = request.getAttribute("orderId");
    String oid    = (oidObj != null)
            ? oidObj.toString()
            : request.getParameter("orderId");

    // 2) Guard against missing or invalid
    if (oid == null) {
        out.println("<p style='color:red;'>Error: missing orderId.</p>");
        return;
    }
    int orderId;
    try {
        orderId = Integer.parseInt(oid);
    } catch (NumberFormatException e) {
        out.println("<p style='color:red;'>Error: invalid orderId “" + oid + "”.</p>");
        return;
    }

    @SuppressWarnings("unchecked")
    java.util.List<uts.isd.model.Shipment> list =
            (java.util.List<uts.isd.model.Shipment>) request.getAttribute("shipments");
    if (list == null) {
        out.println("<p style='color:red;'>No shipments found for order #" + orderId + ".</p>");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Shipments for Order #<%= orderId %></title>
    <link rel="stylesheet" href="<%= ctx %>/styles/IoTBayStyles.css">
</head>
<body>
<h1>Shipments for Order #<%= orderId %></h1>
<p>
    <a href="<%= ctx %>/Shipment/Create?orderId=<%= orderId %>">
        Create New Shipment
    </a>
</p>
<table border="1" cellpadding="6">
    <tr>
        <th>ID</th>
        <th>Destination</th>
        <th>Status</th>
        <th>Options</th>
        <th>Actions</th>
    </tr>
    <% for (uts.isd.model.Shipment s : list) { %>
    <tr>
        <td><%= s.getShipmentId() %></td>
        <td>
            <%= s.getDestination().getNumber() %>
            <%= s.getDestination().getStreetName() %>,
            <%= s.getDestination().getSuburb() %>
            <%= s.getDestination().getPostcode() %>,
            <%= s.getDestination().getCity() %>
        </td>
        <td><%= s.getStatus() %></td>
        <td><%= s.getShippingOptions() %></td>
        <td>
            <a href="<%= ctx %>/Shipment/Update?id=<%= s.getShipmentId()
                      %>&orderId=<%= orderId %>">Edit</a>
            &nbsp;|&nbsp;
            <form action="<%= ctx %>/Shipment/Delete" method="post" style="display:inline">
                <input type="hidden" name="id"      value="<%= s.getShipmentId() %>">
                <input type="hidden" name="orderId" value="<%= orderId %>">
                <button type="submit">Delete</button>
            </form>
        </td>
    </tr>
    <% } %>
</table>
<p><a href="<%= ctx %>/index.jsp">Back Home</a></p>
</body>
</html>
