package gca.game.missions.objectives;

import gca.game.missions.Dialogue;
import gca.game.missions.Objective;
import gca.game.player.GPlayer;

public class EscapeCopsObjective extends Objective{

	private int times;
	public EscapeCopsObjective(String description, Dialogue dialogue, int times){
		super(description, dialogue);
		this.times = times;
	}
	
	/**
	 * Get how many times the player must escape the cops
	 * @return The amount of times the player must lose the cops
	 */
	public int getTimesToEscape(){
		return times;
	}
	
	/**
	 * Get the amount of times the player has lost the cops
	 * @param player - The player to get the times escaped from
	 * @return The amount of times the player has lost the cops
	 */
	public int getTimesEscaped(String player){
		GPlayer gplayer = new GPlayer(player);
		return gplayer.getCurrentObjectiveAmount("copsEscaped");
	}
	
	/**
	 * Set the amount of times the player has lost the cops
	 * @param player - The player to set the times escaped for
	 * @param amnt - The amount of times the player has lost the cops
	 */
	public void setTimesEscaped(String player, int amnt){
		GPlayer gplayer = new GPlayer(player);
		gplayer.setCurrentObjectiveAmount("copsEscaped", amnt);
	}
}
