<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Home</title>
	<link href="https://fonts.googleapis.com/css?family=Open+Sans&display=swap" rel="stylesheet">
	<style>
		<%@ include file="/css/main.css" %>
	</style>
	<style>
		#d1_1 {
			display: inline-block;
		}
		#d1_2 {
			float: right;
			padding-top: 32px;
		}
		#d2_1 {
			display: inline-block;
		}
		#d2_2 {
			width: 60%;
			margin-left: 44px;
			display: inline-block;
			vertical-align: top;
		}
		button {
			min-width: 200px;
			min-height: 60px;
			padding: 16px;
			margin-left: 40px;
			margin-top: 16px;
		}
	</style>
</head>
<body>
<div id="container">
	<div id="d1">
		<div id="d1_1">
			<h1>Tic Tac Toe</h1>
		</div>
		<div id="d1_2">
			<form action="...">
				<input type="text" name="name" value="">
  				<input type="password" name="password" value="">
  				<input type="submit" value="Login">
  			</form>
  			<a href="http://google.com">Noch nicht registriert?</a>
		</div>
	</div>
	<div id="d2">
		<div id="d2_1">
			<button onclick="fieldClick(1)">Tic Tac Toe</button>
			<br>
			<button onclick="fieldClick(1)">Super Tic Tac Toe</button>
			<br>
			<button onclick="fieldClick(1)">Inception Tic Tac Toe</button>
			<br>
			<button onclick="fieldClick(1)">Fancy Tic Tac Toe</button>
			<br>
			<button onclick="fieldClick(1)">Join Game</button>
			<br>
			<button onclick="fieldClick(1)">Leaderboard</button>
			<br>
			<button onclick="fieldClick(1)">About</button>
			</div>
		<div id="d2_2">
			<h2>What's Tic Tac Toe?</h2>
			<p>Tic-tac-toe (American English),
			 noughts and crosses (British English),
			 or Xs and Os is a paper-and-pencil game for two players,
			 X and O, who take turns marking the spaces in a 3×3 grid.
			 The player who succeeds in placing three of their marks in a horizontal,
			vertical, or diagonal row wins the game.</p>
			<p>
			Auf einem quadratischen, 3×3 Felder großen Spielfeld setzen die beiden Spieler abwechselnd ihr Zeichen (ein Spieler Kreuze, der andere Kreise) in ein freies Feld.
			Der Spieler, der als Erster drei Zeichen in eine Zeile, Spalte oder Diagonale setzen kann, gewinnt. Wenn allerdings beide Spieler optimal spielen, kann keiner gewinnen,
			und es kommt zu einem Unentschieden.
			Das heißt, alle neun Felder sind gefüllt, ohne dass ein Spieler die erforderlichen Zeichen in einer Reihe, Spalte oder Diagonalen setzen konnte.
			</p>
			<p>"If you're going to perform inception, you need imagination."
			<br>
			- Christopher Nolan, Inception: The Shooting Script</p>
		</div>
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
	function swapStyleSheet(sheet) {
		document.getElementById("pagestyle").setAttribute("href", sheet);
	}
	function initate() {
		var style1 = document.getElementById("stylesheet1");
		var style2 = document.getElementById("stylesheet2");
		var style3 = document.getElementById("stylesheet3");
		style1.onclick = function() {
			swapStyleSheet("/css/main.css");
		};
		style2.onclick = function() {
			swapStyleSheet("/css/main.css");
		};
		style3.onclick = function() {
			swapStyleSheet("/css/main.css");
		};
	}
	window.onload = initate;
</script>
</html>