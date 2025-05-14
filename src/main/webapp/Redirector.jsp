<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
  String ctx     = request.getContextPath();
  String heading = request.getParameter("HeadingMessage");
  String message = request.getParameter("Message");
  // fallback defaults
  if (heading == null) heading = "Done!";
  if (message == null) message = "Redirecting...";
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title><%= heading %></title>
  <!-- auto‐redirect after 3 seconds -->
  <meta http-equiv="refresh" content="3;url=<%= ctx %>/index.jsp" />
  <link rel="stylesheet" href="<%= ctx %>/styles/IoTBayStyles.css">
</head>
<body>
<div class="IndexDivMain" style="text-align:center; padding-top: 5rem;">
  <h1 style="font-size: 3rem; color: #2c3e50;"><%= heading.toUpperCase() %></h1>
  <p style="font-size: 1.25rem; margin-top: 1rem;"><%= message %></p>
  <p style="margin-top: 2rem;">
    <a href="<%= ctx %>/index.jsp" style="font-size: 1.1rem; color: #8e44ad;">
      Return Home
    </a>
  </p>
</div>
</body>
</html>
