<%--
  Created by IntelliJ IDEA.
  User: z003yj1p
  Date: 04.06.2019
  Time: 11:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Ultimate Tic Tac Toe Game</title>
</head>

<?php
    $db = new mysqli('h2839726.stratoserver.net', 'admin', 'tictactoe', 'xxxULTIMATE_DATABASExxx');
?>

<body>
    <button id="field1" onclick="fieldClick(1)">1</button><button id="field2" onclick="fieldClick(2)">2</button><button id="field3" onclick="fieldClick(3)">3</button><br>
    <button id="field4" onclick="fieldClick(4)">4</button><button id="field5" onclick="fieldClick(5)">5</button><button id="field6" onclick="fieldClick(6)">6</button><br>
    <button id="field7" onclick="fieldClick(7)">7</button><button id="field8" onclick="fieldClick(8)">8</button><button id="field9" onclick="fieldClick(9)">9</button>
    <h1 id="msgBox"></h1>
    <button id="reset" disabled onclick="restartGame()">Nochmal!</button>
</body>

<script src="../ttt_functions.js"></script>

</html>
