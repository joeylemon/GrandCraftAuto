package com.grandcraftauto.game.jobs;

import org.bukkit.Location;

import com.grandcraftauto.utils.Utils;

public class TeamDeathmatch extends Job {

	public TeamDeathmatch(int id){
		super(id);
	}
	
	public int getMinimumPlayers(){
		return 2;
	}
	
	public int getMaximumPlayers(){
		return 10;
	}
	
	public String getType(){
		return "Team Deathmatch";
	}
	
	public boolean hasTeams() {
		return true;
	}
	
	/**
	 * Set the ffa's kills required
	 * @param value - The ffa's kills required
	 */
	public void setKillsRequired(int value){
		this.getFile().setConfigValue("killsRequired", value);
	}
	
	/**
	 * Get the ffa's kills required
	 * @return The ffa's kills required
	 */
	public int getKillsRequired(){
		return this.getFile().getConfig().getInt("killsRequired");
	}
	
	/**
	 * Set a starting location
	 * @param pos - The id of the starting location
	 * @param loc - The starting location
	 */
	public void setStartingLocation(int team, int pos, Location loc){
		this.getFile().setConfigValue("team." + team + ".startingLocations." + pos + ".x", loc.getX());
		this.getFile().setConfigValue("team." + team + ".startingLocations." + pos + ".y", loc.getY());
		this.getFile().setConfigValue("team." + team + ".startingLocations." + pos + ".z", loc.getZ());
		this.getFile().setConfigValue("team." + team + ".startingLocations." + pos + ".yaw", loc.getYaw());
		this.getFile().setConfigValue("team." + team + ".startingLocations." + pos + ".pitch", loc.getPitch());
	}
	
	/**
	 * Get a starting location
	 * @param pos - The id of the starting location
	 * @return The starting location
	 */
	public Location getStartingLocation(int team, int pos){
		double x,y,z;
		x = this.getFile().getConfig().getDouble("team." + team + ".startingLocations." + pos + ".x");
		y = this.getFile().getConfig().getDouble("team." + team + ".startingLocations." + pos + ".y");
		z = this.getFile().getConfig().getDouble("team." + team + ".startingLocations." + pos + ".z");
		int yaw,pitch;
		yaw = this.getFile().getConfig().getInt("team." + team + ".startingLocations." + pos + ".yaw");
		pitch = this.getFile().getConfig().getInt("team." + team + ".startingLocations." + pos + ".pitch");
		return new Location(Utils.getGCAWorld(), x, y, z, yaw, pitch);
	}
	
	/**
	 * Get a random starting location
	 * @return A random starting location
	 */
	public Location getRandomStartingLocation(int team){
		return this.getStartingLocation(team, Utils.randInt(1, this.getTotalStartingLocations(team)));
	}
	
	/**
	 * Set the total starting locations
	 * @param value - The total starting locations
	 */
	public void setTotalStartingLocations(int team, int value){
		this.getFile().setConfigValue("team." + team + ".startingLocations.total", value);
	}
	
	/**
	 * Get the total starting locations
	 * @return The total starting locations
	 */
	public int getTotalStartingLocations(int team){
		return this.getFile().getConfig().getInt("team." + team + ".startingLocations.total");
	}
	
	/**
	 * Add a starting location
	 * @param loc - The starting location to add
	 */
	public void addStartingLocation(int team, Location loc){
		int pos = this.getTotalStartingLocations(team) + 1;
		this.setStartingLocation(team, pos, loc);
		this.setTotalStartingLocations(team, pos);
	}
}
