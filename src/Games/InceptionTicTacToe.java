package Games;

import java.io.IOException;
import java.util.Random;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.json.JSONObject;

import Games.instances.*;
import Games.Game.Gamestate;
import Games.Game.GameType;
import Games.attachments.LittleField;
import Games.attachments.Player;

public class InceptionTicTacToe extends Game{
	Player playerOne, playerTwo, currentPlayer;
	NormalGame_Instance startGame;
	SuperGame_Instance actualGame;
	GameType currentGameType;
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
	
	public void fieldClick(Player player, int field) {
		switch(currentGameType) {
		case TicTacToe:
			startGame.click(player, field);
			checkStartGame(player, field);
			break;
		case Super_TicTacToe:
			actualGame.click(player, field);
			checkActualGame(player, field);
			break;
		default:
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
			//Sobald beide Spieler vorhanden wird Startrunde initialisiert
			startGame = new NormalGame_Instance();
			currentGameType = GameType.TicTacToe;
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
					fieldClick(sender, obj.getInt("fieldNum"));
					break;
				case "reset":
					if(getPlayerAmount() == 2) {
						if(currentPlayer != null) setCurrentPlayer(currentPlayer);
						else setCurrentPlayer(playerOne);
						
						//Spiel wird resettet, Rundenanzahl auf 0 gesetzt und neue Startrunnde initialisiert
						counter = 0;
						currentGameType = GameType.TicTacToe;
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
	
	/**
	 * Nach jeder Spielrunde wird der counter um 1 nach oben gezählt.
	 * Wenn counter == rounds wird das Gesamtergebnis bestimmt.
	 * @return Spielergebnis
	 */
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
	
	/**
	 * Leitet je nach aktuellem Feldtyp auf Funktionen der Spielinstanzen weiter.
	 * @param number Nummer des Feldes (0-8)
	 * @return 0, 1 oder 2. Welcher Spieler dieses Unterfeld gewonnen hat.
	 */
	public int getLittleFieldResult(int number) {
		if(currentGameType == GameType.TicTacToe) {
			return startGame.getLittleFieldResult(number);
		}
		if(currentGameType == GameType.Super_TicTacToe) {
			return actualGame.getLittleFieldResult(number);
		}
		return -1;
	}
	/**
	 * Leitet je nach aktuellem Feldtyp auf Funktionen der Spielinstanzen weiter.
	 * @return Array mit dem Zustand des aktuellen Spielfeldes. int[8] bei TicTacToe, int[81] bei SuperTicTacToe
	 */
	public int[] getCompleteFieldArray() {
		if(currentGameType == GameType.TicTacToe) {
			return startGame.getField();
		}
		if(currentGameType == GameType.Super_TicTacToe) {
			return actualGame.getField();
		}
		int[] error = new int[1];
		return error;
	}
	/**
	 * Leitet je nach aktuellem Feldtyp auf Funktionen der Spielinstanzen weiter.
	 * @return aktives Teilfeld; -1 bei Error
	 */
	public int getCurrentField() {
		if(currentGameType == GameType.TicTacToe) {
			return startGame.getCurrentField();
		}
		if(currentGameType == GameType.Super_TicTacToe){
			return actualGame.getCurrentField();
		}
		return -1;
	}
	/**
	 * @return GameType.Inception_TicTacToe
	 */
	public GameType getGameType() {
		return GameType.Inception_TicTacToe;
	}
	/**
	 * Gibt Spieltyp des aktuellen Spielfeldes zurück (Normal oder Super)
	 * @return currentGameType of field
	 */
	public GameType getGameTypeOfField() {
		return currentGameType;
	}
	/**
	 * Setzt Spieler 1 für aktuelle Spielinstanz. 
	 * Wird bei neuer Instanzierung(Start, Reset oder neue Runde) in Funktion setPlayers() aufgerufen.
	 * Um Spieler dem gesamtem Spiel hinzuzufügen wird addPlayer verwendet.
	 * @return boolean for success
	 */
	private boolean setPlayerOne() {
			switch(currentGameType) {
			case TicTacToe:
				startGame.setPlayerOne(this.playerOne);
				return true;
			case Super_TicTacToe:
				actualGame.setPlayerOne(this.playerOne);
				return true;
			default:
				return false;
			}
	}
	/**
	 * Setzt Spieler 2 für aktuelle Spielinstanz. 
	 * Wird bei neuer Instanzierung(Start, Reset oder neue Runde) in Funktion setPlayers() aufgerufen.
	 * Um Spieler dem gesamtem Spiel hinzuzufügen wird addPlayer verwendet.
	 * @return boolean for success
	 */
	private boolean setPlayerTwo() {
			switch(currentGameType) {
			case TicTacToe:
				startGame.setPlayerTwo(this.playerTwo);
				return true;
			case Super_TicTacToe:
				actualGame.setPlayerTwo(this.playerTwo);
				return true;
			default:
				return false;
			}
	}
	/**
	 * Prüft ob übergebener Spieler schon als PlayerOne oder PlayerTwo gesetzt wurde.
	 * Setzt dann CurrentPlayer sowohl für gesamtes Spiel als auch für Spielinstanz.
	 * @param player Player-Object
	 */
	public void setCurrentPlayer(Player player) {
		if(player.equals(playerOne)) {
			this.currentPlayer = this.playerOne;
			switch(currentGameType) {
			case TicTacToe:
				startGame.setCurrentPlayer(player);
				break;
			case Super_TicTacToe:
				actualGame.setCurrentPlayer(player);
				break;
			default:
			}
		}
		if(player.equals(playerTwo)) {
			this.currentPlayer = this.playerTwo;
			switch(currentGameType) {
			case TicTacToe:
				startGame.setCurrentPlayer(player);
				break;
			case Super_TicTacToe:
				actualGame.setCurrentPlayer(player);
				break;
			default:
				break;
			}
		}
	}
	/**
	 * Prüft Status des Anfangsspiels mit kleinem TicTacToe Feld.
	 * Ist dieses abgeschlossen wird die nächste Runde initialisiert mit einem SuperTicTacToe Feld.
	 * Zähler für Ergebniscount wird hochgezählt. Muss auch übergeben werden auf welchem Feld der letzte Zug war.
	 * @param player Player-Object des Siegers
	 * @param field Mit welchem Feld wurde abgeschlossen. 
	 */
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
			currentGameType = GameType.Super_TicTacToe;
			resetActualGame(player, field, true);
		}else {
			if(currentPlayer.equals(playerOne)) {
				setCurrentPlayer(playerTwo);
			}else {
				setCurrentPlayer(playerOne);
			}
		}
	}
	/**
	 * Prüft ob Status der SuperTicTacToe Instanz.
	 * Wenn alle Runden beendet wird keine neue Spielinstanz erzeugt
	 * @param player Spieler der zuletzt gezogen hat
	 * @param field Zuletzt gesetztes Feld
	 */
	private void checkActualGame(Player player, int field) {
		if(counter<rounds) {
			if(actualGame.getCompleteResult()!=0) {
				//neue Instanz für nächtse Runde
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
			//Wird keine neue Spielinstanz erzeugt
			case 1:
				wonByPlayerOne+=1;
			case 2:
				wonByPlayerTwo+=1;
			}
		}
		currentPlayer = actualGame.currentPlayer;
	}
	/**
	 * Setzt PlayerOne und PlayerTwo für Spielinstanz.
	 */
	private void setPlayers() {
		setPlayerOne();
		setPlayerTwo();
	}
	/**
	 * Setzt nach neuer Instanzierung das bereits verdiente Feld für Sieger der vorherigen Runde
	 * @param player last Winner
	 * @param wonField Nummer des zuletzt gesetzten Feldes (0-8 nach Startrunde, sonst 0-80)
	 * @param game neue Spielinstanz
	 * @param origin true wenn vorherige Instanz vom Typ TicTacToe
	 * 				 false wenn vorherige Instanz vom Typ SuperTicTacToe
	 */
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
	
	/**
	 * Beginnt neue Spielrunde mit SuperTicTacToe Feld.
	 * origin nur zum weiterleiten an setLastWonField
	 * @param player last Winner
	 * @param field neue Spielinstanz
	 * @param origin true wenn vorherige Instanz vom Typ TicTacToe
	 * 				 false wenn vorherige Instanz vom Typ SuperTicTacToe
	 */
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
