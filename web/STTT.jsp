<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Super Tic Tac Toe</title>
	<link href="https://fonts.googleapis.com/css?family=Open+Sans&display=swap" rel="stylesheet">
	<link id="pagestyle" rel="stylesheet" type="text/css" href="css/main.css" title="norm">
	<style>
		#reset {
			padding: 16px;
			min-height: 40px;
			min-width: 700px;
			text-align: center;
		}
		.fieldButton {
			min-width: 60px;
			min-height: 60px;
			margin-left: 4px;
			margin-top: 2px;
			background: white;
			background-position: center;
			background-repeat: no-repeat;
			background-size: cover;
		}
		.subTable {
			padding: 14px;
			border: 1px solid black;
		}
		#d2 {
			padding-top: 80px;
			padding-left: 80px;
		}
		#field9 {
			background: red;
		}
		#subTable_0_2 {
			background: black;
		}
		.buttonGroup_0_2 {
			opacity: 0;
		}
	</style>
	<script src="/resources/js/sttt_functions.js"></script>
</head>
<body>
<div id="container">
	<div id="d1">
		<h1>Tic Tac Toe</h1>
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
</body>
<script type="text/javascript">
	var table = "<table id='bigBoy'>";
	var y_big;
	var x_big;
	var c = 0;
	for(y_big = 0; y_big < 3; y_big++) {
		table += "<tr>"
		for(x_big = 0; x_big < 3; x_big++) {
			table += "<td><table class='subTable' id=subTable_" + y_big + "_" + x_big + ">"
			var y, x;
			for(y = 0; y < 3; y++) {
				table += "<tr>";
				for(x = 0; x < 3; x++) {
					table += "<td>";
					table += "<button class='fieldButton buttonGroup_"+ y_big + "_" + x_big + "' id='field" + c + "' onclick='fieldClick(" + c + ")'>";
					table += "</td>";
					c++;
				}
				table += "</tr>";
			}
			table += "</td></table>";
		}
		table += "</tr>";
	}
	table += "</table>";
	table += "<h1 id='msgBox'></h1>";
	table += "<button id='reset' disabled onclick='restartGame()'>Nochmal!</button>";
	document.getElementById('d2').innerHTML = table;
</script>
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
</html>