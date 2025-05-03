<%@ page import="uts.isd.model.Person.User" %>
<%@ page import="uts.isd.model.Person.Address" %>
<%@ page import="uts.isd.model.Person.Staff" %>
<%@ page import="uts.classes.DAO.DBConnector" %>
<%@ page import="uts.classes.DAO.DBManager" %>
<%@ page import="java.sql.SQLException" %>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Retrieve the logged-in user from session
    User user = (User) session.getAttribute("loggedInUser");
    //DB Manager
    DBManager db = (DBManager) session.getAttribute("db");
    if (db == null) {
        try {
            db = new DBManager(new DBConnector().getConnection());
            session.setAttribute("db", db);
        } catch (SQLException e) {
            System.out.println("Failed to connect to database");
        }
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>IoTBay</title>
    <link rel="stylesheet" href="styles/IoTBayStyles.css">
</head>
<body>
<div class="IndexDivMain">
    <!-- Top Menu Bar -->
    <!-- I think when we finish up the initial design let's make the nav bar horizontal instead of vertical -->
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

    <!-- Content Area -->
    <div>
        <p class="paragraph textarea">
            IoTBay | Introduction to Software Development Assignment 1: R0
            <br><br>
            The internet of Things Store (IoTBay) is a small company based in Sydney, Australia.
            IoTBay wants to develop an online IoT devices ordering application to allow their customers
            to purchase IoT devices.
        </p>
    </div>
    <%--    Show up customer/User information--%>
    <div>
        <div class ="CentreScreen">
            <%
                try {
                    User active = (User) session.getAttribute("loggedInUser");
                    if (active != null) {
                        String firstName = active.getFirstName();
                        String lastName = active.getLastName();

                        if (firstName != null && lastName != null) {
                            out.println("<h1>Hello, " + firstName + " " + lastName + "!</h1>");
                        } else {
                            out.println("<h1>Hello Customer!</h1>");
                        }

                        out.println("<br><p class=\"text\">");
                        out.println("Your E-Mail Address is: " + active.getEmail());
                        out.println("<br>");
                        out.println("Your Password is: " + active.getPassword());
                        out.println("</p>");
                    }
                } catch (ClassCastException c) {
                    Staff active = (Staff) session.getAttribute("User");
                    if (active != null) {
                        String firstName = active.getFirstName();
                        String lastName = active.getLastName();

                        if (firstName != null && lastName != null) {
                            out.println("<h1>Hello, " + firstName + " " + lastName + "!</h1>");
                        } else {
                            out.println("<h1>Hello!</h1>");
                        }

                        out.println("<br><p class=\"text\">");
                        out.println("Your E-Mail Address is: " + active.getEmail());
                        out.println("<br>");
                        out.println("Your Password is: " + active.getPassword());
                    }
                }
            %>
        </div>

    <!-- Logged-in User Information -->
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
    </div>
</body>
</html>

<script>

    function logout() {
        window.location.href = "LogoutHandler.jsp";
    }
</script>