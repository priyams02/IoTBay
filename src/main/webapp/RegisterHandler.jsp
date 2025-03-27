<%@ page import="main.classes.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    // Retrieve form data
    String name = request.getParameter("name");
    String email = request.getParameter("email");
    String password = request.getParameter("password");
    String tos = request.getParameter("tos");

    // Check if TOS is accepted (optional)
    if (tos == null) {
        response.sendRedirect("register.jsp?error=tos");
    } else {
        // Create user object using the default constructor
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        // Save user in session (mocking "logged in" state)
        session.setAttribute("loggedInUser", user);

        // Redirect to index.jsp (or welcome.jsp) on success
        response.sendRedirect("index.jsp");
    }
%>