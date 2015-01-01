package gca.game.missions;

import gca.core.Main;
import gca.utils.Utils;

public class SideMission {

	static Main main = Main.getInstance();
	
	/**
	 * Check if there are any side missions
	 * @return True or false depending on if there are any side missions or not
	 */
	public static boolean anySideMissions(){
		boolean anySideMissions = false;
		for(Mission m : Mission.values()){
			if(m.getType() == MissionType.SIDE_MISSION){
				anySideMissions = true;
				break;
			}
		}
		return anySideMissions;
	}
	
	/**
	 * Randomize the side mission
	 * @param type - The side mission type to randomize
	 */
	public static void randomizeSideMission(SideMissionType type){
		Mission mission = null;
		while(mission == null){
			for(Mission m : Mission.values()){
				if(m.getType() == MissionType.SIDE_MISSION){
					if(Utils.randInt(1, 7) == 1){
						mission = m;
						break;
					}
				}
			}
		}
		setSideMission(type, mission);
		main.getConfig().set("sidemissions." + type.toString().toLowerCase() + ".lastReset", System.currentTimeMillis() + type.getMillisecondsToAdd());
		main.saveConfig();
	}
	
	/**
	 * Get the last reset of the side mission
	 * @param type - The side mission type
	 * @return The last reset of the side mission
	 */
	public static long getLastReset(SideMissionType type){
		return main.getConfig().getLong("sidemissions." + type.toString().toLowerCase() + ".lastReset");
	}
	
	/**
	 * Check if a side mission can be randomized
	 * @param type - The side mission type to check
	 * @return True or false depending on if the side mission can be randomized
	 */
	public static boolean canBeRandomized(SideMissionType type){
		if((System.currentTimeMillis() - getLastReset(type)) > 0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Set the side mission for the type
	 * @param type - The side mission type to set
	 * @param mission - The mission to set
	 */
	public static void setSideMission(SideMissionType type, Mission mission){
		main.getConfig().set("sidemissions." + type.toString().toLowerCase() + ".mission", mission.getID());
		main.saveConfig();
	}
	
	/**
	 * Get the side mission for the type
	 * @param type - The side mission type to get
	 * @return The side mission from the side mission type
	 */
	public static Mission getSideMission(SideMissionType type){
		Mission mission = null;
		for(Mission m : Mission.values()){
			if(m.getID() == main.getConfig().getInt("sidemissions." + type.toString().toLowerCase() + ".mission")){
				mission = m;
				break;
			}
		}
		return mission;
	}
}
