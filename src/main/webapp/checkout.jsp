<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="uts.isd.model.Person.User" %>
<%
    String ctx = request.getContextPath();
    User user = (User) session.getAttribute("loggedInUser");
    String email = user != null ? user.getEmail() : "";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Checkout</title>
    <link rel="stylesheet" href="<%= ctx %>/styles/IoTBayStyles.css">
</head>
<body>
<h1>Checkout</h1>
<form method="post" action="<%= ctx %>/Checkout">
    <!-- registered email (or blank for guest) -->
    <input type="hidden" name="email" value="<%= email %>">
    <!-- shipping address -->
    <label>Street Number: <input name="addNum" required></label><br>
    <label>Street Name:   <input name="addStreetName" required></label><br>
    <label>Suburb:        <input name="addSuburb" required></label><br>
    <label>Postcode:      <input name="addPostcode" required></label><br>
    <label>City:          <input name="addCity" required></label><br>
    <!-- payment info -->
    <label>Card Number:   <input name="CardNo" required></label><br>
    <label>CVV:           <input name="CVV" required></label><br>
    <label>Cardholder:    <input name="CardHolder" required></label><br>
    <button type="submit">Place Order</button>
</form>
</body>
</html>
