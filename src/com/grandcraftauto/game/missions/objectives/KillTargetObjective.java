package com.grandcraftauto.game.missions.objectives;

import org.bukkit.entity.EntityType;

import com.grandcraftauto.game.missions.Dialogue;
import com.grandcraftauto.game.missions.Objective;
import com.grandcraftauto.game.player.GPlayer;

public class KillTargetObjective extends Objective {
	
	private EntityType target;
	private int amountToKill;
	private String targetName;
	private boolean killUntilGone;
	public KillTargetObjective(String desc, Dialogue dialogue, boolean revert, EntityType target, int amountToKill, String targetName, boolean killUntilGone){
		super(desc, dialogue, revert);
		this.target = target;
		this.amountToKill = amountToKill;
		this.targetName = targetName;
		this.killUntilGone = killUntilGone;
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
	 * Check if the targets should be killed until they're all gone
	 * @return True if they should be killed until they're all gone, false if not
	 */
	public boolean shouldKillUntilGone(){
		return killUntilGone;
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
