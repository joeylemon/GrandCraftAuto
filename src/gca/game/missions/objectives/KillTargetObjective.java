package gca.game.missions.objectives;

import gca.game.missions.Dialogue;
import gca.game.missions.Objective;
import gca.game.player.GPlayer;

import org.bukkit.entity.EntityType;

public class KillTargetObjective extends Objective {
	
	private EntityType target;
	private int amountToKill;
	private String targetName;
	
	public KillTargetObjective(String desc, Dialogue dialogue, EntityType target, int amountToKill, String targetName){
		super(desc, dialogue);
		this.target = target;
		this.amountToKill = amountToKill;
		this.targetName = targetName;
	}
	
	/**
	 * Get the entity type of the target
	 * @return The entity type of the target
	 */
	public EntityType getTarget(){
		return target;
	}
	
	/**
	 * Get the target's name
	 * @return The target's name
	 */
	public String getTargetName(){
		return targetName;
	}
	
	/**
	 * Get the amount of the entity that should be killed
	 * @return The amount of the entity that should be killed
	 */
	public int getAmountToKill(){
		return amountToKill;
	}
	
	/**
	 * Get the amount of the entity that has been killed
	 * @param player - The player to get the amount killed from
	 * @return The amount of the entity that has been killed
	 */
	public int getAmountKilled(String player){
		GPlayer gplayer = new GPlayer(player);
		return gplayer.getCurrentObjectiveAmount(this.getTarget().toString().toLowerCase());
	}
	
	/**
	 * Set the amount of the entity that has been killed
	 * @param player - The player to set the amount killed for
	 * @param amnt - The amount of the entity that has been killed
	 */
	public void setAmountKilled(String player, int amnt){
		GPlayer gplayer = new GPlayer(player);
		gplayer.setCurrentObjectiveAmount(this.getTarget().toString().toLowerCase(), amnt);
	}
}
