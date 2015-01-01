package gca.game.missions;

public class Dialogue {
	
	private Character character;
	private String[] dialogue;
	public Dialogue(Character character, String... dialogue){
		this.character = character;
		this.dialogue = dialogue;
	}
	
	/**
	 * Get the character speaking
	 * @return The character speaking
	 */
	public Character getCharacter(){
		return character;
	}
	
	/**
	 * Get what the character is saying
	 * @return What the character is saying
	 */
	public String[] getDialogue(){
		return dialogue;
	}
}
