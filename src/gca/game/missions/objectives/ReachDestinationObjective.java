package gca.game.missions.objectives;

import gca.game.missions.Dialogue;
import gca.game.missions.Objective;

import org.bukkit.Location;

public class ReachDestinationObjective extends Objective{

	private Location destination;
	
	/**
	 * An objective for reaching a location
	 * @param objDesc - The description of the objective
	 * @param objDialogue - The dialogue after completing the objective
	 * @param objDestination - The location to be reached
	 */
	public ReachDestinationObjective(String objDesc, Dialogue objDialogue, Location objDestination){
		super(objDesc, objDialogue);
		destination = objDestination;
	}
	
	/**
	 * Get the destination of the objective
	 * @return The destination of the objective
	 */
	public Location getDestination(){
		return destination;
	}
}
