<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<style>
    <%@ include file="/css/mainGame.css" %>
</style>
<head>
    <meta charset="ISO-8859-1">
    <title>Tic Tac Toe</title>
    <link href="https://fonts.googleapis.com/css?family=Open+Sans&display=swap" rel="stylesheet">
    <script src="/resources/js/ttt_functions.js"></script>
</head>
<body>
<div id="container">
    <div id="d1">
        <h1>Tic Tac Toe</h1>
    </div>
    <div id="d2">
        <button class="fieldButton" id="field1" onclick="fieldClick(1)"></button><button class="fieldButton" id="field2" onclick="fieldClick(2)"></button><button class="fieldButton" id="field3" onclick="fieldClick(3)"></button>
        <br>
        <button class="fieldButton" id="field4" onclick="fieldClick(4)"></button><button class="fieldButton" id="field5" onclick="fieldClick(5)"></button><button class="fieldButton" id="field6" onclick="fieldClick(6)"></button>
        <br>
        <button class="fieldButton" id="field7" onclick="fieldClick(7)"></button><button class="fieldButton" id="field8" onclick="fieldClick(8)"></button><button class="fieldButton" id="field9" onclick="fieldClick(9)"></button>
        <h1 id="msgBox"></h1>
        <button id="reset" disabled onclick="restartGame()">Nochmal!</button>
    </div>
    <div id="d3">
        <h2>TeamTacToe</h2>
    </div>
</div>
</body>
</html>
