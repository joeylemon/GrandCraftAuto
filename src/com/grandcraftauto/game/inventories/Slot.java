package com.grandcraftauto.game.inventories;

public enum Slot {
	
	GPS(8),
	PHONE(7),
	CAR(6);
	
	private int slot;
	Slot(int slot){
		this.slot = slot;
	}
	
	public int getSlot(){
		return slot;
	}
}
