<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Register</title>
    <link rel="stylesheet" href="styles/IoTBayStyles.css">
</head>

<body>
<div class="IndexDivMain">
  <!-- Top menu bar thing. -->
  <nav>
    <div>
      <div class="navLinks left"><a href="index.jsp">Home</a></div>
    </div>
  </nav>

  <div class="wrapper ForceCentreScreen">
    <div class="title-text">
      <div class="title login">Register</div>
    </div>

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
    <form action="RegisterHandler.jsp" class = "login" method="post">
      <label for="name">Name</label>
      <input id="name" name="name" type="text"/>
      <label for="email">Email</label>
      <input id="email" name="email" type="text"/>
      <label for="password">Password</label>
      <input id="password" name="password" type="password"/>
      <label for="tos">Agree to our
        <span style="color: dodgerblue; cursor: pointer">Terms of Service</span>
      </label>
      <input type="checkbox" id="tos" name="tos">
      <input type="submit" value="Register">
    </form>
    <div class="signup-link">
      Already Registered? <a href="login.jsp">Login Here</a>.
    </div>
  </div>
  <div class="footer"></div>
</body>
</html>