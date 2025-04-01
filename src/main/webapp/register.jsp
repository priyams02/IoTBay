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
      <div class="register-container">
        <h2>Register</h2>
        <form class="register-form" action="RegisterHandler.jsp" method="post">

          <label for="firstName">First Name</label>
          <input class="box" id="firstName" name="firstName" type="text" required />

          <label for="lastName">Last Name</label>
          <input class="box" id="lastName" name="lastName" type="text" required />

          <label for="email">Email</label>
          <input class="box" id="email" name="email" type="email" required />

          <label for="password">Password</label>
          <input class="box" id="password" name="password" type="password" required />

          <label for="tos">
            Agree to our
            <span style="color: dodgerblue; cursor: pointer">Terms of Service</span>
          </label>
          <input type="checkbox" id="tos" name="tos" required />

          <button type="submit">Register</button>
        </form>

        <div class="signup-link">
          Already registered? <a href="login.jsp">Login Here</a>.
        </div>
      </div>

      <div class="footer"></div>
</body>
</html>