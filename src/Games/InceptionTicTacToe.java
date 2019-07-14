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
	GameType currentGameType;
	int lastField;
	String lastWinner;
	int rounds;
	int counter;
	int wonByPlayerOne;
	int wonByPlayerTwo;
	LittleField[] fieldArray;
	LittleField currentField;
	
	public InceptionTicTacToe(String GameID, int rounds){
		super(GameType.Inception_TicTacToe);
		super.gameID = GameID;
		gamestate = Gamestate.WAITING_FOR_PLAYER;
		
		for(int i=0; i<=8; i++) {
			fieldArray[i] = new LittleField();
		}
		currentField = fieldArray[0];
		currentGameType = GameType.TicTacToe;
		
		counter = 0;
		this.rounds = rounds;
		wonByPlayerOne = 0;
		wonByPlayerTwo = 0;
	}
	
	public void FieldClick(Player player, int field) {
		switch(currentGameType) {
		case TicTacToe:
			if(currentField.getTile(field%9).getPlayer()==0) {
				setTile(field%9);
				switchCurrentPlayer(player);
				int result = getCompleteResult();
				if(result!=0) {
					currentField.reset();
					
					if(result==1 || result==2) {
						for(int j=0; j<=8; j++) {
							if(player.equals(playerOne)) {
								fieldArray[field%9].getTile(j).setPlayer(1);
								currentPlayer = playerOne;
							}
							if(player.equals(playerTwo)) {
								fieldArray[field%9].getTile(j).setPlayer(2);
								currentPlayer = playerTwo;
							}
						}
					}
					if(result == 1) {
						wonByPlayerOne++;
					}
					if(result == 2) {
						wonByPlayerTwo++;
					}
					setNextField(field);
					currentGameType = GameType.Super_TicTacToe;
					counter++;
				}
			}
			break;
		case Super_TicTacToe:
			if((field>=0)&&(field<81)&&(player == currentPlayer)){
				if((fieldArray[field/9]==currentField)&&(currentField.getTile(field%9).getPlayer()==0)) {
					setTile(field%9);
					switchCurrentPlayer(player);
					int result = getCompleteResult();
					if(result==0) {
						setNextField(field%9);
					}else {
						if(counter!=rounds) {
							for(int i=0; i<=8; i++) {
								fieldArray[i].reset();
							}
							if(result==1 || result==2) {
								for(int k=0; k<=8; k++) {
									if(result==1) {
										fieldArray[field/9].getTile(k).setPlayer(1);
										currentPlayer = playerOne;
									}
									if(result==2) {
										fieldArray[field/9].getTile(k).setPlayer(2);
										currentPlayer = playerTwo;
									}
								}
								if(result==1) {
									wonByPlayerOne++;
								}
								if(result==2) {
									wonByPlayerTwo++;
								}
							}
							setNextField(field%9);
							counter++;
						}else {
							if(result==1) {
								wonByPlayerOne++;
							}
							if(result==2) {
								wonByPlayerTwo++;
							}
						}
					}
				}
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * Setzt nächstes currentField
	 * @param number Feld das zuletzt gesetzt wurde (0-8)
	 */
	private void setNextField(int number) {
		int nextField;
		Random rnd = new Random();
		if(fieldArray[number].getResult()!=0) {
			do {
				nextField = rnd.nextInt(9);
			}while(fieldArray[nextField].getResult()!=0);
		}else {
			nextField = number;
		}
		currentField = fieldArray[nextField];
	}
	
	private void setTile(int number) {
		if(this.playerOne.equals(this.currentPlayer) && number>=0 && number<9) {
			this.currentField.getTile(number).setPlayer(1);
		}else if(this.playerTwo.equals(this.currentPlayer) && number=>0 && number<9) {
			this.currentField.getTile(number).setPlayer(2);
		}
	}
	
	private void switchCurrentPlayer(Player player) {
		if(player.equals(playerOne)) {
			currentPlayer = playerTwo;
		}
		if(player.equals(playerTwo)) {
			currentPlayer = playerOne;
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
				if(generator.nextInt(2) == 1) currentPlayer = playerOne;
				else currentPlayer=playerTwo;
			}
			
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
						if(currentPlayer != null) {}
						else currentPlayer = playerOne;
						
						//Spiel wird resettet, Rundenanzahl auf 0 gesetzt und neue Startrunnde initialisiert
						counter = 0;
						currentGameType = GameType.TicTacToe;
						for(int i=0; i<=8; i++) {
							for(int j=0; j<=8; j++) {
								fieldArray[i].getTile(j).setPlayer(0);
							}
						}

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
		switch(currentGameType) {
		case TicTacToe:
			return currentField.getResult();
		case Super_TicTacToe:
			//Durch Spalten durchgehen
			for(int i=0; i<3; i++) {
				if((getLittleFieldResult(i) == 1)&&(getLittleFieldResult(i+3) == 1)&&(getLittleFieldResult(i+6) == 1)) {
					return 1;
				}else {
					if((getLittleFieldResult(i) == 2)&&(getLittleFieldResult(i+3) == 2)&&(getLittleFieldResult(i+6) == 2)) {
						return 2;
					}
				}
			}
			//Durch Reihen durchgehen
			for(int i=0; i<=6; i+=3) {
				if((getLittleFieldResult(i) == 1)&&(getLittleFieldResult(i+1) == 1)&&(getLittleFieldResult(i+2) == 1)) {
					return 1;
				}else {
					if((getLittleFieldResult(i) == 2)&&(getLittleFieldResult(i+1) == 2)&&(getLittleFieldResult(i+2) == 2)) {
						return 2;
					}
				}
			}
			//Diagonale oben links nach unten rechts prüfen
			if((getLittleFieldResult(0) == 1)&&(getLittleFieldResult(4) == 1)&&(getLittleFieldResult(8) == 1)) {
				return 1;
			}
			if((getLittleFieldResult(0) == 2)&&(getLittleFieldResult(4) == 2)&&(getLittleFieldResult(8) == 2)) {
				return 2;
			}
			//Diagonale oben rechts nach unten links prüfen
			if((getLittleFieldResult(2) == 1)&&(getLittleFieldResult(4) == 1)&&(getLittleFieldResult(6) == 1)) {
				return 1;
			}
			if((getLittleFieldResult(2) == 2)&&(getLittleFieldResult(4) == 2)&&(getLittleFieldResult(6) == 2)) {
				return 2;
			}
			boolean playable = false;
			for(int i=0; i<=80; i++) {
				if(fieldArray[i/9].getTile(i%9).getPlayer()==0) {
					playable = true;
				}
			}
			if(!playable) {
				return -1;
			}else {
				return 0;
			}
		default:
			return 42;
		}
	}
	
	/**
	 * Nach jeder Spielrunde wird der counter um 1 nach oben gezählt.
	 * Wenn counter == rounds wird das Gesamtergebnis bestimmt.
	 * @return Spielergebnis
	 */
	public int getEndResult() {	
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
			return getCompleteResult();
		}
		if(currentGameType == GameType.Super_TicTacToe) {
			return fieldArray[number].getResult();
		}
		return -1;
	}
	/**
	 * Leitet je nach aktuellem Feldtyp auf Funktionen der Spielinstanzen weiter.
	 * @return Array mit dem Zustand des aktuellen Spielfeldes. int[8] bei TicTacToe, int[81] bei SuperTicTacToe
	 */
	public int[] getCompleteFieldArray() {
		int[] result = new int[81];
		for(int i=0; i<=80; i++) {
			result[i] = fieldArray[(i/9)].getFieldArray()[(i % 9)];
		}
		return result;
	}
	/**
	 * Leitet je nach aktuellem Feldtyp auf Funktionen der Spielinstanzen weiter.
	 * @return aktives Teilfeld; -1 bei Error
	 */
	public int getCurrentField() {
		for(int i=0; i<=8; i++) {
			if(this.fieldArray[i] == this.currentField) {
				return i;
			}
		}
		return 42;
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
}

