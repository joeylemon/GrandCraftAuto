package com.grandcraftauto.game.missions.objectives;

import org.bukkit.Material;

import com.grandcraftauto.game.VillagerType;
import com.grandcraftauto.game.missions.Dialogue;
import com.grandcraftauto.game.missions.Objective;
import com.grandcraftauto.game.player.GPlayer;

public class ObtainItemsObjective extends Objective{

	private Material itemType;
	private int amount;
	private VillagerType toInvoke;
	private String gangToInvoke;
	
	public ObtainItemsObjective(String desc, Dialogue dialogue, Material itemType, int amount, VillagerType toInvoke, String gangToInvoke){
		super(desc, dialogue);
		this.itemType = itemType;
		this.amount = amount;
		this.toInvoke = toInvoke;
		this.gangToInvoke = gangToInvoke;
	}
	
	/**
	 * Get the material of the item
	 * @return The material of the item
	 */
	public Material getItemType(){
		return itemType;
	}
	
	/**
	 * Get the amount of the item to be collected
	 * @return The amount of the item to be collected
	 */
	public int getAmountToObtain(){
		return amount;
	}
	
	/**
	 * Get the type of villager to be invoked for obtaining the item
	 * @return The type of villager to be invoked for obtaining the item
	 */
	public VillagerType getToInvoke(){
		return toInvoke;
	}
	
	/**
	 * Get the gang to be invoked for obtaining the item
	 * @return The gang to be invoked for obtaining the item
	 */
	public String getGangToInvoke(){
		return gangToInvoke;
	}
	
	/**
	 * Set the amount of items a player has obtained
	 * @param player - The player to set the obtained items amount for
	 * @param amount - The amount of items that have been picked up
	 */
	public void setAmountObtained(String player, int amount){
		GPlayer gplayer = new GPlayer(player);
		gplayer.setCurrentObjectiveAmount(this.getItemType().toString().toLowerCase(), amount);
	}
	
	/**
	 * Get the amount of items that have been obtained
	 * @param player - The player to get the amount of obtained items from
	 * @return The amount of the items that have been obtained
	 */
	public int getAmountObtained(String player){
		GPlayer gplayer = new GPlayer(player);
		return gplayer.getCurrentObjectiveAmount(this.getItemType().toString().toLowerCase());
	}
}
