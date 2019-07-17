<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Tic Tac Toe</title>

    <link href="https://fonts.googleapis.com/css?family=Open+Sans&display=swap" rel="stylesheet">
    <link id="pagestyle" rel="stylesheet" type="text/css" href="css/main.css" title="norm">

    <style>
        <%@ include file="/css/main.css" %>
    </style>
    <style>
        #reset {
            padding: 16px;
            min-height: 40px;
            min-width: 520px;
            text-align: center;
        }
        .fieldButton {
            min-width: 170px;
            min-height: 170px;
            margin-left: 4px;
            margin-top: 2px;
            background: white;
            background-position: center;
            background-repeat: no-repeat;
            background-size: cover;
        }
        #d2 {
            padding-top: 80px;
            padding-left: 80px;
        }
    </style>
    <script src="/resources/js/ttt_functions.js"></script>
</head>
<body>
<div id="container">
    <div id="d1">
        <h1>Tic Tac Toe</h1>
    </div>
    <div id="d2">
        <button class="fieldButton" id="field0" onclick="fieldClick(0)"></button><button class="fieldButton" id="field1" onclick="fieldClick(1)"></button><button class="fieldButton" id="field2" onclick="fieldClick(2)"></button>
        <br>
        <button class="fieldButton" id="field3" onclick="fieldClick(3)"></button><button class="fieldButton" id="field4" onclick="fieldClick(4)"></button><button class="fieldButton" id="field5" onclick="fieldClick(5)"></button>
        <br>
        <button class="fieldButton" id="field6" onclick="fieldClick(6)"></button><button class="fieldButton" id="field7" onclick="fieldClick(7)"></button><button class="fieldButton" id="field8" onclick="fieldClick(8)"></button>
        <h1 id="msgBox"></h1>
        <button id="reset" disabled onclick="restartGame()">Nochmal!</button>
    </div>
    <div id="d3">
        <h2>TeamTacToe</h2>
    </div>
</div>
</body>
<script>

    function loadStyle()
    {
        var style = getCookie("pStyle");

        switch(style)
        {
            case "main":
                document.getElementById("pagestyle").setAttribute("href", "css/main.css");
                break;
            case "dark":
                document.getElementById("pagestyle").setAttribute("href", "css/darkmode.css");
                break;
        }
    }

    function getCookie(cname) {
        var name = cname + "=";
        var decodedCookie = decodeURIComponent(document.cookie);
        var ca = decodedCookie.split(';');
        for(var i = 0; i <ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) === ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) === 0) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    }

    window.onload = loadStyle;
</script>
</html>
