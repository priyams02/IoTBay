<%--
  Created by IntelliJ IDEA.
  User: Richard
  Date: 3/30/2025
  Time: 10:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome Page</title>
    <link rel="stylesheet" type="text/css" href="styles/welcome.css">

</head>

<body>
<nav class="navbar">
    <h2>IoTBay</h2>
    <div class="search-box">
        <input type="text" placeholder="Search IoT devices...">
        <button>Search</button>
    </div>
</nav>
<div class="welcome-container">
    <div class="welcome-text">
        <h1>Welcome to IoTBay</h1>
    </div>
    <a href="shop.jsp" class="enter-button">Enter</a>
</div>

<a href="logout.jsp" class="logout-button">Logout</a>
</body>
</html>
