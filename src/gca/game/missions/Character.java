package gca.game.missions;

import org.apache.commons.lang3.text.WordUtils;

public enum Character {
	
	LAMAR,
	SIMEON,
	MICHAEL;
	
	/**
	 * Get the name of the character
	 * @return The name of the character
	 */
	public String getName(){
		return WordUtils.capitalizeFully(this.toString());
	}

}
