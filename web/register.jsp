<%--
  Created by IntelliJ IDEA.
  User: z003yj1p
  Date: 05.07.2019
  Time: 12:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="userBean" class="javaBeans.UserBean" scope="request"/>
<html>
<head>
    <title>Tic Tac Toe Register</title>
    <script type="text/javascript" src="resources/js/sha512.js"></script>
    <script>
        function hashSubmit()
        {
            document.getElementById('pw').value = hex_sha512(document.getElementById('tempPassword').value);
            document.getElementById('tempPassword').value = "#";

            document.getElementById('repeatPw').value = hex_sha512(document.getElementById('tempRepeatPassword').value);
            document.getElementById('tempRepeatPassword').value = "#";

            return true;
        }
    </script>
</head>
<body>
    <form action="register" class="form-container" method="post" onsubmit="hashSubmit();">
        <h1>Registrieren</h1>

        <p><jsp:getProperty property="alert" name="userBean"/></p>

        <label for="nickname"><b>Benutzername</b></label><br>
        <input id="nickname" type="text" placeholder="Bitte Benutzername eingeben (3-30 Zeichen)" value="<jsp:getProperty property="nickname" name="userBean"/>" name="nickname" required/>
        <br>
        <label for="tempPassword"><b>Passwort</b></label><br>
        <input size="6" id="tempPassword" placeholder="Bitte Passwort eingeben (min 6 Zeichen)" type="password" required/>
        <br>
        <label for="tempRepeatPassword"><b>Passwort wiederholen</b></label><br>
        <input size="6" id="tempRepeatPassword" placeholder="Bitte Passwort wiederholen" type="password" required/>
        <br>
        <input type="hidden" id="pw" value="" name="pw"/>
        <input type="hidden" id="repeatPw" value="" name="repeatPw"/>

       <button type="submit">Registrieren</button><button type="button">Zum Login</button>
    </form>
<h1> </h1>

</body>
</html>
