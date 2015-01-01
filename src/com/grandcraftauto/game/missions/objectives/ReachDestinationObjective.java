package com.grandcraftauto.game.missions.objectives;

import org.bukkit.Location;

import com.grandcraftauto.game.missions.Dialogue;
import com.grandcraftauto.game.missions.Objective;

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
