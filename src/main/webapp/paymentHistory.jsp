<%@ page import="uts.isd.model.DAO.DAO" %>
<%@ page import="uts.isd.model.Order" %>
<%@ page import="uts.isd.model.Person.PaymentInformation" %>
<%@ page import="uts.isd.model.Person.Customer" %>
<%@ page import="uts.isd.model.Person.User" %>
<%@ page import="java.util.regex.Pattern" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%

    String ctx = request.getContextPath();
    Customer customer = (Customer) session.getAttribute("loggedInUser");

%>
<!DOCTYPE html>
<html>
<head>
    <title>Payment History</title>
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
<div class="payment-container">
    <%
        for (Order order : customer.getPurchaseHistory()) {
            PaymentInformation paymentInformation = order.getPaymentInformation();
    %>
    <div class="payment-card">
        <p><%=order.getId()%></p>
        <p><%=order.getTotalCost()%></p>
        <p><%=paymentInformation.getCardNo()%></p>
        <p><%=paymentInformation.getCVV()%></p>
        <p><%=paymentInformation.getCardHolder()%></p>
        <p><%=paymentInformation.getExpiryDate()%></p>
    </div>
    <%
        }
    %>
</div>
</body>
</html>
