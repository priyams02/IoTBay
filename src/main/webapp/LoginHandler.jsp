<%@ page import="main.classes.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    // Retrieve form data
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    // Replace with database for assessment 2
    // If i get around to it I will try to make this remember memory bt for now do this i guess
    if ("test@iotbay.com".equals(username) && "Password123!".equals(password)) {
        User user = new User();
        user.setEmail(username);
        user.setPassword(password);

        // Save user in session
        session.setAttribute("loggedInUser", user);

        // Redirect to index.jsp if valid
        response.sendRedirect("index.jsp");
    } else {
        // Redirect back to login page if invalid
        response.sendRedirect("login.jsp?error=invalid");
    }
%>