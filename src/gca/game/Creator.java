package gca.game;

import gca.core.GPlayer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class Creator {
	
	public String gray = ChatColor.GRAY + "";
	public String gold = ChatColor.GOLD + "";
	
	public int step = 1;
	public GPlayer creator;
	public int totalSteps;
	public Creator(Player creator, int totalSteps){
		this.creator = new GPlayer(creator);
		this.totalSteps = totalSteps;
	}
	
	/**
	 * Get the player of the creator
	 * @return Get the player of the creator
	 */
	public GPlayer getCreator(){
		return creator;
	}
	
	/**
	 * Get the total steps of the creator
	 * @return The total steps of the creator
	 */
	public int getTotalSteps(){
		return totalSteps;
	}
	
	/**
	 * Get the current step of the creator
	 * @return The current step of the creator
	 */
	public int getStep(){
		return step;
	}
	
	/**
	 * Send the player a step
	 * @param step - The step to send to the player
	 */
	public abstract void sendStep(int step);
	
	/**
	 * Advance the step of the creator
	 */
	public void advanceStep(){
		if(step < totalSteps){
			step++;
			this.sendStep(step);
		}
	}
}
