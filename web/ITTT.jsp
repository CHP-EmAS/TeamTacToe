<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Inception Tic Tac Toe</title>
    <link href="https://fonts.googleapis.com/css?family=Open+Sans&display=swap" rel="stylesheet">
    <style>
        <%@ include file="/css/main.css" %>
    </style>
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

    </style>
    <script src="/resources/js/ittt_functions.js?6"></script>
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
    </div>
</div>
</body>
</html>