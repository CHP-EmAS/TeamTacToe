<%--
  Created by IntelliJ IDEA.
  User: z003yj1p
  Date: 05.07.2019
  Time: 12:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Tic Tac Toe Login</title>
</head>
<body>
<jsp:useBean id="loginBean" class="Beans.LoginRegisterBean" scope="request"/>

<p><jsp:getProperty property="alert" name="loginBean"/></p>
<div class="form-popup" id="loginForm">
    <form action="login" class="form-container" method="post">
        <h1>Login</h1>

        <label for="nickname"><b>Benutzername</b></label>
        <input id="nickname" type="text" value="<jsp:getProperty property="nickname" name="loginBean"/>" name="nickname" required>

        <label for="passwd"><b>Passwort</b></label>
        <input id="passwd" type="password" name="passwd" required>

        <button type="submit" class="btn">Login</button>
    </form>
</div>
</body>
</html>
