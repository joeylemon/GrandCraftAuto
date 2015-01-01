package com.grandcraftauto.game.missions;


public class Objective {
	
	private String description;
	private Dialogue dialogue;
	
	public Objective(String description, Dialogue dialogue){
		this.description = description;
		this.dialogue = dialogue;
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
}
