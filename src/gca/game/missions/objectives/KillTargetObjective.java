package gca.game.missions.objectives;

import gca.core.GPlayer;
import gca.game.missions.Dialogue;
import gca.game.missions.Objective;

import org.bukkit.entity.EntityType;

public class KillTargetObjective extends Objective {
	
	private EntityType target;
	private int amountToKill;
	private String targetName;
	
	/**
	 * An objective for killing a certain amount of a specific entity type
	 * @param objDesc - The description of the objective
	 * @param objDialogue - The dialogue after completing the objective
	 * @param objTarget - The type of entity to be killed
	 * @param objAmountToKill - The amount of the entity to be killed
	 * @param objTargetName - The name of the entity (this is optional)
	 */
	public KillTargetObjective(String objDesc, Dialogue objDialogue, EntityType objTarget, int objAmountToKill, String objTargetName){
		super(objDesc, objDialogue);
		target = objTarget;
		amountToKill = objAmountToKill;
		targetName = objTargetName;
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
