package Games;

import java.io.IOException;
import java.util.Random;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.json.JSONObject;

import Games.instances.*;
import Games.Game.Gamestate;
import Games.attachments.GameTypes;
import Games.attachments.LittleField;
import Games.attachments.Player;

public class InceptionTicTacToe extends Game{
	Player playerOne, playerTwo, currentPlayer;
	NormalGame_Instance startGame;
	SuperGame_Instance actualGame;
	GameTypes currentGameType;
	int lastField;
	String lastWinner;
	int rounds;
	int counter;
	int wonByPlayerOne;
	int wonByPlayerTwo;
	
	public InceptionTicTacToe(String GameID, int rounds){
		super(GameType.Inception_TicTacToe);
		super.gameID = GameID;
		gamestate = Gamestate.WAITING_FOR_PLAYER;
		
		lastField = 0;
		lastWinner = "";
		counter = 0;
		this.rounds = rounds;
		wonByPlayerOne = 0;
		wonByPlayerTwo = 0;
	}
	
	public void FieldClick(Player player, int field) {
		switch(currentGameType) {
		case NORMAL:
			startGame.click(player, field);
			checkStartGame(player, field);
			break;
		case SUPER:
			actualGame.click(player, field);
			checkActualGame(player, field);
			break;
			}
	}
	
	@Override
	public Boolean addPlayer(final Session playerSession)
	{
		String httpSessionID = ((HttpSession)playerSession.getUserProperties().get("sessionID")).getId();

		if(gamestate != Gamestate.WAITING_FOR_PLAYER || isPlayerInGame(httpSessionID)) return false;

		if(playerOne == null){
			playerOne = new Player(playerSession);
			if(playerTwo == null) playerOne.sendInfoMessage("Warte auf Mitspieler!");
		}
		else if(playerTwo == null)
		{
			playerTwo = new Player(playerSession);
			if(playerOne == null) playerTwo.sendInfoMessage("Warte auf Mitspieler!");
		}
		else return false;

		if(getPlayerAmount() == 2)
		{
			if(currentPlayer == null)
			{
				Random generator = new Random();
				if(generator.nextInt(2) == 1) setCurrentPlayer(playerOne);
				else setCurrentPlayer(playerTwo);
			}
			startGame = new NormalGame_Instance();
			currentGameType = GameTypes.NORMAL;
			setPlayers();
			
			updateUserField();
			gamestate = Gamestate.RUNNING;
		}

		return true;
	}
	
	@Override
	public Boolean removePlayer(String httpSessionID)
	{
		if(playerOne.getHttpSessionID().equals(httpSessionID))
		{
			if(playerOne == currentPlayer) currentPlayer = null;
			playerOne = null;

			if(playerTwo != null)  playerTwo.sendInfoMessage("Mitspieler hat das Spiel verlassen!");

			return true;
		}
		else if(playerTwo.getHttpSessionID().equals(httpSessionID))
		{
			if(playerTwo == currentPlayer) currentPlayer = null;
			playerTwo = null;

			if(playerOne != null) playerOne.sendInfoMessage("Mitspieler hat das Spiel verlassen!");

			return true;
		}

		return false;
	}

	@Override
	public Integer getPlayerAmount()
	{
		int pAmount = 0;

		if(playerOne != null) pAmount++;
		if(playerTwo != null) pAmount++;

		return pAmount;
	}

	@Override
	public Boolean isPlayerInGame(String httpSessionID)
	{
		if(playerOne != null) {
			if (httpSessionID.equals(playerOne.getHttpSessionID()))
				return true;
		}

		if(playerTwo != null) {
			if(httpSessionID.equals(playerTwo.getHttpSessionID()))
				return true;
		}

		return false;
	}
	
	@Override
	public void receiveMessage(String cmd, String httpSessionID) {
		if(!isPlayerInGame(httpSessionID)) return;

		Player sender;

		if(playerOne.getHttpSessionID().equals(httpSessionID)) sender = playerOne;
		else sender = playerTwo;

		JSONObject obj = new JSONObject(cmd);

		if(obj.has("cmd"))
		{
			switch(obj.getString("cmd"))
			{
				case "click":
					fieldClick(obj.getInt("fieldNum"), sender);
					break;
				case "reset":
					if(getPlayerAmount() == 2) {
						if(currentPlayer != null) setCurrentPlayer(currentPlayer);
						else setCurrentPlayer(playerOne);
						
						counter = 0;
						currentGameType = GameTypes.NORMAL;
						startGame = new NormalGame_Instance();
						setPlayers();

						updateUserField();
						gamestate = Gamestate.RUNNING;
					}
					break;
			}
		}
	}

	@Override
	public Boolean closeGame()
	{
		try {
			if(playerOne != null)
			{
				playerOne.sendInfoMessage("Spiel wird beendet!");
				playerOne.getSession().close();
				playerOne = null;
			}

			if(playerTwo != null)
			{
				playerTwo.sendInfoMessage("Spiel wird beendet!");
				playerTwo.getSession().close();
				playerTwo = null;
			}

			gameID = "";
			gamestate = Gamestate.CLOSED;

			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Player getPlayer(String httpSessionID)
	{
		if(playerOne.getHttpSessionID().equals(httpSessionID)) return playerOne;
		else if(playerTwo.getHttpSessionID().equals(httpSessionID)) return playerTwo;
		else return errorPlayer;
	}

	private void updateUserField()
	{
		JSONObject json = new JSONObject();

		json.put("cmd", "field");
		json.put("fieldData", getCompleteFieldArray());
		json.put("currentfield", getCurrentField());

		if(playerOne != null) playerOne.sendMessage(json.toString());
		if(playerTwo != null) playerTwo.sendMessage(json.toString());
	}
	
	public int getCompleteResult() {	
		if(counter == rounds) {
			if(wonByPlayerOne>wonByPlayerTwo) {
				return 1;
			}else {
				if(wonByPlayerTwo>wonByPlayerOne) {
					return 2;
				}else {
					if(wonByPlayerTwo==wonByPlayerOne) {
						return 3;
					}else {
						return 0;
					}
				}
			}
		}else {
			return 0;
		}
	}
	
	public int getLittleFieldResult(int number) {
		if(currentGameType == GameTypes.NORMAL) {
			return startGame.getLittleFieldResult(number);
		}else {
			return actualGame.getLittleFieldResult(number);
		}
	}
	
	public int[] getCompleteFieldArray() {
		if(currentGameType == GameTypes.NORMAL) {
			return startGame.getField();
		}else {
			return actualGame.getField();
		}
	}
	
	public int getCurrentField() {
		if(currentGameType == GameTypes.NORMAL) {
			return startGame.getCurrentField();
		}else {
			return actualGame.getCurrentField();
		}
	}
	
	public GameTypes getGameType() {
		return currentGameType;
	}
	
	public boolean setPlayerOne(Player player) {
			playerOne = player;
			switch(currentGameType) {
			case NORMAL:
				startGame.setPlayerOne(playerOne);
				break;
			case SUPER:
				actualGame.setPlayerOne(playerOne);
			}
			return true;
	}
	
	public boolean setPlayerTwo(Player player) {
			playerTwo = player;
			switch(currentGameType) {
			case NORMAL:
				startGame.setPlayerTwo(playerTwo);
				break;
			case SUPER:
				actualGame.setPlayerTwo(playerTwo);
			}
			return true;
	}
	
	public void setCurrentPlayer(Player player) {
		if(player.equals(playerOne)) {
			this.currentPlayer = this.playerOne;
			switch(currentGameType) {
			case NORMAL:
				startGame.setCurrentPlayer(player);
				break;
			case SUPER:
				actualGame.setCurrentPlayer(player);
				break;
			default:
			}
		}
		if(player.equals(playerTwo)) {
			this.currentPlayer = this.playerTwo;
			switch(currentGameType) {
			case NORMAL:
				startGame.setCurrentPlayer(player);
				break;
			case SUPER:
				actualGame.setCurrentPlayer(player);
				break;
			default:
				break;
			}
		}
	}
	
	private void checkStartGame(Player player, int field) {
		if(startGame.getCompleteResult()!=0) {
			switch(startGame.getCompleteResult()) {
			case 1:
				wonByPlayerOne+=1;
				break;
			case 2:
				wonByPlayerTwo+=1;
				break;
			}
			currentGameType = GameTypes.SUPER;
			resetActualGame(player, field, true);
		}else {
			if(currentPlayer.equals(playerOne)) {
				setCurrentPlayer(playerTwo);
			}else {
				setCurrentPlayer(playerOne);
			}
		}
	}
	
	private void checkActualGame(Player player, int field) {
		if(counter<rounds) {
			if(actualGame.getCompleteResult()!=0) {
				resetActualGame(player, field, false);
				counter++;
			}else {
				if(currentPlayer.equals(playerOne)) {
					setCurrentPlayer(playerTwo);
				}else {
					setCurrentPlayer(playerOne);
				}
			}
		}else {
			switch(actualGame.getCompleteResult()) {
			case 0:
				if(currentPlayer.equals(playerOne)) {
					setCurrentPlayer(playerTwo);
				}else {
					setCurrentPlayer(playerOne);
				}
				break;
			case 1:
				wonByPlayerOne+=1;
			case 2:
				wonByPlayerTwo+=1;
			}
		}
		currentPlayer = actualGame.currentPlayer;
	}
	
	private void setPlayers() {
		setPlayerOne(playerOne);
		setPlayerTwo(playerTwo);
	}
	
	private void setLastWonField(Player player, int wonField, SuperGame_Instance game, boolean origin) {
		int chosenField=0;
		if(origin) {
			chosenField=wonField*9;
		}else {
			chosenField=(wonField/9)*9;
		}
		game.setTile(player, chosenField);
		game.setTile(player, chosenField+1);
		game.setTile(player, chosenField+2);
		game.setCurrentPlayer(player);
	}

	private void resetActualGame(Player player, int field, boolean origin) {
		int nextField;
		Random rnd = new Random();
		actualGame = new SuperGame_Instance();
		
		if(player.equals(playerOne)) {
			wonByPlayerOne+=1;
		}else {
			wonByPlayerTwo+=1;
		}
		
		setPlayers();
		setCurrentPlayer(player);
		setLastWonField(player, field, actualGame, origin);
		
		do {
			nextField = rnd.nextInt(9);
		}while(actualGame.getLittleFieldResult(nextField)!=0);
		actualGame.setCurrentField(nextField);
	}
}
