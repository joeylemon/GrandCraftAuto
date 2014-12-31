package gca.game.missions.objectives;

import gca.game.missions.Dialogue;
import gca.game.missions.Objective;

public class RobStationObjective extends Objective{

	private int robPeriod;
	
	/**
	 * An objective for robbing a gas station
	 * @param description - The description of the objective
	 * @param dialogue - The dialogue after completing the objective
	 * @param robPeriod - The time, in seconds, that the robbery should last
	 */
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
