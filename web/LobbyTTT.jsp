<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Lobby</title>
	<link href="https://fonts.googleapis.com/css?family=Open+Sans&display=swap" rel="stylesheet">
	<link id="pagestyle" rel="stylesheet" type="text/css" href="css/main.css" title="norm">
	<style>
		#d2 {
			text-align: center;
			padding-top: 160px;
		}
		#inputStartGame {
			height: 30px;
			width: 300px;
		}
		#buttonStartGame {
			margin-top: 24px;
			height: 40px;
			width: 360px;
		}
	</style>
</head>
<body>
<div id="container">
	<div id="d1">
		<h1>Tic Tac Toe</h1>
	</div>
	<div id="d2">
		<input id="inputStartGame" name="gameID" id="gameID" type="text">
		<br>
 		<button id="buttonStartGame" onCLick="startGame('ttt')">Play!</button>
	</div>
	<div id="d3">
		<h2>TeamTacToe</h2>	
		<button id="stylesheet1" class="Footer_button">Default</button>
		<button id="stylesheet2" class="Footer_button">Darkmode</button>
		<button id="stylesheet3" class="Footer_button">Awesome</button>
	</div>
</div>
</body>
<script>
    function startGame(type)
    {
        switch(type)
        {
            case 'ttt':
                window.location.href = window.location.protocol + "//" + window.location.host + '/TTT/' + document.getElementById('gameID').value;
                break;
            case 'uttt':

                break;
            default:

                break;
        }
    }
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