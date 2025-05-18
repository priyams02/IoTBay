<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="uts.isd.model.Person.User, uts.isd.model.Person.User.UserType, uts.isd.model.Person.Customer, uts.isd.model.Person.Staff, uts.isd.model.Person.Address, uts.isd.model.Person.PaymentInformation" %>
<%
    String ctx = request.getContextPath();
    User active = (User) session.getAttribute("loggedInUser");
    if (active == null) {
        response.sendRedirect(ctx + "/LoginServlet");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Your Profile</title>
    <link rel="stylesheet" href="<%= ctx %>/styles/IoTBayStyles.css">
</head>
<body>
<h1>Update your Profile</h1>

<% if (active.getType() == UserType.CUSTOMER) {
    Customer cust = (Customer) active;
    Address addr = cust.getAddress();
    PaymentInformation pi = cust.getPayment();
%>
<section>
    <h2>Personal Information</h2>
    <form action="<%= ctx %>/UpdateProfile" method="post">
        <input type="hidden" name="Attribute" value="Names">
        <input type="hidden" name="bIsCustomer" value="yes">
        <label>First Name:<br>
            <input type="text" name="First" value="<%= cust.getFirstName() %>" required>
        </label><br>
        <label>Last Name:<br>
            <input type="text" name="Last" value="<%= cust.getLastName() %>" required>
        </label><br>
        <button type="submit">Update Names</button>
    </form>
</section>

<section>
    <h2>Contact Details</h2>
    <form action="<%= ctx %>/UpdateProfile" method="post">
        <input type="hidden" name="Attribute" value="Email">
        <input type="hidden" name="bIsCustomer" value="yes">
        <label>Email Address:<br>
            <input type="email" name="Email" value="<%= cust.getEmail() %>" required>
        </label><br>
        <button type="submit">Update Email</button>
    </form>
    <section>
        <h2>Change Password</h2>
        <form action="<%= ctx %>/UpdateProfile" method="post">
            <input type="hidden" name="Attribute"   value="Password">
            <input type="hidden" name="bIsCustomer" value="yes">

            <label>
                Current Password:
                <input type="password" name="Password" required>
                <input type="hidden" name="bIsCustomer" value="yes">
            </label><br>

            <label>
                New Password:
                <input type="password" name="Pass1" required>
                <input type="hidden" name="bIsCustomer" value="yes">
            </label><br>

            <label>
                Confirm Password:
                <input type="password" name="Pass2" required>
            </label><br>

            <button type="submit">Update Password</button>
        </form>
    </section>
    <form action="<%= ctx %>/UpdateProfile" method="post">
        <input type="hidden" name="Attribute" value="PhoneNumber">
        <input type="hidden" name="bIsCustomer" value="yes">
        <label>Phone Number:<br>
            <input type="tel" name="PhoneNumber" value="<%= cust.getPhoneNumber() %>">
        </label><br>
        <button type="submit">Update Phone</button>
    </form>
</section>

<section>
    <h2>Address</h2>
    <form action="<%= ctx %>/UpdateProfile" method="post">
        <input type="hidden" name="Attribute" value="Address">
        <input type="hidden" name="bIsCustomer" value="yes">
        <label>Street Number:<br>
            <input type="number" name="addNum" value="<%= addr != null ? addr.getNumber() : "" %>">
        </label><br>
        <label>Street Name:<br>
            <input type="text" name="addStreetName" value="<%= addr != null ? addr.getStreetName() : "" %>">
        </label><br>
        <label>Suburb:<br>
            <input type="text" name="addSuburb" value="<%= addr != null ? addr.getSuburb() : "" %>">
        </label><br>
        <label>Postcode:<br>
            <input type="number" name="addPostcode" value="<%= addr != null ? addr.getPostcode() : "" %>">
        </label><br>
        <label>City:<br>
            <input type="text" name="addCity" value="<%= addr != null ? addr.getCity() : "" %>">
        </label><br>
        <button type="submit">Update Address</button>
    </form>
</section>

<section>
    <h2>Payment Information</h2>
    <form action="<%= ctx %>/UpdateProfile" method="post">
        <input type="hidden" name="Attribute" value="PaymentInformation">
        <input type="hidden" name="bIsCustomer" value="yes">
        <label>Card Number:<br>
            <input type="text" name="CardNo" value="<%= pi != null ? pi.getCardNo() : "" %>">
        </label><br>
        <label>CVV:<br>
            <input type="text" name="CVV" value="<%= pi != null ? pi.getCVV() : "" %>">
        </label><br>
        <label>Card Holder:<br>
            <input type="text" name="CardHolder" value="<%= pi != null ? pi.getCardHolder() : "" %>">
        </label><br>
        <button type="submit">Update Payment</button>
    </form>
</section>
<% } else {
    Staff st = (Staff) active;
%>
<section>
    <h2>Staff Profile</h2>
    <form action="<%= ctx %>/UpdateProfile" method="post">
        <input type="hidden" name="Attribute" value="Names">
        <input type="hidden" name="bIsCustomer" value="no">
        <label>First Name:<br>
            <input type="text" name="First" value="<%= st.getFirstName() %>">
        </label><br>
        <label>Last Name:<br>
            <input type="text" name="Last" value="<%= st.getLastName() %>">
        </label><br>
        <button type="submit">Update Names</button>
    </form>
</section>
<% } %>

<section>
    <form action="<%= ctx %>/DeleteServlet" method="post">
        <button type="submit">Deactivate Account</button>
    </form>
</section>

<p><a href="<%= ctx %>/index.jsp">&larr; Back Home</a></p>
</body>
</html>
