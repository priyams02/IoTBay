<%@ page import="main.classes.User" %>
<%@ page import="main.classes.UserDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    // Retrieve form data
    String firstName = request.getParameter("firstName");
    String lastName = request.getParameter("lastName");
    String email = request.getParameter("email");
    String password = request.getParameter("password");
    String tos = request.getParameter("tos");

    if (tos == null) {
        response.sendRedirect("register.jsp?error=tos");
    } else {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);

        UserDAO.addUser(user);

        // Save user in session (so they're "logged in" right away)
        session.setAttribute("loggedInUser", user);

        response.sendRedirect("index.jsp");
    }
%>
