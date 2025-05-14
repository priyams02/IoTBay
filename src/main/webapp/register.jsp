<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
  // Pull any validation error & prior values from the request
  String err   = (String) request.getAttribute("err");
  String first = (String) request.getAttribute("First");
  String last  = (String) request.getAttribute("Last");
  String email = (String) request.getAttribute("Email");
  String phone = (String) request.getAttribute("PhoneNumber");
  String num   = (String) request.getAttribute("addNum");
  String street       = (String) request.getAttribute("addStreetName");
  String suburb       = (String) request.getAttribute("addSuburb");
  String postcode     = (String) request.getAttribute("addPostcode");
  String city         = (String) request.getAttribute("addCity");
  String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
  <title>Register</title>
  <link rel="stylesheet" href="<%= ctx %>/styles/IoTBayStyles.css">
</head>
<body>
<div class="IndexDivMain">
  <nav class="navbar">
    <div class="navLinks left">
      <a href="<%= ctx %>/index.jsp">Home</a>
    </div>
  </nav>

  <!-- show error if set -->
  <% if (err != null) { %>
  <p style="text-align:center; color:red"><%= err %></p>
  <% } %>

  <h1>Create Account</h1>
  <form action="<%= ctx %>/RegisterServlet" method="post">
    <input type="text"  name="First"            placeholder="First Name"
           value="<%= first  != null ? first  : "" %>" required>
    <input type="text"  name="Last"             placeholder="Last Name"
           value="<%= last   != null ? last   : "" %>" required>
    <input type="email" name="Email"            placeholder="E-Mail Address"
           value="<%= email  != null ? email  : "" %>" required>
    <input type="text"  name="PhoneNumber"      placeholder="Phone Number"
           value="<%= phone  != null ? phone  : "" %>" required>
    <input type="password" name="Pass1"         placeholder="Password" required>
    <input type="password" name="Pass2"         placeholder="Confirm Password" required>
    <input type="text"  name="addNum"           placeholder="Unit/Street No."
           value="<%= num    != null ? num    : "" %>" required>
    <input type="text"  name="addStreetName"    placeholder="Street"
           value="<%= street != null ? street : "" %>" required>
    <input type="text"  name="addSuburb"        placeholder="Suburb"
           value="<%= suburb != null ? suburb : "" %>" required>
    <input type="text"  name="addPostcode"      placeholder="Postcode"
           value="<%= postcode != null ? postcode : "" %>" required>
    <input type="text"  name="addCity"          placeholder="City"
           value="<%= city   != null ? city   : "" %>" required>

    <label>
      <input type="checkbox" name="tos" required>
      Agree to our <span style="color:dodgerblue; cursor:pointer">Terms of Service</span>
    </label>

    <button type="submit">Register</button>
    <p>Already Registered? <a href="<%= ctx %>/LoginServlet">Login Here</a>.</p>
  </form>
</div>
</body>
</html>
