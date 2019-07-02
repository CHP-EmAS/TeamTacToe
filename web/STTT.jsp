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
        <button class="fieldButton" id="field1" onclick="fieldClick(1)"></button><button class="fieldButton" id="field2" onclick="fieldClick(2)"></button><button class="fieldButton" id="field3" onclick="fieldClick(3)"></button>&nbsp<button class="fieldButton" id="field10" onclick="fieldClick(10)"></button><button class="fieldButton" id="field11" onclick="fieldClick(11)"></button><button class="fieldButton" id="field12" onclick="fieldClick(12)"></button>&nbsp<button class="fieldButton" id="field19" onclick="fieldClick(19)"></button><button class="fieldButton" id="field20" onclick="fieldClick(20)"></button><button class="fieldButton" id="field21" onclick="fieldClick(21)"></button>
        <br>
        <button class="fieldButton" id="field4" onclick="fieldClick(4)"></button><button class="fieldButton" id="field5" onclick="fieldClick(5)"></button><button class="fieldButton" id="field6" onclick="fieldClick(6)"></button>&nbsp<button class="fieldButton" id="field13" onclick="fieldClick(13)"></button><button class="fieldButton" id="field14" onclick="fieldClick(14)"></button><button class="fieldButton" id="field15" onclick="fieldClick(15)"></button>&nbsp<button class="fieldButton" id="field22" onclick="fieldClick(22)"></button><button class="fieldButton" id="field23" onclick="fieldClick(23)"></button><button class="fieldButton" id="field24" onclick="fieldClick(24)"></button>
        <br>
        <button class="fieldButton" id="field7" onclick="fieldClick(7)"></button><button class="fieldButton" id="field8" onclick="fieldClick(8)"></button><button class="fieldButton" id="field9" onclick="fieldClick(9)"></button>&nbsp<button class="fieldButton" id="field16" onclick="fieldClick(16)"></button><button class="fieldButton" id="field17" onclick="fieldClick(17)"></button><button class="fieldButton" id="field18" onclick="fieldClick(18)"></button>&nbsp<button class="fieldButton" id="field25" onclick="fieldClick(25)"></button><button class="fieldButton" id="field26" onclick="fieldClick(26)"></button><button class="fieldButton" id="field27" onclick="fieldClick(27)"></button>
        <br><br>
        <button class="fieldButton" id="field28" onclick="fieldClick(28)"></button><button class="fieldButton" id="field29" onclick="fieldClick(92)"></button><button class="fieldButton" id="field30" onclick="fieldClick(30)"></button>&nbsp<button class="fieldButton" id="field37" onclick="fieldClick(37)"></button><button class="fieldButton" id="field38" onclick="fieldClick(38)"></button><button class="fieldButton" id="field39" onclick="fieldClick(39)"></button>&nbsp<button class="fieldButton" id="field46" onclick="fieldClick(46)"></button><button class="fieldButton" id="field47" onclick="fieldClick(47)"></button><button class="fieldButton" id="field48" onclick="fieldClick(48)"></button>
        <br>
        <button class="fieldButton" id="field31" onclick="fieldClick(31)"></button><button class="fieldButton" id="field32" onclick="fieldClick(32)"></button><button class="fieldButton" id="field33" onclick="fieldClick(33)"></button>&nbsp<button class="fieldButton" id="field40" onclick="fieldClick(40)"></button><button class="fieldButton" id="field41" onclick="fieldClick(41)"></button><button class="fieldButton" id="field42" onclick="fieldClick(42)"></button>&nbsp<button class="fieldButton" id="field49" onclick="fieldClick(49)"></button><button class="fieldButton" id="field50" onclick="fieldClick(50)"></button><button class="fieldButton" id="field51" onclick="fieldClick(51)"></button>
        <br>
        <button class="fieldButton" id="field34" onclick="fieldClick(34)"></button><button class="fieldButton" id="field35" onclick="fieldClick(35)"></button><button class="fieldButton" id="field36" onclick="fieldClick(36)"></button>&nbsp<button class="fieldButton" id="field43" onclick="fieldClick(43)"></button><button class="fieldButton" id="field44" onclick="fieldClick(44)"></button><button class="fieldButton" id="field45" onclick="fieldClick(45)"></button>&nbsp<button class="fieldButton" id="field52" onclick="fieldClick(52)"></button><button class="fieldButton" id="field53" onclick="fieldClick(53)"></button><button class="fieldButton" id="field54" onclick="fieldClick(54)"></button>
        <br><br>
        <button class="fieldButton" id="field55" onclick="fieldClick(55)"></button><button class="fieldButton" id="field56" onclick="fieldClick(56)"></button><button class="fieldButton" id="field57" onclick="fieldClick(57)"></button>&nbsp<button class="fieldButton" id="field64" onclick="fieldClick(64)"></button><button class="fieldButton" id="field65" onclick="fieldClick(65)"></button><button class="fieldButton" id="field66" onclick="fieldClick(66)"></button>&nbsp<button class="fieldButton" id="field73" onclick="fieldClick(73)"></button><button class="fieldButton" id="field74" onclick="fieldClick(74)"></button><button class="fieldButton" id="field75" onclick="fieldClick(75)"></button>
        <br>
        <button class="fieldButton" id="field58" onclick="fieldClick(58)"></button><button class="fieldButton" id="field59" onclick="fieldClick(59)"></button><button class="fieldButton" id="field60" onclick="fieldClick(60)"></button>&nbsp<button class="fieldButton" id="field67" onclick="fieldClick(67)"></button><button class="fieldButton" id="field68" onclick="fieldClick(68)"></button><button class="fieldButton" id="field69" onclick="fieldClick(69)"></button>&nbsp<button class="fieldButton" id="field76" onclick="fieldClick(76)"></button><button class="fieldButton" id="field77" onclick="fieldClick(77)"></button><button class="fieldButton" id="field78" onclick="fieldClick(78)"></button>
        <br>
        <button class="fieldButton" id="field61" onclick="fieldClick(61)"></button><button class="fieldButton" id="field62" onclick="fieldClick(62)"></button><button class="fieldButton" id="field63" onclick="fieldClick(63)"></button>&nbsp<button class="fieldButton" id="field70" onclick="fieldClick(70)"></button><button class="fieldButton" id="field71" onclick="fieldClick(71)"></button><button class="fieldButton" id="field72" onclick="fieldClick(72)"></button>&nbsp<button class="fieldButton" id="field79" onclick="fieldClick(79)"></button><button class="fieldButton" id="field80" onclick="fieldClick(80)"></button><button class="fieldButton" id="field81" onclick="fieldClick(81)"></button>
        <h1 id="msgBox"></h1>
        <button id="reset" disabled onclick="restartGame()">Nochmal!</button>
    </div>
    <div id="d3">
        <h2>TeamTacToe</h2>
    </div>
</div>
</body>
</html>
