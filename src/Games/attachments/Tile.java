package Games.attachments;

public class Tile {
	int player;
	ItemList item;
	
	public Tile() {
		this.player = 0;
		this.item = ItemList.NONE;
	}
	
	public Tile(ItemList item) {
		this.player = 0;
		this.item = item;
	}
	
	/**
	 * 
	 * @param player 0 entspricht leer; 1 und 2 entsprechen den Spielern
	 */
	public void setPlayer(int player) {
		this.player = player;
	}
	
	/**
	 * 
	 * @param player 0 entspricht leer; 1 und 2 entsprechen den Spielern
	 */
	public int getPlayer() {
		return this.player;
	}
	
	public ItemList getItem() {
		return this.item;
	}
	
	public void setItem(ItemList item) {
		this.item = item;
	}
}
