package com.grandcraftauto.game.missions.objectives;

import com.grandcraftauto.game.missions.Dialogue;
import com.grandcraftauto.game.missions.Objective;
import com.grandcraftauto.game.player.GPlayer;

public class StealKidneyObjective extends Objective {

	private int amount;
	public StealKidneyObjective(String description, Dialogue dialogue, boolean revertOnDeath, int amount){
		super(description, dialogue, revertOnDeath);
		this.amount = amount;
	}
	
	/**
	 * Get the amount the player has to steal
	 * @return The amount the player has to steal
	 */
	public int getAmountToSteal(){
		return amount;
	}
	
	/**
	 * Get the amount a player has stolen
	 * @param player - The player
	 * @return The amount the player has stolen
	 */
	public int getAmountStolen(String player){
		GPlayer gplayer = new GPlayer(player);
		return gplayer.getCurrentObjectiveAmount("stolenKidneys");
	}
	
	/**
	 * Set the amount a player has stolen
	 * @param player - The player
	 * @param amnt - The amount the player has stolen
	 */
	public void setAmountStolen(String player, int amnt){
		GPlayer gplayer = new GPlayer(player);
		gplayer.setCurrentObjectiveAmount("stolenKidneys", amnt);
	}
}
