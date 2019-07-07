package Games.instances;

import Games.attachments.LittleField;
import Games.attachments.Player;
import Games.attachments.GameTypes;

public class NormalGame_Instance extends TicTacToe_Instance{
private LittleField field;
	
	public NormalGame_Instance() {
		this.field = new LittleField();
	}
	
	@Override
	public void click(Player player, int field) {
		if(player.equals(currentPlayer)&&(1<=field)&&(9>=field)) {
			setTile(player, field);
			if(player.equals(playerOne)) {
				setCurrentPlayer(playerTwo);
			}else {
				setCurrentPlayer(playerOne);
			}
		}
	}
	
	/**
	 * manuelles Setzen eines Feldes für einen Spieler. currentPlayer bleibt gleich.
	 * @param player
	 * @param fieldNumber
	 * @return
	 */
	public boolean setTile(Player player, int fieldNumber) {
		if((playerOne == player) && ((1<=fieldNumber)&&(fieldNumber<=9))) {
			this.field.getTile(fieldNumber).setPlayer(1);
			return true;
		}
		if((playerTwo == player) && ((1<=fieldNumber)&&(fieldNumber<=9))) {
			this.field.getTile(fieldNumber).setPlayer(2);
			return true;
		}
		return false;
	}
	
	/**
	 * Gibt Status des gesamten Spielfeldes als Array aus.
	 * @return int Array mit Länge 9
	 */
	@Override
	public int[] getField() {
		return this.field.getFieldArray();
	}
	
	/**
	 * Prüfen ob es einen Gewinner gibt
	 * @return 0 entspricht kein Gewinner, 1 und 2 entspricht welcher Spieler gewonnen hat
	 */
	@Override
	public int getCompleteResult() {
		return this.field.getResult();
	}
	
	@Override
	public int getLittleFieldResult(int number) {
		return getCompleteResult();
	}
	
	@Override
	public int getCurrentField() {
		return 1;
	}
	
	@Override
	public GameTypes getGameType() {
		return GameTypes.NORMAL;
	}

}
