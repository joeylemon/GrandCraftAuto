package com.grandcraftauto.game.districts;

import org.bukkit.Location;

import com.grandcraftauto.core.Main;

public class DistrictUtils {
	
	static Main main = Main.getInstance();
	
	public static final int getTotalDistricts(){
		return main.getConfig().getInt("districts.total");
	}
	
	/**
	 * Create a new district
	 * @param name - The name of the district
	 * @param firstPos - The first position of the district
	 * @param secondPos - The second position of the district
	 */
	public static final void addDistrict(String name, Location firstPos, Location secondPos){
		int id = getTotalDistricts() + 1;
		main.getConfig().set("districts.total", id);
		main.getConfig().set("districts." + id + ".name", name);
		main.getConfig().set("districts." + id + ".firstPos.x", firstPos.getX());
		main.getConfig().set("districts." + id + ".firstPos.y", firstPos.getY());
		main.getConfig().set("districts." + id + ".firstPos.z", firstPos.getZ());
		main.getConfig().set("districts." + id + ".secondPos.x", secondPos.getX());
		main.getConfig().set("districts." + id + ".secondPos.y", secondPos.getY());
		main.getConfig().set("districts." + id + ".secondPos.z", secondPos.getZ());
		main.saveConfig();
	}
	
	/**
	 * Get the district at the location
	 * @param loc - The location to check for a district
	 * @return The district at the location (returns null if there is no district)
	 */
	public static final District getDistrict(Location loc){
		District dis = null;
		for(int x = 1; x <= getTotalDistricts(); x++){
			District d = new District(x);
			if(loc.getX() >= d.getLowestX() && loc.getX() <= d.getHighestX() && loc.getZ() >= d.getLowestZ() && loc.getZ() <= d.getHighestZ()){
				dis = d;
				break;
			}
		}
		return dis;
	}
}
