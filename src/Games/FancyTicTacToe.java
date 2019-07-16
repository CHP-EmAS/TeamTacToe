package Games;

import Games.attachments.*;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

public class FancyTicTacToe extends Game{
	//Status des Spiels, ob ein normaler Klick folgt, eine Mine gesetzt wird, oder ein Feld gelöscht wird
	 public enum Status{
			NORMAL, MINE, DELETE;
		}
	
    private Player playerOne,playerTwo;
    private Player currentPlayer = null;

    private FancyLittleField[] fields;
    private FancyLittleField currentField;
    
    public Status status;

    public FancyTicTacToe(String gameID){
        super(GameType.Fancy_TicTacToe);

        super.gameID = gameID;

        playerOne = null;
        playerTwo = null;

        gamestate = Gamestate.WAITING_FOR_PLAYER;

        this.fields = new FancyLittleField[9];
        //Items werden im Konstruktor bereits gesetzt
        for(int i = 0; i<9; i++) fields[i] = new FancyLittleField();

        this.currentField = fields[0];
        status = Status.NORMAL;
    }

    @Override
    public Boolean addPlayer(final Session playerSession)
    {
        String httpSessionID = ((HttpSession)playerSession.getUserProperties().get("session")).getId();

        if(gamestate != Gamestate.WAITING_FOR_PLAYER || isPlayerInGame(httpSessionID)) return false;


        String host = "";
        if(playerSession.getRequestURI().getPort() != 80) host = playerSession.getRequestURI().getHost() + ":" + playerSession.getRequestURI().getPort();
        else host = playerSession.getRequestURI().getHost();

        String url = host + "/" + gametype.shortcut() + "/" + gameID;

        if(playerOne == null){
            playerOne = new Player(playerSession);
            if(playerTwo == null) {
                playerOne.sendInfoMessage("Warte auf Mitspieler!");
                playerOne.sendMessage("{\"cmd\":\"alert\",\"msg\":\"Wilkommen bei Super TicTacToe, sende deinem Mitspieler einfach diesen Link und schon kannst los gehen:\n" + url + "\"}");
            }
        }
        else if(playerTwo == null)
        {
            playerTwo = new Player(playerSession);
            if(playerOne == null)
            {
                playerTwo.sendInfoMessage("Warte auf Mitspieler!");
                playerOne.sendMessage("{\"cmd\":\"alert\",\"msg\":\"Wilkommen bei Super TicTacToe, sende deinem Mitspieler einfach diesen Link und schon kannst los gehen:\n" + url + "\"}");
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
                    fieldClick(obj.getInt("fieldNum"), sender);
                    break;
                case "reset":
                    if(getPlayerAmount() == 2 && gamestate == Gamestate.PAUSED)
                    {
                        if(currentPlayer != null)
                        {
                            if(currentPlayer.equals(playerOne)) setCurrentPlayer(playerTwo);
                            else setCurrentPlayer(playerOne);
                        }
                        else setCurrentPlayer(playerOne);

                        for(int i=0; i<=8; i++)
                        {
                            this.fields[i] = new FancyLittleField();
                        }

                        currentField = fields[0];

                        playerOne.sendMessage("{\"cmd\":\"disableReset\"}");
                        playerTwo.sendMessage("{\"cmd\":\"disableReset\"}");

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

            super.closeGame();

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
     * Setzen, welcher Spieler am Zug ist. Player One und PLayer Two mÃ¼ssen initialisiert sein!
     * Muss initialisiert werden vor Spielbeginn!
     * @param player Spieler-Instanz
     */
    private void setCurrentPlayer(Player player) {
        if(playerOne != null && playerTwo != null) {
            if (player.equals(playerOne)) {
                currentPlayer = playerOne;
                playerOne.sendInfoMessage("Du bist dran!");
                playerTwo.sendInfoMessage("Mitspieler wÃ¤hlt ein Feld!");
                return;
            }

            if (player.equals(playerTwo)) {
                currentPlayer = playerTwo;
                playerTwo.sendInfoMessage("Du bist dran!");
                playerOne.sendInfoMessage("Mitspieler wÃ¤hlt ein Feld!");
                return;
            }

            System.out.println("ERROR: UngÃ¼ltige Spielersession! Spieler=" + player.getHttpSessionID() + " Game=" + gameID);
            player.sendInfoMessage("ERROR: UngÃ¼ltige Spielersession!");
        }
        else
            System.out.println("ERROR: Spieler noch nicht initialisiert!");
    }

    private void updateUserField()
    {
        JSONObject json = new JSONObject();

        json.put("cmd", "field");
        json.put("fieldData", getCompleteFieldArray());
        json.put("currentfield", getCurrentField());
        json.put("bigFieldData", getBigFieldFieldArray());

        if(playerOne != null) playerOne.sendMessage(json.toString());
        if(playerTwo != null) playerTwo.sendMessage(json.toString());
    }

    //Ab hier Funktionen explizit fÃ¼r SuperTicTacToe

    /**
     * Standardaktion eines Nutzers.
     * Nummerierung der Felder bei SuperTicTacToe von 1-81. Es wird ein kleines Feld komplett durchgezÃ¤hlt und dann beim nÃ¤chsen
     * weitergemacht. Also Feld 1 (oben links) hat Felder 1 bis 9 und Feld 2 (oben Mitte) Felder 10 bis 18. Nummerierung der
     * Spielfelder wie die Nummerierung der kleinen Felder bei einfachem TicTacToe.
     *
     */
    private void fieldClick(int fieldNum, Player player)
    {
        if(gamestate == Gamestate.RUNNING)
        {
            if (playerOne != null && playerTwo != null) {
                if (fieldNum >= 0 && fieldNum < 81) {
                    if (player == currentPlayer) {
                    	//
            			switch(status) {
            			case NORMAL:
            				if((fields[fieldNum/9]==currentField)&&(currentField.getTile(fieldNum%9).getPlayer()==0)) {
            					switch(currentField.getItemOfTile(fieldNum%9)) {
            					//Normaler Click
            					case NONE: 
            						setTile((fieldNum%9));
            						switchCurrentPlayer();
            						break;
            					//Folgend das auslösen der verschiedenen Items
            					case SWAP:
            						triggerSwap(fieldNum);
            						break;
            					case DELETE:
            						triggerDelete(fieldNum); 
            						break;
            					case DOUBLE_TURN:
            						triggerDoubleTurn(fieldNum);
            						break;
            					case TIME_OUT:
            						triggerTimeOut(fieldNum);
            						break;
            					case TIME_OUT_VISIBLE_ONE:
            						triggerTimeOut(fieldNum);
            						break;
            					case TIME_OUT_VISIBLE_TWO:
            						triggerTimeOut(fieldNum);
            						break;
            					case MINE:
            						triggerMine(fieldNum);
            						break;
            					case BOMB:
            						triggerBomb(fieldNum);
            						break;
            					case SWAP_ALL:
            						triggerSwapAll(fieldNum);
            						break;
            					}
            					updateUserField();
            					int gameResult = getCompleteResult();

                                switch (gameResult) {
                                    case -1:
                                        playerOne.sendInfoMessage("Unentschieden!");
                                        playerTwo.sendInfoMessage("Unentschieden!");
                                        playerOne.sendMessage("{\"cmd\":\"enableReset\"}");
                                        playerTwo.sendMessage("{\"cmd\":\"enableReset\"}");
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
                                        playerTwo.sendMessage("{\"cmd\":\"enableReset\"}");

                                        if(playerOne.isRegisteredPlayer()) playerTwo.addPoints(3);

                                        gamestate = Gamestate.PAUSED;
                                        return;
                                    case 2:
                                        playerTwo.sendInfoMessage("Du hast gewonnen!");
                                        playerOne.sendInfoMessage("Du hast verloren!");
                                        playerOne.sendMessage("{\"cmd\":\"enableReset\"}");
                                        playerTwo.sendMessage("{\"cmd\":\"enableReset\"}");

                                        if(playerTwo.isRegisteredPlayer()) playerTwo.addPoints(3);

                                        gamestate = Gamestate.PAUSED;
                                        return;
                                }
            				}else {
                                player.sendInfoMessage("UngÃ¼ltiger Zug!");
                            }
            				break;
            			case MINE:
            				if((fields[fieldNum/9].getTile(fieldNum%9).getPlayer()==0)) {
            					setTimeOut(fieldNum);
            					switchCurrentPlayer();
            					status = Status.NORMAL;
            				}
            				updateUserField();
            				break;
            			case DELETE:
            				if((fields[fieldNum/9].getTile(fieldNum%9).getPlayer()!=0)) {
            					fields[fieldNum/9].getTile(fieldNum%9).setPlayer(0);
            					switchCurrentPlayer();
            					status = Status.NORMAL;
            				}
            				updateUserField();
            				break;
            			}
                    } else {
                        player.sendInfoMessage("Dein Mitspieler ist am Zug!");
                    }
                } else {
                    System.out.println("Fehler beim Klicken eines Feldes! Field <" + fieldNum + "> out of range");
                }
            } else {
                player.sendInfoMessage("UngÃ¼ltiger Zug! Sie brauchen einen Mitspieler!");
            }
        }
    }
    
	/**
	 * Auslösen eines Swap Events
	 * @param field Feld im Spielfeld auf dem das Event stattfindet
	 */
	private void triggerSwap(int field) {
		switchCurrentPlayer();
		setTile(field%9);
		setNextField(field%9);
		resetItem(field);
	}

	private void triggerDelete(int field) {
		status = Status.DELETE;
		setTile(field%9);
		resetItem(field);
		setNextField(field%9);
	}
	private void triggerDoubleTurn(int field) {
		setTile(field%9);
		resetItem(field);
	}
	
	private void triggerTimeOut(int field) {
		resetItem(field);
		switchCurrentPlayer();
	}
	private void triggerMine(int field) {
		status = Status.MINE;
		setTile(field%9);
		resetItem(field);
		setNextField(field%9);
	}
	
	private void triggerBomb(int field) {
		for(int i=0; i<=8; i++) {
			this.currentField.getTile(i).setPlayer(0);
		}
		resetItem(field);
		setNextField(field%9);
		switchCurrentPlayer();
	}
	
	private void triggerSwapAll(int field) {
		int[] fieldArray = getCompleteFieldArray();
		for(int i=0; i<81; i++) {
			if(fieldArray[i]==1) {
				fields[i/9].getTile(i%9).setPlayer(2);
			}
			if(fieldArray[i]==2) {
				fields[i/9].getTile(i%9).setPlayer(1);
			}
		}
		resetItem(field);
		setNextField(field%9);
		switchCurrentPlayer();
	}
	/**
	 * Setzt TimeOut Event auf übergebenes Feld
	 * @param field 0-80
	 */
	private void setTimeOut(int field) {
		if(this.currentPlayer.equals(this.playerOne)){
			fields[field/9].setItemOfTile(field%9, ItemList.TIME_OUT_VISIBLE_ONE);
		}else {
			fields[field/9].setItemOfTile(field%9, ItemList.TIME_OUT_VISIBLE_TWO);
		}
	}
	/**
	 * Setzt Item des Feldes auf NONE
	 * @param field 0-80 Feld auf Spielfeld
	 */
	private void resetItem(int field) {
		fields[field/9].getTile(field%9).setItem(ItemList.NONE);
	}

    private int[] getCompleteFieldArray() {
        int[] result = new int[81];
        for(int i=0; i<=80; i++) {
            result[i] = fields[(i/9)].getFieldArray()[(i % 9)];
        }
        return result;
    }

    private int[] getBigFieldFieldArray() {
        int[] result = new int[9];
        for(int i=0; i<9; i++) {
            result[i] = getLittleFieldResult(i);
        }
        return result;
    }

    /**
     * Ãœbergabe der Belegung eines einzelnen kleinen Feldes.
     * Nummerierung:
     * 1  2  3
     * 4  5  6
     * 7  8  9
     * @param number Nummer des Feldes
     * @return int-Array mit Belegung des Feldes
     */
    private int getLittleFieldResult(int number) {
        if((number>=0)&&(number<=8)) {
            return fields[number].getResult();
        }
        return 42;
    }

    private int getCompleteResult() {
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
        for(int i=0; i<7; i+=3) {
            if((getLittleFieldResult(i) == 1)&&(getLittleFieldResult(i+1) == 1)&&(getLittleFieldResult(i+2) == 1)) {
                return 1;
            }else {
                if((getLittleFieldResult(i) == 2)&&(getLittleFieldResult(i+1) == 2)&&(getLittleFieldResult(i+2) == 2)) {
                    return 2;
                }
            }
        }
        //Diagonale oben links nach unten rechts prÃ¼fen
        if((getLittleFieldResult(0) == 1)&&(getLittleFieldResult(4) == 1)&&(getLittleFieldResult(8) == 1)) {
            return 1;
        }
        if((getLittleFieldResult(0) == 2)&&(getLittleFieldResult(4) == 2)&&(getLittleFieldResult(8) == 2)) {
            return 2;
        }
        //Diagonale oben rechts nach unten links prÃ¼fen
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
    }

    private int getCurrentField() {
        for(int i=0; i<=8; i++) {
            if(this.fields[i] == this.currentField) {
                return i;
            }
        }
        return 42;
    }

    /**
     * Setzt Feld in aktuellem Spielfeld, für aktuellen Spieler. Wechselt nicht mehr automatisch currentPlayer. Dafür Funktion switchCurrentPlayer()
     * @param number Nummer von 1-9
     * @return gibt zurÃ¼ck ob Feld gesetzt wurde
     */
	public boolean setTile(int number) {
		boolean success = false;
		if((this.playerOne == this.currentPlayer) && ((0<=number)&&(number<=8))) {
			this.currentField.getTile(number).setPlayer(1);
			success = true;
		}else {
			if((this.playerTwo == this.currentPlayer) && ((0<=number)&&(number<=8))) {
				this.currentField.getTile(number).setPlayer(2);
				success = true;
			}
		}
		if(success) {
			setNextField(number);
		}
		return success;
	}
	/**
	 * Wechselt aktuellen Spieler
	 */
	public void switchCurrentPlayer() {
		if(this.currentPlayer.equals(this.playerOne)){
			this.currentPlayer = playerTwo;
		}else {
			this.currentPlayer = playerOne;
		}
	}
	/**
	 * Setzt nächstes currentField
	 * Wenn übergebenes Feld bereits beendet wird zufällig das nächste bestimmt
	 * @param number Nummer von 0-8 für nächstes CurrentField
	 */
	public void setNextField(int number) {
		Random rnd = new Random();
		int nextField;
		if(fields[number].getResult()==0) {
			nextField=number;
		}else {
			do {
				nextField = rnd.nextInt(9);
			}while(fields[nextField].getResult()!=0);
		}
		this.currentField = fields[nextField];
	}
}
