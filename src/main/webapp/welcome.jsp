<%--
  Created by IntelliJ IDEA.
  User: Richard
  Date: 3/25/2025
  Time: 7:47 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>IoTBay - Welcome</title>

</head>
<style>
    body {
        font-family: Arial, sans-serif;
        margin: 0;
        padding: 0;
        background: #f5f5f5;
        text-align: center;
        position: relative;
        min-height: 100vh;
    }
    .navbar {
        background: #232f3e;
        padding: 15px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        color: whitesmoke;

    }
    .search-box {
        display: flex;
        align-items: center;
    }
    .search-box input {
        padding: 8px;
        border: none;
        border-radius: 5px;
        margin-right: 10px;
    }
    .search-box button {
        padding: 8px 12px;
        border: none;
        background: #37475a;
        color: white;
        cursor: pointer;
        border-radius: 5px;
    }
    .search-box button:hover {
        background: #485769;
    }
    .welcome-container {
        margin-top: 100px;
    }
    .welcome-text h1 {
        font-size: 2.5em;
        margin-bottom: 20px;
    }
    .enter-button {
        padding: 15px 30px;
        font-size: 1.2em;
        color: white;
        background: #232f3e;
        border: none;
        cursor: pointer;
        border-radius: 5px;
        text-decoration: none;
    }
    .enter-button:hover {
        background: #37475a;
    }
    .session-info {
        position: absolute;
        bottom: 10px;
        right: 10px;
        font-size: 1em;
        color: #333;
    }
    .logout-button {
        position: absolute;
        bottom: 10px;
        left: 10px;
        padding: 10px 20px;
        font-size: 1em;
        color: white;
        background: #d9534f;
        border: none;
        cursor: pointer;
        border-radius: 5px;
        text-decoration: none;
    }
    .logout-button:hover {
        background: #c9302c;
    }

</style>
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
    <a href="main.html" class="enter-button">Enter Store</a>
</div>
<div class="session-info">
    <h1>Current Session:</h1>
</div>
<a href="logout.html" class="logout-button">Logout</a>
</body>
</html>
