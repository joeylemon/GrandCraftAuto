package com.grandcraftauto.game.jobs;

public class Heist extends Job {

	public Heist(int id){
		super(id);
	}

	public int getMinimumPlayers(){
		return 4;
	}

	public int getMaximumPlayers(){
		return 4;
	}

	public String getType(){
		return "Heist";
	}

	public boolean hasTeams(){
		return false;
	}
	
	/**
	 * Get the type of heist
	 * @return The type of heist
	 */
	public HeistType getHeistType(){
		HeistType type = null;
		String conf = this.getFile().getConfig().getString("heistType");
		for(HeistType t : HeistType.values()){
			if(conf.equalsIgnoreCase(t.toString()) == true){
				type = t;
				break;
			}
		}
		return type;
	}
	
	

}
