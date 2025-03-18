<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Register</title>
    <link rel="stylesheet" href="style.css">
</head>

<body>
  <div class="header">Davey's Record Store</div>
  <div class="content">
    <div class="bg"></div>
    <h1>Create Account</h1>
    <form action="welcome.jsp" method="post">
      <label for="name">Name</label>
      <input id="name" name="name" type="text"/>
      <label for="email">Email</label>
      <input id="email" name="email" type="text"/>
      <label for="password">Password</label>
      <input id="password" name="password" type="password"/>
      <label for="repassword">Re-enter Password</label>
      <input id="repassword" name="repassword" type="password"/>
      <label for="tos">Agree to our <span style="color: dodgerblue; cursor: pointer">Terms of Service</span></label>
      <input type="checkbox" id="tos" name="tos">
      <input type="submit" value="Register">
    </form>
  </div>
  <div class="footer">Stylesheet is not to be used in assessable submissions</div>
</body>
</html>
