package gca.game.jobs;

import gca.game.cars.Car;
import gca.utils.Utils;

import org.bukkit.Location;

public class Race extends Job {

	public Race(int id){
		super(id);
	}
	
	public int getMinimumPlayers(){
		return 2;
	}
	
	public int getMaximumPlayers(){
		return 6;
	}
	
	public String getType(){
		return "Race";
	}
	
	public boolean hasTeams() {
		return false;
	}
	
	/**
	 * Set the race's laps
	 * @param value - The race's laps
	 */
	public void setLaps(int value){
		this.getFile().setConfigValue("laps", value);
	}
	
	/**
	 * Get the race's laps
	 * @return The race's laps
	 */
	public int getLaps(){
		return this.getFile().getConfig().getInt("laps");
	}
	
	/**
	 * Set the race's car
	 * @param value - The race's car
	 */
	public void setCar(Car car){
		this.getFile().setConfigValue("car", car.getName());
	}
	
	/**
	 * Get the race's car
	 * @return The race's car
	 */
	public Car getCar(){
		Car car = null;
		for(Car c : Car.values()){
			if(c.getName().equalsIgnoreCase(this.getFile().getConfig().getString("car"))){
				car = c;
				break;
			}
		}
		return car;
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
	
	/**
	 * Set a starting location
	 * @param pos - The id of the starting location
	 * @param loc - The starting location
	 */
	public void setCheckpoint(int pos, Location loc){
		this.getFile().setConfigValue("checkpoints." + pos + ".x", loc.getX());
		this.getFile().setConfigValue("checkpoints." + pos + ".y", loc.getY());
		this.getFile().setConfigValue("checkpoints." + pos + ".z", loc.getZ());
	}
	
	/**
	 * Get a starting location
	 * @param pos - The id of the starting location
	 * @return The starting location
	 */
	public Location getCheckpoint(int pos){
		double x,y,z;
		x = this.getFile().getConfig().getDouble("checkpoints." + pos + ".x");
		y = this.getFile().getConfig().getDouble("checkpoints." + pos + ".y");
		z = this.getFile().getConfig().getDouble("checkpoints." + pos + ".z");
		return new Location(Utils.getGCAWorld(), x, y, z);
	}
	
	/**
	 * Set the total starting locations
	 * @param value - The total starting locations
	 */
	public void setTotalCheckpoints(int value){
		this.getFile().setConfigValue("checkpoints.total", value);
	}
	
	/**
	 * Get the total starting locations
	 * @return The total starting locations
	 */
	public int getTotalCheckpoints(){
		return this.getFile().getConfig().getInt("checkpoints.total");
	}
	
	/**
	 * Add a starting location
	 * @param loc - The starting location to add
	 */
	public void addCheckpoint(Location loc){
		int pos = this.getTotalCheckpoints() + 1;
		this.setCheckpoint(pos, loc);
		this.setTotalCheckpoints(pos);
	}
}
