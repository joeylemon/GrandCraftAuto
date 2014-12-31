package gca.game.jobs;

import gca.utils.Utils;

import org.bukkit.Location;

public class FreeForAll extends Job {

	public FreeForAll(int id){
		super(id);
	}
	
	public int getMinimumPlayers(){
		return 2;
	}
	
	public int getMaximumPlayers(){
		return 10;
	}
	
	public String getType(){
		return "Free for All";
	}
	
	public boolean hasTeams() {
		return false;
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
	public void setStartingLocation(int pos, Location loc){
		this.getFile().setConfigValue("startingLocations." + pos + ".x", loc.getX());
		this.getFile().setConfigValue("startingLocations." + pos + ".y", loc.getY());
		this.getFile().setConfigValue("startingLocations." + pos + ".z", loc.getZ());
		this.getFile().setConfigValue("startingLocations." + pos + ".yaw", loc.getYaw());
		this.getFile().setConfigValue("startingLocations." + pos + ".pitch", loc.getPitch());
	}
	
	/**
	 * Get a starting location
	 * @param pos - The id of the starting location
	 * @return The starting location
	 */
	public Location getStartingLocation(int pos){
		double x,y,z;
		x = this.getFile().getConfig().getDouble("startingLocations." + pos + ".x");
		y = this.getFile().getConfig().getDouble("startingLocations." + pos + ".y");
		z = this.getFile().getConfig().getDouble("startingLocations." + pos + ".z");
		int yaw,pitch;
		yaw = this.getFile().getConfig().getInt("startingLocations." + pos + ".yaw");
		pitch = this.getFile().getConfig().getInt("startingLocations." + pos + ".pitch");
		return new Location(Utils.getGCAWorld(), x, y, z, yaw, pitch);
	}
	
	/**
	 * Get a random starting location
	 * @return A random starting location
	 */
	public Location getRandomStartingLocation(){
		return this.getStartingLocation(Utils.randInt(1, this.getTotalStartingLocations()));
	}
	
	/**
	 * Set the total starting locations
	 * @param value - The total starting locations
	 */
	public void setTotalStartingLocations(int value){
		this.getFile().setConfigValue("startingLocations.total", value);
	}
	
	/**
	 * Get the total starting locations
	 * @return The total starting locations
	 */
	public int getTotalStartingLocations(){
		return this.getFile().getConfig().getInt("startingLocations.total");
	}
	
	/**
	 * Add a starting location
	 * @param loc - The starting location to add
	 */
	public void addStartingLocation(Location loc){
		int pos = this.getTotalStartingLocations() + 1;
		this.setStartingLocation(pos, loc);
		this.setTotalStartingLocations(pos);
	}
}
