<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
  String heading = request.getParameter("HeadingMessage");
  String message = request.getParameter("Message");
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title><%= heading %></title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/IoTBayStyles.css">
</head>
<body>
<h1><%= heading %></h1>
<p><%= message %></p>
<p><a href="<%= request.getContextPath() %>/index.jsp">Return Home</a></p>
</body>
</html>
