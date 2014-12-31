package gca.game.missions.objectives;

import gca.game.missions.Character;
import gca.game.missions.Dialogue;
import gca.game.missions.Objective;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ApproachObjective extends Objective{

	private Character toApproach;
	private ItemStack itemToGive;
	private Material itemToReturn;
	private int amountItemToReturn;
	private boolean autoDialogue;
	
	/**
	 * An objective for approaching a specific NPC
	 * @param objDesc - The description of the objective
	 * @param objDialogue - The dialogue after completing the objective
	 * @param objToApproach - The name of the NPC to approach
	 */
	public ApproachObjective(String objDesc, Dialogue objDialogue, Character lamar, ItemStack itemToGive, Material itemToReturn, int amountItemToReturn, boolean autoDialogue){
		super(objDesc, objDialogue);
		this.toApproach = lamar;
		this.itemToGive = itemToGive;
		this.itemToReturn = itemToReturn;
		this.amountItemToReturn = amountItemToReturn;
		this.autoDialogue = autoDialogue;
	}
	
	/**
	 * Get the character to approach
	 * @return The character to approach
	 */
	public Character getToApproach(){
		return toApproach;
	}
	
	/**
	 * Get the item to give to the player after approaching
	 * @return The item to give to the player after approaching
	 */
	public ItemStack getItemToGive(){
		return itemToGive;
	}
	
	/**
	 * Get the type of item the player has to return
	 * @return The type of item the player has to return
	 */
	public Material getItemToReturn(){
		return itemToReturn;
	}
	
	/**
	 * Get the amount of items the player has to return
	 * @return The amount of items the player has to return
	 */
	public int getAmountOfItemToReturn(){
		return amountItemToReturn;
	}
	
	/**
	 * Get the objective's auto dialogue boolean
	 * @return The objective's auto dialogue boolean
	 */
	public boolean shouldAutoDialogue(){
		return autoDialogue;
	}
}
