<%--
  Created by IntelliJ IDEA.
  User: z003yj1p
  Date: 24.05.2019
  Time: 11:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Ultimate Tic Tac Toe Lobby</title>

  </head>
  <body>

  <input name="gameID" id="gameID" type="text">
  <button onCLick="startGame('ttt')">Play!</button>


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

</html>
