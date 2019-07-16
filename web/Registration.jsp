<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!Doctype html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Registrieren</title>
	<link href="https://fonts.googleapis.com/css?family=Open+Sans&display=swap" rel="stylesheet">
	<link id="pagestyle" rel="stylesheet" type="text/css" href="css/main.css" title="norm">
	<style>
		.input{
			min-width: 200px;
			min-height: 40px;
			padding: 16px;
			margin-left: px;
			margin-top: 10px;
		}
	</style>
<body>
	<div id="d1">
		<h1>Registrieren</h1>
	</div>
	<div id="d2">
		<p>Benutzernamen/Passwort eingeben:</p>
		<div id="">
			<form action="" method="post">
				<input class="input" type="text" placeholder="Registriere neuen Benutzer" autofocus></input>
			</form>

			<form action="" method="post">
				<input class="input" type="password" value=""></input>
			</form>
			<form action="" method="post">
				<button class="input" type="submit">Registrieren</button>
			</form>
		</div>
	</div>
	<div id="d3">
		<h2>TeamTacToe</h2>
		<button id="stylesheet1" class="Footer_button">Default</button>
		<button id="stylesheet2" class="Footer_button">Darkmode</button>
		<button id="stylesheet3" class="Footer_button">Awesome</button>
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