<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="user" class="javaBeans.UserBean" scope="session"/>

<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Home</title>
	<link href="https://fonts.googleapis.com/css?family=Open+Sans&display=swap" rel="stylesheet">
	<script type="text/javascript" src="resources/js/sha512.js"></script>
	<script src="/resources/js/home.js?6"></script>
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
			<h1>Tic Tac Toe </h1>
		</div>
		<div id="d1_2">
			<c:choose>
				<c:when test="${user.loggedIn}">
					<h1><jsp:getProperty property="nickname" name="user"/></h1> <button><a href="logout">Log Out!</a></button>
				</c:when>
				<c:otherwise>
					<form action="login" class="form-container" method="post" onsubmit="hashSubmit();">
						<jsp:getProperty property="alert" name="user"/>
						<input placeholder="Benutzername" id="nickname" type="text" name="nickname" required/>
						<input placeholder="Passwort" id="nopassword" type="password" required/>
						<input type="hidden" id="pw" value="" name="pw"/>
						<input type="submit" value="Login">
					</form>
					<a href="register">Noch nicht registriert?</a>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<div id="d2">
		<div id="d2_1">
			<button onclick="createGame('TTT')">Tic Tac Toe</button>
			<br>
			<button onclick="createGame('STTT')">Super Tic Tac Toe</button>
			<br>
			<button onclick="createGame('ITTT')">Inception Tic Tac Toe</button>
			<br>
			<button>Fancy Tic Tac Toe</button>
			<br>
			<button onclick="joinGame()">Join Game</button>
			<br>
			<button onClick="window.location='leaderboard.jsp'">Leaderboard</button>
			<br>
			<button>About</button>
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
	</div>
</div>
</body>
</html>