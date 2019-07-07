package Games.attachments;

public class Tile {
	int player;
	//Später Item einfügen
	
	public Tile() {
		this.player = 0;
	}
	
	/**
	 * @param player 0 entspricht leer; 1 und 2 entsprechen den Spielern
	 */
	public Boolean setPlayer(int player) {
		if(player>=0 && player<=2) {
			this.player = player;
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * @return player 0 entspricht leer; 1 und 2 entsprechen den Spielern
	 */
	public int getPlayer() {
		return this.player;
	}
}
