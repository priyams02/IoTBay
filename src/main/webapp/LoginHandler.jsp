<%@ page import="main.classes.User" %>
<%@ page import="main.classes.UserDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    User user = UserDAO.findUserByEmail(username);

    if (user != null && user.getPassword().equals(password)) {
        session.setAttribute("loggedInUser", user);
        response.sendRedirect("index.jsp");
    } else {
        response.sendRedirect("login.jsp?error=invalid");
    }
%>
