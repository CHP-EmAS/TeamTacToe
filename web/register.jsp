<!Doctype html>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="userBean" class="javaBeans.UserBean" scope="request"/>

<html>
<script>
    function hashSubmit()
    {
        document.getElementById('pw').value = hex_sha512(document.getElementById('tempPassword').value);
        document.getElementById('tempPassword').value = "#";

        document.getElementById('repeatPw').value = hex_sha512(document.getElementById('tempRepeatPassword').value);
        document.getElementById('tempRepeatPassword').value = "#";

        return true;
    }

    function swapStyleSheet(sheet) {
        document.getElementById("pagestyle").setAttribute("href", sheet);
    }

    function initate() {
        var style1 = document.getElementById("stylesheet1");
        var style2 = document.getElementById("stylesheet2");
        var style3 = document.getElementById("stylesheet3");
        style1.onclick = function() {
            swapStyleSheet("Default.css")
        };
        style2.onclick = function() {
            swapStyleSheet("green.css");
        };
        style3.onclick = function() {
            swapStyleSheet("whatever.css");
        };
    }
    window.onload = initate;
</script>
<style>/*Registrationpage*/

.input{
    min-width: 200px;
    min-height: 40px;
    padding: 16px;
    margin-left: px;
    margin-top: 10px;
}</style>
<head>

    <meta name="viewport" content="width=device-width, initial-scale=1.0" charset="UTF-8">
    <link id="pagestyle" rel="stylesheet" type="text/css" href="Default.css" title="norm">

    <title>Registrieren</title>

</head>
<body>
<div id="d1">
    <h1>Registrieren</h1>
</div>
<div id="d2">
    <div id="">
        <form action="register" class="form-container" method="post" onsubmit="hashSubmit();">

            <p><jsp:getProperty property="alert" name="userBean"/></p>

            <label for="nickname"><b>Benutzername</b></label><br>
            <input class="input" id="nickname" type="text" placeholder="Bitte Benutzername eingeben (3-30 Zeichen)" value="<jsp:getProperty property="nickname" name="userBean"/>" name="nickname" required/>
            <br>
            <label for="tempPassword"><b>Passwort</b></label><br>
            <input class="input" size="6" id="tempPassword" placeholder="Bitte Passwort eingeben (min 6 Zeichen)" type="password" required/>
            <br>
            <label for="tempRepeatPassword"><b>Passwort wiederholen</b></label><br>
            <input class="input" size="6" id="tempRepeatPassword" placeholder="Bitte Passwort wiederholen" type="password" required/>
            <br>
            <input type="hidden" id="pw" value="" name="pw"/>
            <input type="hidden" id="repeatPw" value="" name="repeatPw"/>

            <button class="input" type="submit">Registrieren</button>
        </form>
    </div>
</div>
<div id="d3">
    <h2>TeamTacToe</h2>
    <button id="stylesheet1" class="Footer_button">Default</button>
    <button id="stylesheet2" class="Footer_button">Green</button>
    <button id="stylesheet3" class="Footer_button">Whatever</button>

</div>

</body>

</html>