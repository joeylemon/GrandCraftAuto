package com.grandcraftauto.game.gps;

public enum Destination {
	
	AMMUNATION("ammunation"),
	CAR_DEALERSHIP("car dealership"),
	BURGERSHOT("burgershot"),
	BLACK_MARKET("black market"),
	BANK("bank"),
	APARTMENT_COMPLEX("manager"),
	GAS_STATION("gas station");
	
	private String keyword;
	Destination(String keyword){
		this.keyword = keyword;
	}
	
	/**
	 * Get the name of the NPC that hosts this destination
	 * @return The name of the NPC that hosts this destination
	 */
	public String getKeyword(){
		return keyword;
	}

}
