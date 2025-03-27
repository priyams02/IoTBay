<%--
    Document:index
    Created on:March 25, 3.45
    Author: Bryce

    Purpose: The landing page of IotBay
--%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>IoTBay</title>
    <link rel="stylesheet" href = styles/IoTBayStyles.css>
</head>
<body>
<div class="IndexDivMain">
    <!-- Top Menu Bar -->
    <nav>
        <div>
            <div class ="navLinks left"><a href ="index.jsp">Home</a></div>
            <%-- Some user authenthication for when they log in will be implemented later--%>

            <div class="navLinks right"><a href ="login.jsp">Login</a></div>

            <div class="navLinks right"><a href ="register.jsp">Register</a></div>

    </nav>
    <h1 class="IndexH1">IotBay</h1>
    <!--Content Area -->
    <div>
        <p class="paragraph textarea">
            IotBay | Introduction to Software Development Assignment 1:R0
            <br><br>
            The internet of Things Store (IoTBay) is a small company based in Sydney Australia.
            IotBay wants to develop an online IoT devices ordering application to allow their customers
            to purchase IoT devices
        </p>
    </div>
</div>
</body>
</html>