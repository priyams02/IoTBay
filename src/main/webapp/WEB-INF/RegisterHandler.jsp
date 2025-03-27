<%--
  Created by IntelliJ IDEA.
  User: bryce
  Date: 3/27/2025
  Time: 8:45 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="main.classes.user" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String email = request.getParameter("email");
    String password = request.getParameter("password");

    User user = (User)session.getAttribute("loggedInUser");
    user.setEmail(email);
    user.setPassword(password);
%>

<html>
<head>
    <title>Title</title>
</head>
<body>

</body>
</html>
