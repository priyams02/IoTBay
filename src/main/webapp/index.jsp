<%@ page import="uts.isd.model.Person.User" %>
<%@ page import="uts.isd.model.Person.Address" %>
<%@ page import="uts.isd.model.Person.Staff" %>
<%@ page import="uts.isd.model.DAO.DBConnector" %>
<%@ page import="uts.isd.model.DAO.CustomerDBManager" %>
<%@ page import="java.sql.SQLException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    // Retrieve the logged-in user once, so 'user' is available everywhere
    User user = (User) session.getAttribute("loggedInUser");

    // Retrieve or initialize your DAO (swap out CustomerDBManager for whichever you need)
    CustomerDBManager db = (CustomerDBManager) session.getAttribute("db");
    if (db == null) {
        try {
            db = new CustomerDBManager(new DBConnector().getConnection());
            session.setAttribute("db", db);
        } catch (SQLException e) {
            // Log or handle
            e.printStackTrace();
        }
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>IoTBay</title>
    <link rel="stylesheet" href="styles/IoTBayStyles.css">
</head>
<body>
<div class="IndexDivMain">
    <!-- Top Menu Bar -->
    <nav class="navbar">
        <div class="navLinks left">
            <a href="index.jsp">Home</a>
        </div>
        <div class="navLinks right">
            <a href="shop.jsp">Shop</a>
            <% if (user == null) { %>
            <a href="login.jsp">Login</a>
            <a href="register.jsp">Register</a>
            <% } else { %>
            <a href="LogoutHandler.jsp">Logout</a>
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
        <%
            if (user != null) {
                String name = (user.getFirstName() != null && user.getLastName() != null)
                        ? user.getFirstName() + " " + user.getLastName()
                        : "Customer";
                out.println("<h1>Hello, " + name + "!</h1>");
                out.println("<p>Your E-Mail: " + user.getEmail() + "<br>Password: " + user.getPassword() + "</p>");
            } else {
                // try Staff
                Staff staff = (Staff) session.getAttribute("User");
                if (staff != null) {
                    String name = (staff.getFirstName() != null && staff.getLastName() != null)
                            ? staff.getFirstName() + " " + staff.getLastName()
                            : "";
                    out.println("<h1>Hello" + (name.isEmpty() ? "!" : ", " + name + "!") + "</h1>");
                    out.println("<p>Your E-Mail: " + staff.getEmail() + "<br>Password: " + staff.getPassword() + "</p>");
                }
            }
        %>
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
        window.location.href = "LogoutHandler.jsp";
    }
</script>
</body>
</html>
