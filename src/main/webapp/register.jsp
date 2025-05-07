<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Register</title>
    <link rel="stylesheet" href="styles/IoTBayStyles.css">
</head>

<body>
<div class="IndexDivMain">
  <!-- Top menu bar thing. -->
  <nav class="navbar">
    <div>
      <div class="navLinks left"><a href="index.jsp">Home</a></div>
    </div>
  </nav>
    <!-- If the user's passwords didn't match, show the message. -->
    <p style="text-align:center; color:red">
      <%
        String err = request.getParameter("err");
        if (err != null) {
          out.println("<br>" + err);
        }
      %>
    </p>
    <div class="content">
    <div class="bg"></div>
    <h1>Create Account</h1>
      <h2>Register</h2>
      <div class="register-container">
        <div class="form-inner">
          <form action="../Register" class="login" method="POST">

            <div class="field">
              <input type="text" placeholder="First Name" name="First" <%String f = request.getParameter("First");%> required>
            </div>

            <div class="field">
              <input type="text" placeholder="Last Name" name="Last" <%String l = request.getParameter("Last");%> required>
            </div>

            <div class="field">
              <input type="email" placeholder="E-Mail Address" name="Email" <%String e = request.getParameter("Email");%> required>
            </div>

            <div class="field">
              <input type="text" placeholder="Phone Number" name="PhoneNumber" <%String pn = request.getParameter("PhoneNumber");%> required>
            </div>

            <div class="field">
              <input type="password" placeholder="Password" name="Pass1" required>
            </div>

            <div class="field">
              <input type="password" placeholder="Confirm Password" name="Pass2" required>
            </div>

            <div class="field">
              <input type="text" placeHolder="Unit/Street No." name="addNum" <%String n = request.getParameter("addNum");%> required>
            </div>

            <div class="field">
              <input type="text" placeHolder="Street" name="addStreetName" <%String sn = request.getParameter("addStreetName");%> required>
            </div>

            <div class="field">
              <input type="text" placeHolder="Suburb" name="addSuburb" <%String s = request.getParameter("addSuburb");%> required>
            </div>

            <div class="field">
              <input type="text" placeHolder="Postcode" name="addPostcode" <%String p = request.getParameter("addPostcode");%> required>
            </div>

            <div class="field">
              <input type="text" placeHolder="City" name="addCity" <%String c = request.getParameter("addCity");%> >
            </div>
            <label for="tos">
              Agree to our
              <span style="color: dodgerblue; cursor: pointer">Terms of Service</span>
            </label>
            <input type="checkbox" id="tos" name="tos" required />

            <div class="field">
              <input type="submit" value="Register">
            </div>

            <div class="signup-link">
              Already Registered? <a href="Login.jsp">Login Here</a>.
            </div>

          </form>
      <div class="footer"></div>
</body>
</html>