<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="uts.isd.model.Person.User" %>
<%
    String ctx = request.getContextPath();
    User user = (User) session.getAttribute("loggedInUser");
    String email        = (String) request.getAttribute("Email");
    String streetNum    = (String) request.getAttribute("addNum");
    String street       = (String) request.getAttribute("addStreetName");
    String suburb       = (String) request.getAttribute("addSuburb");
    String postcode     = (String) request.getAttribute("addPostcode");
    String city         = (String) request.getAttribute("addCity");
    String cardNo       = (String) request.getAttribute("cardNo");
    String CVV          = (String) request.getAttribute("CVV");
    String cardHolder   = (String) request.getAttribute("cardHolder");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Checkout</title>
    <link rel="stylesheet" href="<%= ctx %>/styles/IoTBayStyles.css">
</head>
<body>
<h1>Update Payment Details</h1>
<form method="post" action="<%= ctx %>/EditPaymentDetailsServlet">
    <!-- registered email (or blank for guest) -->
    <input type="hidden" name="email" value="<%= email %>">
    <!-- shipping address -->
    <label>Street Number: <input name="addNum" value="<%= streetNum  != null ? streetNum  : "" %>" required></label><br>
    <label>Street Name:   <input name="addStreetName" value="<%= street  != null ? street  : "" %>" required></label><br>
    <label>Suburb:        <input name="addSuburb" value="<%= suburb  != null ? suburb  : "" %>" required></label><br>
    <label>Postcode:      <input name="addPostcode" value="<%= postcode  != null ? postcode : "" %>" required></label><br>
    <label>City:          <input name="addCity" value="<%= city != null ? city : "" %>" required></label><br>
    <!-- payment info -->
    <label>Card Number:   <input name="CardNo" value="<%= cardNo != null ? cardNo : "" %>" required></label><br>
    <label>CVV:           <input name="CVV" value="<%= CVV != null ? CVV : "" %>" required></label><br>
    <label>Cardholder:    <input name="CardHolder" value="<%= cardHolder != null ? cardHolder : "" %>" required></label><br>
    <button type="submit">Update Payment Details</button>
</form>
</body>
</html>
