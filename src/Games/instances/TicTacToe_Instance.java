package Games.instances;

import Games.attachments.GameTypes;
import Games.attachments.Player;

public class TicTacToe_Instance {
	public Player playerOne, playerTwo = null;
	public Player currentPlayer = null;
	
	public TicTacToe_Instance() {
	}
	
	//!Session Objekte für Spieler müssen nach Erstellung der Klasse gesetzt werden, bevor gespielt werden kann!
	
	/**
	 * Player One eine Session zuweisen. Muss initialisiert werden vor Spielbeginn!
	 * @param session 
	 * @return true bei erstmaliger Zuweisung, oder wenn die Session die gleiche ist. False bei Versuch 
	 * einer erneuten Zuweisung.
	 */
	public boolean setPlayerOne(Player player) {
		if(this.playerOne != null) {
			if(this.playerOne.equals(player)) {
				return true;
			}else {
				return false;
			}
		}else {
			this.playerOne = player;
			return true;
		}
	}
	
	/**
	 * Muss initialisiert werden vor Spielbeginn!
	 * Player Two eine Session zuweisen
	 * @param session 
	 * @return true bei erstmaliger Zuweisung, oder wenn die Session die gleiche ist. False bei Versuch 
	 * einer erneuten Zuweisung.
	 */
	public boolean setPlayerTwo(Player player) {
		if(this.playerTwo != null) {
			if(this.playerTwo.equals(player)) {
				return true;
			}else {
				return false;
			}
		}else {
			this.playerTwo = player;
			return true;
		}
	}
	
	/**
	 * Setzen, welcher Spieler am Zug ist. Player One und PLayer Two müssen initialisiert sein!
	 * Muss initialisiert werden vor Spielbeginn!
	 * @param player
	 * @return True bei Erfolg. False bei Misserfolg
	 */
	public boolean setCurrentPlayer(Player player) {
		if(player.equals(playerOne)) {
			this.currentPlayer = this.playerOne;
			return true;
		}
		if(player.equals(playerTwo)) {
			this.currentPlayer = this.playerTwo;
			return true;
		}
		return false;
	}
	
	public void click(Player player, int field) {
	}
	
	public int getCompleteResult() {
		return 0;
	}
	
	public int getLittleFieldResult(int number) {
		return 0;
	}
	
	public int[] getField() {
		return new int[0];
	}
	
	public int getCurrentField() {
		return 0;
	}
	
	public GameTypes getGameType() {
		return GameTypes.NORMAL;
	}
}
