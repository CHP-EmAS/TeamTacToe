package Games.instances;

import java.util.Random;

import Games.attachments.GameTypes;
import Games.attachments.LittleField;
import Games.attachments.Player;

public class SuperGame_Instance extends TicTacToe_Instance{
	LittleField[] fields;
	LittleField currentField;
	
	public SuperGame_Instance(){
		this.fields = new LittleField[9];
		for(int i=0; i<=8; i++) {
			this.fields[i] = new LittleField();
		}
		this.currentField = fields[0];
	}
	
	@Override
	/**
	 * Standardaktion eines Nutzers.
	 * Nummerierung der Felder bei SuperTicTacToe von 1-81. Es wird ein kleines Feld komplett durchgezählt und dann beim nächsen
	 * weitergemacht. Also Feld 1 (oben links) hat Felder 1 bis 9 und Feld 2 (oben Mitte) Felder 10 bis 18. Nummerierung der 
	 * Spielfelder wie die Nummerierung der kleinen Felder bei einfachem TicTacToe.
	 * 
	 */
	public void click(Player player, int field) {
		if((field>=0)&&(field<81)&&(player == currentPlayer)){
			if((fields[(field)/9]==currentField)&&(currentField.getTile((field)%9).getPlayer()==0)) {
				setTile(field%9);
			}
		}
	}
	
	@Override
	public int[] getField() {
		int[] result = new int[81];
		for(int i=0; i<=80; i++) {
			result[i] = fields[(i/9)].getFieldArray()[(i % 9)];
		}
		return result;
	}
	
	/**
	 * Übergabe der Belegung eines einzelnen kleinen Feldes. 
	 * Nummerierung:
	 * 0  1  2
	 * 3  4  5
	 * 6  7  8
	 * @param number Nummer des Feldes
	 * @return int-Array mit Belegung des Feldes
	 */
	@Override 
	public int getLittleFieldResult(int number) {
		if((number>=0)&&(number<=8)) {
			return fields[number].getResult();
		}
		return 42;
	}
	
	@Override
	public int getCompleteResult() {
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
		return 0;
	}
	
	@Override
	public int getCurrentField() {
		for(int i=0; i<=8; i++) {
			if(this.fields[i] == this.currentField) {
				return i+1;
			}
		}
		return 42;
	}
	
	public void setCurrentField(int number) {
		if(number<9 && number>=0) {
			currentField = fields[number];
		}
	}
	
	@Override
	public GameTypes getGameType() {
		return GameTypes.SUPER;
	}
	/**
	 * Vorgesehen für Funktion click();
	 * @param number
	 * @return
	 */
	private boolean setTile(int number) {
		Random rnd = new Random();
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
		return success;
	}
	/**
	 * Vorgesehen zum manuellen Setzen von Feldern
	 * @param player
	 * @param field
	 */
	public void setTile(Player player, int field) {
		if(field>=0 && field<=80) {
			if(player.equals(playerOne)) {
				fields[field/9].getTile(field%9).setPlayer(1);
			}
			if(player.equals(playerTwo)) {
				fields[field/9].getTile(field%9).setPlayer(2);
			}
		}
	}
}
