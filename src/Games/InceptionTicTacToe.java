package Games;

import java.io.IOException;
import java.util.Random;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.json.JSONObject;

import Games.attachments.LittleField;
import Games.attachments.Player;

public class InceptionTicTacToe extends Game{
	private Player playerOne, playerTwo, currentPlayer;
	private GameType currentGameType;

	private int lastField;
	private String lastWinner;

	private int rounds;
	private int counter;

	private int wonByPlayerOne;
	private int wonByPlayerTwo;

	private LittleField[] fieldArray;
	private LittleField currentField;
	
	public InceptionTicTacToe(String GameID){
		super(GameType.Inception_TicTacToe);
		super.gameID = GameID;
		this.gamestate = Gamestate.WAITING_FOR_PLAYER;

		this.fieldArray = new LittleField[9];
		for(int i = 0; i<9; i++) fieldArray[i] = new LittleField();

		currentField = fieldArray[0];

		this.counter = 0;
		this.rounds = 5;
		this.wonByPlayerOne = 0;
		this.wonByPlayerTwo = 0;

		currentGameType = GameType.TicTacToe;

		currentPlayer = null;
	}
	
	public void fieldClick(Player player, int field)
	{
		if(gamestate == Gamestate.RUNNING) {
			if (playerOne != null && playerTwo != null) {
				if (player == currentPlayer) {

					Random rnd = new Random();

					switch(currentGameType) {
						case TicTacToe:
							if (field >= 0 && field < 9) {
								if(currentField.getTile(field).getPlayer()==0)
								{
									if (player.equals(playerOne))  fieldArray[0].getTile(field).setPlayer(1);
									else if (player.equals(playerTwo))  fieldArray[0].getTile(field).setPlayer(2);

									int gameResult = fieldArray[0].getResult();

									switch (gameResult) {
										case -1:
											playerOne.sendMessage("{\"cmd\":\"changeToBig\"}");
											playerTwo.sendMessage("{\"cmd\":\"changeToBig\"}");
											playerOne.sendMessage("{\"cmd\":\"alert\",\"msg\":\"Ab in die " + (counter+2) + ". Stufe!\"}");
											playerTwo.sendMessage("{\"cmd\":\"alert\",\"msg\":\"Ab in die " + (counter+2) + ". Stufe!\"}");

											if(player.equals(playerOne)) setCurrentPlayer(playerTwo);
											else setCurrentPlayer(playerOne);

											for(int f = 0; f < 9; f++)
											{
												fieldArray[f].reset();
											}

											currentGameType = GameType.Super_TicTacToe;
											counter++;
											break;
										case 0:
											if(player.equals(playerOne)) setCurrentPlayer(playerTwo);
											else setCurrentPlayer(playerOne);
											break;
										case 1:
											playerOne.sendMessage("{\"cmd\":\"changeToBig\"}");
											playerTwo.sendMessage("{\"cmd\":\"changeToBig\"}");
											playerOne.sendMessage("{\"cmd\":\"alert\",\"msg\":\"Ab in die " + (counter+2) + ". Stufe! Du hast die vorherige Stufe gewonnen!\"}");
											playerTwo.sendMessage("{\"cmd\":\"alert\",\"msg\":\"Ab in die " + (counter+2) + ". Stufe!\"}");

											setCurrentPlayer(playerOne);

											int wF = field%9;

											fieldArray[wF] = fieldArray[0];
											if(wF != 0) fieldArray[0] = new LittleField();

											for(int f = 0; f < 9; f++)
											{
												if(f != wF) fieldArray[f].reset();
											}

											setNextField(wF);

											currentGameType = GameType.Super_TicTacToe;
											counter++;
											break;
										case 2:
											playerOne.sendMessage("{\"cmd\":\"changeToBig\"}");
											playerTwo.sendMessage("{\"cmd\":\"changeToBig\"}");
											playerTwo.sendMessage("{\"cmd\":\"alert\",\"msg\":\"Ab in die " + (counter+2) + ". Stufe! Du hast die vorherige Stufe gewonnen!\"}");
											playerOne.sendMessage("{\"cmd\":\"alert\",\"msg\":\"Ab in die " + (counter+2) + ". Stufe!\"}");

											setCurrentPlayer(playerTwo);

											int wF2 = field%9;

											fieldArray[wF2] = fieldArray[0];
											if(wF2 != 0) fieldArray[0] = new LittleField();

											for(int f = 0; f < 9; f++)
											{
												if(f != wF2) fieldArray[f].reset();
											}

											setNextField(wF2);

											currentGameType = GameType.Super_TicTacToe;
											counter++;
											break;
									}

									if(gameResult == 1) wonByPlayerOne++;
									else if(gameResult == 2) wonByPlayerTwo++;

									updateUserField();

								} else player.sendInfoMessage("Ungültiger Zug!");
							} else System.out.println("Fehler beim Klicken eines Feldes! Field <" + field + "> out of range");
							break;
						case Super_TicTacToe:
							if (field >= 0 && field < 81) {
								if (fieldArray[(field)/9]==currentField && currentField.getTile((field)%9).getPlayer()==0) {
									if (player.equals(playerOne))  currentField.getTile(field%9).setPlayer(1);
									else if (player.equals(playerTwo))  currentField.getTile(field%9).setPlayer(2);

									int gameResult = getCompleteResult();

									switch (gameResult)
									{
										case -1:
											playerTwo.sendMessage("{\"cmd\":\"alert\",\"msg\":\"Ab in die " + (counter+2) + ". Stufe!\"}");
											playerOne.sendMessage("{\"cmd\":\"alert\",\"msg\":\"Ab in die " + (counter+2) + ". Stufe!\"}");

											if(player.equals(playerOne)) setCurrentPlayer(playerTwo);
											else setCurrentPlayer(playerOne);

											for(int f = 0; f < 9; f++)
											{
												fieldArray[f].reset();
											}

											setNextField(rnd.nextInt(9));

											counter++;
											break;
										case 0:
											if(player.equals(playerOne)) setCurrentPlayer(playerTwo);
											else setCurrentPlayer(playerOne);

											setNextField(field%9);
											break;
										case 1:
											playerOne.sendMessage("{\"cmd\":\"alert\",\"msg\":\"Ab in die " + (counter+2) + ". Stufe! Du hast die vorherige Stufe gewonnen!\"}");
											playerTwo.sendMessage("{\"cmd\":\"alert\",\"msg\":\"Ab in die " + (counter+2) + ". Stufe!\"}");

											setCurrentPlayer(playerOne);

											int wF = field/9;

                                            LittleField newField = new LittleField();

											for(int t = 0; t < 9; t++)
											{
												int tilePlayer = getLittleFieldResult(t);
												if(tilePlayer != 1 && tilePlayer != 2) tilePlayer = 0;
                                                newField.getTile(t).setPlayer(tilePlayer);
											}

                                            for(int f = 0; f < 9; f++)
                                            {
                                                fieldArray[f].reset();
                                            }

                                            fieldArray[wF] = newField;

											setNextField(wF);

											counter++;
											break;
										case 2:
											playerTwo.sendMessage("{\"cmd\":\"alert\",\"msg\":\"Ab in die " + (counter+2) + ". Stufe! Du hast die vorherige Stufe gewonnen!\"}");
											playerOne.sendMessage("{\"cmd\":\"alert\",\"msg\":\"Ab in die " + (counter+2) + ". Stufe!\"}");

											setCurrentPlayer(playerTwo);

											int wF2 = field/9;

                                            LittleField newField2 = new LittleField();

                                            for(int t = 0; t < 9; t++)
                                            {
                                                int tilePlayer = getLittleFieldResult(t);
                                                if(tilePlayer != 1 && tilePlayer != 2) tilePlayer = 0;
                                                newField2.getTile(t).setPlayer(tilePlayer);
                                            }

                                            for(int f = 0; f < 9; f++)
                                            {
                                                fieldArray[f].reset();
                                            }

                                            fieldArray[wF2] = newField2;

											setNextField(wF2);

											counter++;
											break;
									}

									updateUserField();

								} else player.sendInfoMessage("Ungültiger Zug!");
							} else System.out.println("Fehler beim Klicken eines Feldes! Field <" + field + "> out of range");
							break;
					}
				} else player.sendInfoMessage("Dein Mitspieler ist am Zug!");
			} else player.sendInfoMessage("Ungültiger Zug! Sie brauchen einen Mitspieler!");
		}
	}

	private void setCurrentPlayer(Player player) {
		if(playerOne != null && playerTwo != null) {
			if (player.equals(playerOne)) {
				currentPlayer = playerOne;
				playerOne.sendInfoMessage("Du bist dran!");
				playerTwo.sendInfoMessage("Mitspieler wählt ein Feld!");
				return;
			}

			if (player.equals(playerTwo)) {
				currentPlayer = playerTwo;
				playerTwo.sendInfoMessage("Du bist dran!");
				playerOne.sendInfoMessage("Mitspieler wählt ein Feld!");
				return;
			}

			System.out.println("ERROR: Ungültige Spielersession! Spieler=" + player.getHttpSessionID() + " Game=" + gameID);
			player.sendInfoMessage("ERROR: Ungültige Spielersession!");
		}
		else
			System.out.println("ERROR: Spieler noch nicht initialisiert!");
	}

	/**
	 * Setzt n�chstes currentField
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

	@Override
	public Boolean addPlayer(final Session playerSession)
	{
		String httpSessionID = ((HttpSession)playerSession.getUserProperties().get("session")).getId();

		if(gamestate != Gamestate.WAITING_FOR_PLAYER || isPlayerInGame(httpSessionID)) return false;

		if(playerOne == null){
			playerOne = new Player(playerSession);
			if(playerTwo == null) playerOne.sendInfoMessage("Warte auf Mitspieler!");
		}
		else if(playerTwo == null)
		{
			playerTwo = new Player(playerSession);
			if (playerOne == null) {
				playerTwo.sendMessage("{\"cmd\":\"roundPromt\"}");
				playerTwo.sendInfoMessage("Warte auf Mitspieler!");
			}
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
				case "setRounds":
					if(gamestate == Gamestate.WAITING_FOR_PLAYER) {
						if(rounds > 0 && rounds <= 5)
							rounds = obj.getInt("amount");
					}
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

		switch(currentGameType) {
			case TicTacToe:
				json.put("cmd", "field");
				json.put("type", "little");
				json.put("fieldData", fieldArray[0].getFieldArray());

				if(playerOne != null) playerOne.sendMessage(json.toString());
				if(playerTwo != null) playerTwo.sendMessage(json.toString());
				break;
			case Super_TicTacToe:
				json.put("cmd", "field");
				json.put("fieldData", getCompleteFieldArray());
				json.put("currentfield", getCurrentField());
				json.put("type", "big");
				json.put("bigFieldData", getBigFieldFieldArray());

				if(playerOne != null) playerOne.sendMessage(json.toString());
				if(playerTwo != null) playerTwo.sendMessage(json.toString());
				break;
		}

	}
	
	public int getCompleteResult() {
		switch(currentGameType) {
		case TicTacToe:
			return fieldArray[0].getResult();
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
			//Diagonale oben rechts nach unten links pr�fen
			if((getLittleFieldResult(2) == 1)&&(getLittleFieldResult(4) == 1)&&(getLittleFieldResult(6) == 1)) {
				return 1;
			}
			if((getLittleFieldResult(2) == 2)&&(getLittleFieldResult(4) == 2)&&(getLittleFieldResult(6) == 2)) {
				return 2;
			}

			int emptyFields = 0;
			for(int f = 0; f < 9; f++)
			{
				if(getLittleFieldResult(f) == 0) emptyFields++;
			}

			if(emptyFields == 0) return -1;

			return 0;
		default:
			return 42;
		}
	}
	
	/**
	 * Nach jeder Spielrunde wird der counter um 1 nach oben gez�hlt.
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

	private int[] getBigFieldFieldArray() {
		int[] result = new int[9];
		for(int i=0; i<9; i++) {
			result[i] = getLittleFieldResult(i);
		}
		return result;
	}

	/**
	 * Leitet je nach aktuellem Feldtyp auf Funktionen der Spielinstanzen weiter.
	 * @return Array mit dem Zustand des aktuellen Spielfeldes. int[8] bei TicTacToe, int[81] bei SuperTicTacToe
	 */
	public int[] getCompleteFieldArray() {
		int[] result = new int[81];
		for(int i=0; i<81; i++) {
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
	 * Gibt Spieltyp des aktuellen Spielfeldes zur�ck (Normal oder Super)
	 * @return currentGameType of field
	 */
	public GameType getGameTypeOfField() {
		return currentGameType;
	}
}

