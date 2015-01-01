package com.grandcraftauto.game.missions;

import org.bukkit.Location;

import com.grandcraftauto.core.Main;
import com.grandcraftauto.utils.Utils;

public class MissionUtils {
	
	static Main main = Main.getInstance();
	
	/**
	 * Set a mission location
	 * @param id - The mission location's identifier
	 * @param loc - The location to set
	 */
	public static final void setMissionLocation(String id, Location loc){
		main.getConfig().set("missions.locations." + id + ".x", loc.getX());
		main.getConfig().set("missions.locations." + id + ".y", loc.getY());
		main.getConfig().set("missions.locations." + id + ".z", loc.getZ());
		main.saveConfig();
	}
	
	/**
	 * Get a mission location
	 * @param id - The mission location's identifier
	 * @return The mission location
	 */
	public static final Location getMissionLocation(String id){
		double x,y,z;
		x = main.getConfig().getDouble("missions.locations." + id + ".x");
		y = main.getConfig().getDouble("missions.locations." + id + ".y");
		z = main.getConfig().getDouble("missions.locations." + id + ".z");
		
		return new Location(Utils.getGCAWorld(), x, y, z);
	}

}
