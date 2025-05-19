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

    // 3) Pager settings
    int maxPage = 5;  // hard-coded number of pages (1…5)
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Shipments for Order #<%= orderId %></title>
    <link rel="stylesheet" href="<%= ctx %>/styles/IoTBayStyles.css">
    <style>
        /* Pager styling */
        .pagination { list-style: none; display: flex; gap: .5em; padding: 0; margin-top: 1em; }
        .pagination li { display: inline; }
        .pagination a, .pagination span {
            display: block; padding: .5em .75em; text-decoration: none;
            border: 1px solid #666; border-radius: 4px; color: #333;
        }
        .pagination .active span {
            background: #333; color: #fff; border-color: #333; cursor: default;
        }
        .pagination .disabled span {
            color: #aaa; border-color: #aaa; cursor: default;
        }
    </style>
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

<!-- ─── Pager ────────────────────────────────────────── -->
<ul class="pagination">
    <!-- Prev -->
    <li class="<%= orderId <= 1 ? "disabled" : "" %>">
        <% if (orderId > 1) { %>
        <a href="<%= ctx %>/Shipment/List?orderId=<%= orderId - 1 %>">Prev</a>
        <% } else { %>
        <span>Prev</span>
        <% } %>
    </li>

    <!-- Page numbers 1…maxPage -->
    <% for (int i = 1; i <= maxPage; i++) { %>
    <li class="<%= (i == orderId) ? "active" : "" %>">
        <% if (i == orderId) { %>
        <span><%= i %></span>
        <% } else { %>
        <a href="<%= ctx %>/Shipment/List?orderId=<%= i %>"><%= i %></a>
        <% } %>
    </li>
    <% } %>

    <!-- Next -->
    <li class="<%= orderId >= maxPage ? "disabled" : "" %>">
        <% if (orderId < maxPage) { %>
        <a href="<%= ctx %>/Shipment/List?orderId=<%= orderId + 1 %>">Next</a>
        <% } else { %>
        <span>Next</span>
        <% } %>
    </li>
</ul>

<p><a href="<%= ctx %>/index.jsp">Back Home</a></p>
</body>
</html>
