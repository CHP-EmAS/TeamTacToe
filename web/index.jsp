<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<style>
	<%@ include file="/css/home.css" %>
</style>
<head>
	<meta charset="ISO-8859-1">
	<title>Home</title>
	<link href="https://fonts.googleapis.com/css?family=Open+Sans&display=swap" rel="stylesheet">
	<script src="/resources/js/std_functions.js"></script>
</head>
<body>
<div id="container">
	<div id="d1">
		<h1>Tic Tac Toe</h1>
	</div>
	<div id="d2">
		<div id="d2_1">
			<button onclick="createGame('TTT')">Normal Tic Tac Toe</button>
			<br>
			<button onClick="createGame('STTT')">Super Tic Tac Toe</button>
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
		</div>
	</div>
	<div id="d3">
		<h2>TeamTacToe</h2>	
	</div>
</div>
</body>
</html>