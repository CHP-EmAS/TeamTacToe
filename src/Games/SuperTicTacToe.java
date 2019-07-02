package Games;

import Games.attachments.*;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class SuperTicTacToe extends Game{
	private Player playerOne,playerTwo;
	private Player currentPlayer = null;

	private LittleField[] fields;
	private LittleField currentField;
	
	public SuperTicTacToe(String gameID){
		super(GameType.Super_TicTacToe);

		super.gameID = gameID;
		
        playerOne = null;
        playerTwo = null;

        gamestate = Gamestate.WAITING_FOR_PLAYER;

		this.fields = new LittleField[9];

		for(int i=0; i<=8; i++)
		{
			this.fields[i] = new LittleField();
		}

		this.currentField = fields[0];
	}

	@Override
	public Boolean addPlayer(final Session playerSession, String nickname, String passwd)
	{
		String httpSessionID = ((HttpSession)playerSession.getUserProperties().get("sessionID")).getId();

		if(gamestate != Gamestate.WAITING_FOR_PLAYER || isPlayerInGame(httpSessionID)) return false;

		Boolean registeredPlayer = (!passwd.equals(""));

		if(playerOne == null){
			playerOne = new Player(playerSession,registeredPlayer,nickname,passwd);
			if(playerTwo == null) playerOne.sendInfoMessage("Warte auf Mitspieler!");
		}
		else if(playerTwo == null)
		{
			playerTwo = new Player(playerSession,registeredPlayer,nickname,passwd);
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

						for(int i=0; i<=8; i++)
						{
							this.fields[i] = new LittleField();
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

	/**
	 * Setzen, welcher Spieler am Zug ist. Player One und PLayer Two müssen initialisiert sein!
	 * Muss initialisiert werden vor Spielbeginn!
	 * @param player Spieler-Instanz
	 * @return True bei Erfolg. False bei Misserfolg
	 */
	private boolean setCurrentPlayer(Player player) {
		if(playerOne != null && playerTwo != null) {
			if (player.equals(playerOne)) {
				currentPlayer = playerOne;
				playerOne.sendInfoMessage("Du bist dran!");
				playerTwo.sendInfoMessage("Mitspieler wählt ein Feld!");
				return true;
			}

			if (player.equals(playerTwo)) {
				currentPlayer = playerTwo;
				playerTwo.sendInfoMessage("Du bist dran!");
				playerOne.sendInfoMessage("Mitspieler wählt ein Feld!");
				return true;
			}

			System.out.println("ERROR: Ungültige Spielersession! Spieler=" + player.getHttpSessionID() + " Game=" + gameID);
			player.sendInfoMessage("ERROR: Ungültige Spielersession!");
		}
		else
			System.out.println("ERROR: Spieler noch nicht initialisiert!");

		return false;
	}

	private void updateUserField()
	{
		String msg = "{\"cmd\":\"field\",\"fieldData\":" + Arrays.toString(getCompleteFieldArray()) + "}";

		if(playerOne != null) playerOne.sendMessage(msg);
		if(playerTwo != null) playerTwo.sendMessage(msg);
	}

    //Ab hier Funktionen explizit für SuperTicTacToe
    
	/**
	 * Standardaktion eines Nutzers.
	 * Nummerierung der Felder bei SuperTicTacToe von 1-81. Es wird ein kleines Feld komplett durchgezählt und dann beim nächsen
	 * weitergemacht. Also Feld 1 (oben links) hat Felder 1 bis 9 und Feld 2 (oben Mitte) Felder 10 bis 18. Nummerierung der 
	 * Spielfelder wie die Nummerierung der kleinen Felder bei einfachem TicTacToe.
	 * 
	 */
	private void fieldClick(int fieldNum, Player player)
	{
		if(gamestate == Gamestate.RUNNING)
		{
			if (playerOne != null && playerTwo != null) {
				if (fieldNum > 0 && fieldNum < 82) {
					if (player == currentPlayer) {
						if (fields[fieldNum/9]==currentField && currentField.getTile((fieldNum-1)%9+1).getPlayer()==0) {

							//ändert currentField gleich mit
							setTile((fieldNum-1)%9);

							updateUserField();

							int gameResult = getCompleteResult();

							switch (gameResult) {
								case -1:
									playerOne.sendInfoMessage("Unentschieden!");
									playerTwo.sendInfoMessage("Unentschieden!");
									playerOne.sendMessage("{\"cmd\":\"enableReset\"}");
									gamestate = Gamestate.PAUSED;
									return;
								case 0:
									if(player.equals(playerOne)) setCurrentPlayer(playerTwo);
									else setCurrentPlayer(playerOne);
									return;
								case 1:
									playerOne.sendInfoMessage("Du hast gewonnen!");
									playerTwo.sendInfoMessage("Du hast verloren!");
									playerOne.sendMessage("{\"cmd\":\"enableReset\"}");
									gamestate = Gamestate.PAUSED;
									return;
								case 2:
									playerTwo.sendInfoMessage("Du hast gewonnen!");
									playerOne.sendInfoMessage("Du hast verloren!");
									playerOne.sendMessage("{\"cmd\":\"enableReset\"}");
									gamestate = Gamestate.PAUSED;
									return;
							}
						} else {
							player.sendInfoMessage("Ungültiger Zug!");
						}
					} else {
						player.sendInfoMessage("Dein Mitspieler ist am Zug!");
					}
				} else {
					System.out.println("Fehler beim Klicken eines Feldes! Field <" + fieldNum + "> out of range");
				}
			} else {
				player.sendInfoMessage("Ungültiger Zug! Sie brauchen einen Mitspieler!");
			}
		}
	}
    
	private int[] getCompleteFieldArray() {
		int[] result = new int[81];
		for(int i=0; i<=80; i++) {
			result[i] = fields[(i/9)].getFieldArray()[(i % 9)];
		}
		return result;
	}
	
	/**
	 * Übergabe der Belegung eines einzelnen kleinen Feldes. 
	 * Nummerierung:
	 * 1  2  3
	 * 4  5  6
	 * 7  8  9
	 * @param number Nummer des Feldes
	 * @return int-Array mit Belegung des Feldes
	 */ 
	private int getLittleFieldResult(int number) {
		if((number>=1)&&(number<=9)) {
			return fields[number-1].getResult();
		}
		return 42;
	}

	private int getCompleteResult() {
		//Durch Spalten durchgehen
		for(int i=1; i<4; i++) {
			if((getLittleFieldResult(i) == 1)&&(getLittleFieldResult(i+3) == 1)&&(getLittleFieldResult(i+6) == 1)) {
				return 1;
			}else {
				if((getLittleFieldResult(i) == 2)&&(getLittleFieldResult(i+3) == 2)&&(getLittleFieldResult(i+6) == 2)) {
					return 2;
				}
			}
		}
		//Durch Reihen durchgehen
		for(int i=1; i<=7; i+=3) {
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
		return 0;
	}

	private int getCurrentField() {
		for(int i=0; i<=8; i++) {
			if(this.fields[i] == this.currentField) {
				return i+1;
			}
		}
		return 42;
	}
	
	/**
	 * Setzt Feld in aktuellem Spielfeld, für aktuellen Spieler. Wechselt automatisch aktuellen Spieler. Wechselt aktuelles Spielfeld
	 * @param number Nummer von 1-9
	 * @return gibt zurück ob Feld gesetzt wurde
	 */
	private boolean setTile(int number) {

		boolean success = false;

		if(this.playerOne.equals(this.currentPlayer) && number>0 && number<=9) {
			this.currentField.getTile(number).setPlayer(1);
			success = true;
		}else if(this.playerTwo.equals(this.currentPlayer) && number>0 && number<=9) {
			this.currentField.getTile(number).setPlayer(2);
			success = true;
		}

		if(success){
			Random rnd = new Random();
			//Neues aktuelles Feld, entpsrechend der Nummer des gesetzten Feldes. Wenn dieses schon abgeschlossen ist wird ein
			//zufälliges gesetzt
			int nextField;
			if(fields[number-1].getResult()==0){
				nextField=number-1;
			}else{
				do {
					nextField = rnd.nextInt(9);
				}while(fields[nextField].getResult()!=0);
			}
			this.currentField = fields[nextField];
		}

		return success;
	}
}