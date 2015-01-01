package com.grandcraftauto.game.missions;

public enum SideMissionType{
	
	DAILY(86400000);
	
	private long millisToAdd;
	SideMissionType(long toAdd){
		millisToAdd = toAdd;
	}
	
	public long getMillisecondsToAdd(){
		return millisToAdd;
	}
}
