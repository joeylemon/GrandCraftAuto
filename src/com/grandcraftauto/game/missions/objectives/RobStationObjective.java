package com.grandcraftauto.game.missions.objectives;

import com.grandcraftauto.game.missions.Dialogue;
import com.grandcraftauto.game.missions.Objective;

public class RobStationObjective extends Objective{

	private int robPeriod;
	
	public RobStationObjective(String description, Dialogue dialogue, int robPeriod){
		super(description, dialogue);
		this.robPeriod = robPeriod;
	}
	
	/**
	 * Get how long the robbery should the last
	 * @return The time, in seconds, that the robbery should last
	 */
	public int getRobbingPeriod(){
		return robPeriod;
	}
}
