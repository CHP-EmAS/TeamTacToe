<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="user" class="javaBeans.UserBean" scope="session"/>

<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Home</title>
	<link href="https://fonts.googleapis.com/css?family=Open+Sans&display=swap" rel="stylesheet">

	<link id="pagestyle" rel="stylesheet" type="text/css" href="css/main.css" title="norm">

	<script type="text/javascript" src="/resources/js/sha512.js"></script>
	<script type="text/javascript" src="/resources/js/home.js"></script>

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
						<input type="hidden" id="pw" value="test" name="pw"/>
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
			<button onclick="createGame('FTTT')">Fancy Tic Tac Toe</button>
			<br>
			<button onclick="joinGame()">Join Game</button>
			<br>
			<button onClick="window.location='leaderboard.jsp'">Leaderboard</button>
			<br>
			<button onClick="window.location='Datenschutz.jsp'">Datenschutz</button>
		</div>
		<div id="d2_2">
			<h2>What's Tic Tac Toe?</h2>
			<p>Tic-tac-toe (American English),
				noughts and crosses (British English),
				or Xs and Os is a paper-and-pencil game for two players,
				X and O, who take turns marking the spaces in a 3×3 grid.
				The player who succeeds in placing three of their marks in a horizontal,
				vertical, or diagonal row wins the game.</p>

		</div>
	</div>
</div>
</body>
</html>