<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<html>
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
		<button id="stylesheet1" class="Footer_button">Default</button>
		<button id="stylesheet2" class="Footer_button">Darkmode</button>
		<button id="stylesheet3" class="Footer_button">Awesome</button>
	</div>
</div>
<script type="text/javascript" src="leaderboardtable.js"></script>
<script>
	function swapStyleSheet(sheet) {
		document.getElementById("pagestyle").setAttribute("href", sheet);
	}
	function initate() {
		var style1 = document.getElementById("stylesheet1");
		var style2 = document.getElementById("stylesheet2");
		var style3 = document.getElementById("stylesheet3");
		style1.onclick = function() {
			swapStyleSheet("css/main.css");
		};
		style2.onclick = function() {
			swapStyleSheet("css/darkmode.css");
		};
		style3.onclick = function() {
			swapStyleSheet("css/main.css");
		};
	}
	window.onload = initate;
</script>
</body>
</html>