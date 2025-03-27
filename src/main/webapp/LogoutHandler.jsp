<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    // Ends session to log the user out
    session.invalidate();

    // Redirect to home page. Someone change to logout page when it's ready
    response.sendRedirect("index.jsp");
%>