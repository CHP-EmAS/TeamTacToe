<%--
  Created by IntelliJ IDEA.
  User: z003yj1p
  Date: 05.07.2019
  Time: 12:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="userBean" class="beans.UserBean" scope="request"/>
<html>
<head>
    <title>Tic Tac Toe Login</title>
    <script type="text/javascript" src="resources/js/sha512.js"></script>
    <script>
        function hashSubmit()
        {
            document.getElementById('pw').value = hex_sha512(document.getElementById('nopassword').value);
            document.getElementById('nopassword').value = "#";
            return true;
        }
    </script>
</head>
<body>
    <form action="login" class="form-container" method="post" onsubmit="hashSubmit();">
        <h1>Login</h1>

        <p><jsp:getProperty property="alert" name="userBean"/></p>

        <label for="nickname"><b>Benutzername</b></label>
        <input id="nickname" type="text" value="<jsp:getProperty property="nickname" name="userBean"/>" name="nickname" required/>

        <label for="nopassword"><b>Passwort</b></label>
        <input id="nopassword" type="password" required/>
        <input type="hidden" id="pw" value="" name="pw"/>

       <button type="submit">Login</button>
    </form>
<h1> </h1>

</body>
</html>
