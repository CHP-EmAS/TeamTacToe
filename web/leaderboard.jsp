<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<html>
<style>
	<%@ include file="/css/leaderboard.css" %>
</style>
<head>
	<meta charset="ISO-8859-1">
	<title>Leaderboard</title>
	<link href="https://fonts.googleapis.com/css?family=Open+Sans&display=swap" rel="stylesheet">
	<link id="pagestyle" rel="stylesheet" type="text/css" href="css/main.css" title="norm">
	<style>
		table {
			width: 100%;
			padding: 16px;
		}
		tr {
			height: 40px;
			width: 80px;
			padding: 8px;
		}
		tr:first-child {
			font-weight: bold;
		}
	</style>
</head>
<body>
<div id="container">
	<div id="d1">
		<h1>Leaderboard</h1>
	</div>
	<div id="d2">
	</div>
	<div id="d3">
		<h2>TeamTacToe</h2>	
	</div>
</div>
<script type="text/javascript" src="resources/js/leaderboardtable.js"></script>
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