package gca.game.crew;

import gca.core.GPlayer;

public interface CrewMember {
	
	/**
	 * Get the member's crew
	 * @return The member's crew
	 */
	public Crew getCrew();
	
	/**
	 * Set the member's crew
	 * @param crew - The crew to set as the member's crew
	 */
	public void setCrew(Crew crew);
	
	/**
	 * Check if the member has a crew
	 * @return True or false depending on if the member has a crew or not
	 */
	public boolean hasCrew();
	
	/**
	 * Get the member's rank
	 * @return The member's rank
	 */
	public CrewRank getCrewRank();
	
	/**
	 * Get the member's name
	 * @return The member's name
	 */
	public String getName();
	
	/**
	 * Get the GPlayer instance of the member
	 * @return The GPlayer instance of the member
	 */
	public GPlayer getPlayer();
}
