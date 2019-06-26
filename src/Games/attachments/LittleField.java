package Games.attachments;

public class LittleField {
	Tile[] field = new Tile[9];
	Tile errorTile = new Tile();
	
	public LittleField() {
		for(int i=0; i<=8; i++) {
			this.field[i] = new Tile();
		}
	}
	
	public Tile getTile(int fieldNumber) {
		if ((fieldNumber>=1) && (fieldNumber<=9)) {
			return field[fieldNumber-1];
		}else {
			return errorTile;
		}
	}

	public int[] getFieldArray() {
		int[] result = new int[9];
		for(int i= 0; i<=8; i++) {
			result[i] = this.field[i].getPlayer();
		}
		return result;
	}
	
	public int getResult() {
		//Durch Spalten durchgehen
		for(int i=0; i<3; i++) {
			if((this.field[i].getPlayer() == 1)&&(this.field[i+3].getPlayer() == 1)&&(this.field[i+6].getPlayer() == 1)) {
				return 1;
			}else {
				if((this.field[i].getPlayer() == 2)&&(this.field[i+3].getPlayer() == 2)&&(this.field[i+6].getPlayer() == 2)) {
					return 2;
				}
			}
		}
		//Durch Reihen durchgehen
		for(int i=0; i<=6; i+=3) {
			if((this.field[i].getPlayer() == 1)&&(this.field[i+1].getPlayer() == 1)&&(this.field[i+2].getPlayer() == 1)) {
				return 1;
			}else {
				if((this.field[i].getPlayer() == 2)&&(this.field[i+1].getPlayer() == 2)&&(this.field[i+2].getPlayer() == 2)) {
					return 2;
				}
			}
		}
		//Diagonale oben links nach unten rechts prüfen
		if((this.field[0].getPlayer() == 1)&&(this.field[4].getPlayer() == 1)&&(this.field[8].getPlayer() == 1)) {
			return 1;
		}
		if((this.field[0].getPlayer() == 2)&&(this.field[4].getPlayer() == 2)&&(this.field[8].getPlayer() == 2)) {
			return 2;
		}
		//Diagonale oben rechts nach unten links prüfen
		if((this.field[2].getPlayer() == 1)&&(this.field[4].getPlayer() == 1)&&(this.field[6].getPlayer() == 1)) {
			return 1;
		}
		if((this.field[2].getPlayer() == 2)&&(this.field[4].getPlayer() == 2)&&(this.field[6].getPlayer() == 2)) {
			return 2;
		}

		int emptyFields = 0;
		for(int f = 0; f < 9; f++)
		{
			if(field[f].getPlayer() == 0) emptyFields++;
		}

		if(emptyFields == 0) return -1;

		return 0;
	}

	public void reset()
	{
		for(int i=0; i<=8; i++) {
			this.field[i].setPlayer(0);
		}
	}
}
