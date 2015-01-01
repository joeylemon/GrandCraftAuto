package gca.game.missions.objectives;

import gca.game.missions.Dialogue;
import gca.game.missions.Objective;

import org.bukkit.Location;

public class ReachDestinationObjective extends Objective{

	private Location destination;
	
	public ReachDestinationObjective(String desc, Dialogue dialogue, Location destination){
		super(desc, dialogue);
		this.destination = destination;
	}
	
	/**
	 * Get the destination of the objective
	 * @return The destination of the objective
	 */
	public Location getDestination(){
		return destination;
	}
}
