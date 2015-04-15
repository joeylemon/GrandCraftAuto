package com.grandcraftauto.game.missions;


public class Objective {
	
	private String description;
	private Dialogue dialogue;
	private boolean revertOnDeath;
	
	public Objective(String description, Dialogue dialogue, boolean revertOnDeath){
		this.description = description;
		this.dialogue = dialogue;
		this.revertOnDeath = revertOnDeath;
	}
	
	/**
	 * Get the objective's description
	 * @return The objective's description
	 */
	public String getDescription(){
		return description;
	}

	/**
	 * Get the objective's dialogue
	 * @return The objective's dialogue
	 */
	public Dialogue getDialogue(){
		return dialogue;
	}
	
	/**
	 * Check if the objective should revert back to the previous objective upon player death
	 * @return True if it should revert, false if not
	 */
	public boolean shouldRevertOnDeath(){
		return revertOnDeath;
	}
}
