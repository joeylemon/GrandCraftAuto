package gca.game.missions.objectives;

import gca.game.missions.Character;
import gca.game.missions.Dialogue;
import gca.game.missions.Objective;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ApproachObjective extends Objective{

	private Character approach;
	private ItemStack toGive;
	private Material toReturn;
	private int amountToReturn;
	private boolean autoDialogue;
	
	public ApproachObjective(String desc, Dialogue dialogue, Character approach, ItemStack toGive, Material toReturn, int amountToReturn, boolean autoDialogue){
		super(desc, dialogue);
		this.approach = approach;
		this.toGive = toGive;
		this.toReturn = toReturn;
		this.amountToReturn = amountToReturn;
		this.autoDialogue = autoDialogue;
	}
	
	/**
	 * Get the character to approach
	 * @return The character to approach
	 */
	public Character getToApproach(){
		return approach;
	}
	
	/**
	 * Get the item to give to the player after approaching
	 * @return The item to give to the player after approaching
	 */
	public ItemStack getItemToGive(){
		return toGive;
	}
	
	/**
	 * Get the type of item the player has to return
	 * @return The type of item the player has to return
	 */
	public Material getItemToReturn(){
		return toReturn;
	}
	
	/**
	 * Get the amount of items the player has to return
	 * @return The amount of items the player has to return
	 */
	public int getAmountOfItemToReturn(){
		return amountToReturn;
	}
	
	/**
	 * Get the objective's auto dialogue boolean
	 * @return The objective's auto dialogue boolean
	 */
	public boolean shouldAutoDialogue(){
		return autoDialogue;
	}
}
