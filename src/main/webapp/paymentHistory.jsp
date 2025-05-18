<%@ page import="uts.isd.model.DAO.DAO" %>
<%@ page import="uts.isd.model.Order" %>
<%@ page import="uts.isd.model.Person.PaymentInformation" %>
<%@ page import="uts.isd.model.Person.Customer" %>
<%@ page import="uts.isd.model.Person.User" %>
<%@ page import="java.util.regex.Pattern" %>
<%@ page import="uts.isd.model.OrderLineItem" %>
<%@ page import="uts.isd.model.Product" %>
<%@ page import="uts.isd.model.Person.Address" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%

    String ctx = request.getContextPath();
    Customer customer = (Customer) session.getAttribute("loggedInUser");
    String searchId = request.getParameter("searchId");
    String searchDate = request.getParameter("searchDate");
    ArrayList<Order> purchaseHistory = new ArrayList<>(customer.getPurchaseHistory());
    ArrayList<Order> filteredHistory = new ArrayList<>();

    if ((searchId != null && !searchId.isEmpty()) || (searchDate != null && !searchDate.isEmpty())) {
        for (Order order : purchaseHistory) {
            boolean matchesId = searchId != null && !searchId.isEmpty() &&
                    String.valueOf(order.getId()).contains(searchId);
            boolean matchesDate = searchDate != null && !searchDate.isEmpty() &&
                    order.getPurchaseDate().toString().contains(searchDate);
            if ((matchesId && matchesDate) || (matchesId && searchDate.isEmpty()) || (matchesDate && searchId.isEmpty())) {
                filteredHistory.add(order);
            }
        }
    } else {
        filteredHistory = purchaseHistory;
    }


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
    <form method="GET" action="paymentHistory.jsp">
        <label for="searchId">Search by Payment ID:</label>
        <input type="text" id="searchId" name="searchId" value="<%= searchId != null ? searchId : "" %>">

        <label for="searchDate">Search by Payment Date:</label>
        <input type="date" id="searchDate" name="searchDate" value="<%= searchDate != null ? searchDate : "" %>">

        <button type="submit">Search</button>
        <button type="button" onclick="window.location.href='paymentHistory.jsp';">Clear</button>

    </form>
    <%
        for (Order order : filteredHistory) {
            PaymentInformation paymentInformation = order.getPaymentInformation();
    %>
    <div class="payment-card">
        <p>Payment ID: <%=order.getId()%></p>
        <p>Payment Date: <%=order.getPurchaseDate()%></p>
        <p>Total Cost: <%=order.getTotalCost()%></p>
        <p>Card Number: <%=paymentInformation.getCardNo()%></p>
        <p>CVV: <%=paymentInformation.getCVV()%></p>
        <p>Card Holder: <%=paymentInformation.getCardHolder()%></p>
        <p>Expiry Date: <%=paymentInformation.getExpiryDate()%></p>
    </div>
    <%
        }
    %>
</div>
</body>
</html>
