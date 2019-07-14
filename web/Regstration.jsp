<!Doctype html>

<html>
<script>
	function swapStyleSheet(sheet) {
		document.getElementById("pagestyle").setAttribute("href", sheet);
	}

	function initate() {
		var style1 = document.getElementById("stylesheet1");
		var style2 = document.getElementById("stylesheet2");
		var style3 = document.getElementById("stylesheet3");

		style1.onclick = function() {
			swapStyleSheet("Default.css")
		};
		style2.onclick = function() {
			swapStyleSheet("green.css");
		};
		style3.onclick = function() {
			swapStyleSheet("whatever.css");
		};
	}
	window.onload = initate;
</script>

<head>

<meta name="viewport" content="width=device-width, initial-scale=1.0"
	charset="UTF-8">

<link id="pagestyle" rel="stylesheet" type="text/css" href="Default.css"
	title="norm">
	
	<title>Registrieren</title>

</head>
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
		<button id="stylesheet2" class="Footer_button">Green</button>
		<button id="stylesheet3" class="Footer_button">Whatever</button>

	</div>

</body>






</html>