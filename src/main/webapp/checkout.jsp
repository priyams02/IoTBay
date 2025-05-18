<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="uts.isd.model.Person.User" %>
<%@ page import="uts.isd.model.Person.PaymentInformation" %>
<%@ page import="uts.isd.model.Person.Address" %>
<%
    String ctx = request.getContextPath();
    User user = (User) session.getAttribute("loggedInUser");
    PaymentInformation paymentInformation = user.getPaymentInfo();
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
<div class="IndexDivMain">
    <nav class="navbar">
        <div class="navLinks left">
            <a href="<%= ctx %>/index.jsp">Home</a>
        </div>
    </nav>
</div>
<%if (user == null || user.getPaymentInfo() == null) {%>
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
<%} else {%>
<div class="payment-card">
    <h1>Payment Details</h1>
    <p>Card Number: <%=paymentInformation.getCardNo()%></p>
    <p>CVV: <%=paymentInformation.getCVV()%></p>
    <p>Card Holder: <%=paymentInformation.getCardHolder()%></p>
    <p>Expiry Date: <%=paymentInformation.getExpiryDate()%></p>
</div>
<form action="<%= ctx %>/editPaymentDetails.jsp">
    <button type="submit">Edit Payment Details</button>
</form>
<form action="<%= ctx %>/DeletePaymentDetails">
    <button type="submit">Delete Payment Details</button>
</form>
<%}%>
</body>
</html>
