<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, uts.isd.model.AccessLog, uts.isd.model.Person.User" %>
<%
    String ctx = request.getContextPath();
    User user = (User) session.getAttribute("loggedInUser");
    if (user == null) {
        response.sendRedirect(ctx + "/login.jsp");
        return;
    }

    @SuppressWarnings("unchecked")
    List<AccessLog> logs = (List<AccessLog>) request.getAttribute("logs");
    String filterDate = request.getParameter("date");
    if (filterDate == null) filterDate = "";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Your Profile & Access Logs</title>
    <link rel="stylesheet" href="<%=ctx%>/styles/IoTBayStyles.css">
    <link rel="stylesheet" href="<%=ctx%>/styles/view-profile.css">
</head>
<body>
<!-- Top Menu Bar -->
<nav class="navbar">
    <div class="navLinks left">
        <a href="<%= ctx %>/index.jsp">Home</a>
    </div>
    <div class="navLinks right">
        <a href="<%= ctx %>/shop.jsp">Shop</a>
        <% if (user == null) { %>
        <a href="<%= ctx %>/LoginServlet">Login</a>
        <a href="<%= ctx %>/RegisterServlet">Register</a>
        <% } else { %>
        <!-- Hard-coded Profile links -->
        <a href="<%= ctx %>/Profile.jsp">Update Profile</a>
        <a href="<%= ctx %>/ViewProfile.jsp">View Profile</a>
        <!-- Hard-coded Shipments link always pointing to orderId=1 -->
        <a href="<%= ctx %>/Shipment/List?orderId=1">Shipments</a>
        <a href="<%= ctx %>/LogoutServlet">Logout</a>
        <% } %>
    </div>
</nav>
<div class="view-profile-wrapper">
<h1>Welcome, <%= user.getFirstName() %>!</h1>

<!-- PROFILE INFO -->
<section>
    <h2>Your Details</h2>
    <p><strong>Email:</strong> <%= user.getEmail() %></p>
    <p><strong>Name:</strong> <%= user.getFirstName() %> <%= user.getLastName() %></p>
    <p><strong>Address:</strong><%= user.getAddress() %></p>
    <p><strong>Password:</strong><%= user.getPassword() %></p>
    <p><strong>Payment Information:<%=user.getPaymentInfo()%></strong></p>
    <p><a href="<%=ctx%>/Profile.jsp">update Profile</a></p>
</section>

<!-- ACCESS LOGS -->
<section style="margin-top:2em;">
    <h2>Your Access Logs</h2>
    <form method="get" action="<%=ctx%>/AccessLogs">
        <label>
            Filter by date:
            <input type="date" name="date" value="<%=filterDate%>"/>
        </label>
        <button type="submit">Filter</button>
        <a href="<%=ctx%>/AccessLogs">Show All</a>
    </form>

    <table border="1" cellpadding="6" style="margin-top:1em;">
        <tr>
            <th>Log ID</th>
            <th>Action</th>
            <th>Timestamp</th>
        </tr>
        <%
            if (logs != null && !logs.isEmpty()) {
                for (AccessLog log : logs) {
        %>
        <tr>
            <td><%= log.getLogId() %></td>
            <td><%= log.getAction() %></td>
            <td><%= log.getTimestamp() %></td>
        </tr>
        <%
            }
        } else {
        %>
        <tr>
            <td colspan="3">No access records found.</td>
        </tr>
        <%
            }
        %>
    </table>
</section>

<p style="margin-top:2em;"><a href="<%=ctx%>/index.jsp">&larr; Home</a></p>

<div>
</body>
</html>