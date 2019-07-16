package Games.attachments;

import java.util.Random;

public class FancyLittleField extends LittleField{
	Random rnd = new Random();
	ItemList item;
	
	/**
	 * Initialisiere ein Feld mit verteilten Items.
	 * Chancen:
	 * NONE 70%
	 * MINE 8%
	 * SWAP 5%
	 * DELETE 5%
	 * DOUBLE Turn 5%
	 * TIME_OUT 5%
	 * BOMB 1%
	 * SWAP_ALL 1%
	 */
	public FancyLittleField(){
		for(int i=0; i<=8; i++) {
			int random = rnd.nextInt(100);
			if(random<70) {
				item = ItemList.NONE;
			}
			if((random>=70)&&(random<=77)) {
				item = ItemList.MINE;
			}
			if((random>=78)&&(random<=82)) {
				item = ItemList.SWAP;
			}
			if((random>=83)&&(random<=87)) {
				item = ItemList.DELETE;
			}
			if((random>=88)&&(random<=92)) {
				item = ItemList.DOUBLE_TURN;
			}
			if((random>=93)&&(random<=97)) {
				item = ItemList.TIME_OUT;
			}
			if(random == 98) {
				item = ItemList.SWAP_ALL;
			}
			if(random == 99) {
				item = ItemList.BOMB;
			}
			this.field[i] = new Tile(item);
		}
	}
	
	public ItemList getItemOfTile(int fieldNumber) {
		if ((fieldNumber>=0) && (fieldNumber<=8)) {
			return field[fieldNumber].getItem();
		}else {
			return ItemList.NONE;
		}
	}
	
	public void setItemOfTile(int fieldNumber, ItemList item){
		if ((fieldNumber>=0) && (fieldNumber<=8)) {
			field[fieldNumber].setItem(item);
		}
	}
}

