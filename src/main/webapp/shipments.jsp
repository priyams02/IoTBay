<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="uts.isd.model.Shipment" %>
<%
    String ctx = request.getContextPath();
    String oid = request.getParameter("orderId");
    if (oid == null) {
        out.println("<p style='color:red;'>Error: missing orderId.</p>");
        return;
    }
    int orderId;
    try { orderId = Integer.parseInt(oid); }
    catch (NumberFormatException e) {
        out.println("<p style='color:red;'>Error: invalid orderId “" + oid + "”.</p>");
        return;
    }

    @SuppressWarnings("unchecked")
    List<Shipment> list = (List<Shipment>) request.getAttribute("shipments");
    if (list == null) {
        out.println("<p style='color:red;'>No shipments found for order #" + orderId + ".</p>");
        return;
    }

    // ─── Pager window setup ───────────────────────────────────
    int windowSize = 5;
    int halfWindow = windowSize / 2;
    int startPage  = Math.max(1, orderId - halfWindow);
    int endPage    = orderId + halfWindow;
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Shipments for Order #<%= orderId %></title>
    <link rel="stylesheet" href="<%= ctx %>/styles/IoTBayStyles.css">
    <style>
        /* ─── Pager styling & centering ───────────────────────── */
        .pagination {
            list-style: none;
            padding: 0;
            display: flex;
            gap: .5em;
            /* center the whole pager */
            justify-content: center;
            margin: 2em auto;
        }
        .pagination li { display: inline; }
        .pagination a, .pagination span {
            padding: .5em .75em;
            text-decoration: none;
            border: 1px solid #666;
            border-radius: 4px;
            color: #333;
            min-width: 2.5em;
            text-align: center;
        }
        .pagination .active span {
            background: #333;
            color: #fff;
            border-color: #333;
            cursor: default;
        }
        .pagination .disabled span {
            color: #aaa;
            border-color: #aaa;
            cursor: default;
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
        <th>ID</th><th>Destination</th><th>Status</th><th>Options</th><th>Actions</th>
    </tr>
    <% for (Shipment s : list) { %>
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

<!-- ─── Pager ─────────────────────────────────── -->
<ul class="pagination">
    <!-- Prev -->
    <li class="<%= orderId <= 1 ? "disabled" : "" %>">
        <% if (orderId > 1) { %>
        <a href="<%= ctx %>/Shipment/List?orderId=<%= orderId-1 %>">Prev</a>
        <% } else { %>
        <span>Prev</span>
        <% } %>
    </li>
    <!-- Page numbers -->
    <% for (int i = startPage; i <= endPage; i++) { %>
    <li class="<%= (i == orderId) ? "active" : "" %>">
        <% if (i == orderId) { %>
        <span><%= i %></span>
        <% } else { %>
        <a href="<%= ctx %>/Shipment/List?orderId=<%= i %>"><%= i %></a>
        <% } %>
    </li>
    <% } %>
    <!-- Next (always enabled) -->
    <li>
        <a href="<%= ctx %>/Shipment/List?orderId=<%= orderId+1 %>">Next</a>
    </li>
</ul>

<p><a href="<%= ctx %>/index.jsp">Back Home</a></p>
</body>
</html>
