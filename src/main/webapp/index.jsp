<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="uts.isd.model.Person.User" %>
<%@ page import="uts.isd.model.Person.Staff" %>
<%@ page import="uts.isd.model.DAO.DBConnector" %>
<%@ page import="uts.isd.model.DAO.CustomerDBManager" %>
<%@ page import="java.sql.SQLException" %>

<%
    // Grab the logged-in user
    User user = (User) session.getAttribute("loggedInUser");

    // Lazy-init a CustomerDBManager if needed
    CustomerDBManager db = (CustomerDBManager) session.getAttribute("db");
    if (db == null) {
        try {
            db = new CustomerDBManager(new DBConnector().getConnection());
            session.setAttribute("db", db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Context path for absolute links
    String ctx = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>IoTBay</title>
    <link rel="stylesheet" href="<%= ctx %>/styles/IoTBayStyles.css">
    <style>
        .navDisabled {
            color: #666;
            cursor: not-allowed;
            text-decoration: none;
            padding: 0 10px;
        }
    </style>
</head>
<body>
<div class="IndexDivMain">
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
            <% } else {
                // grab the last‐placed order ID from session
                Integer lastOrderId = (Integer) session.getAttribute("lastOrderId");
            %>
            <% if (lastOrderId != null) { %>
            <a href="<%= ctx %>/Shipment/List?orderId=<%= lastOrderId %>">
                Shipments
            </a>
            <% } else { %>
            <!-- no orders yet, disable link -->
            <span class="navDisabled">Shipments</span>
            <% } %>
            <a href="<%= ctx %>/LogoutServlet">Logout</a>
            <% } %>
        </div>
    </nav>

    <h1 class="IndexH1">IoTBay</h1>

    <div>
        <p class="paragraph textarea">
            IoTBay | Introduction to Software Development Assignment 1: R0<br><br>
            The Internet of Things Store (IoTBay) is a small company based in Sydney, Australia.
            IoTBay wants to develop an online IoT devices ordering application to allow their customers
            to purchase IoT devices.
        </p>
    </div>

    <!-- Greet the user or staff -->
    <div class="CentreScreen">
        <% if (user != null) {
            String name = (user.getFirstName() != null && user.getLastName() != null)
                    ? user.getFirstName() + " " + user.getLastName()
                    : "Customer";
        %>
        <h1>Hello, <%= name %>!</h1>
        <p>Your E-Mail: <%= user.getEmail() %><br>Password: <%= user.getPassword() %></p>
        <% } else {
            Staff staff = (Staff) session.getAttribute("User");
            if (staff != null) {
                String name = (staff.getFirstName() != null && staff.getLastName() != null)
                        ? staff.getFirstName() + " " + staff.getLastName()
                        : "";
        %>
        <h1>Hello<%= name.isEmpty() ? "!" : ", " + name + "!" %></h1>
        <p>Your E-Mail: <%= staff.getEmail() %><br>Password: <%= staff.getPassword() %></p>
        <%   }
        } %>
    </div>

    <!-- Footer showing who’s logged in -->
    <div class="login">
        <p>Logged in user:
            <% if (user == null) { %>
            No one
            <% } else { %>
            <%= user.getEmail() %>
            <% } %>
        </p>
    </div>
</div>

<script>
    function logout() {
        window.location.href = '<%= ctx %>/LogoutServlet';
    }
</script>
</body>
</html>
