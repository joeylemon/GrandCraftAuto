package gca.game;

import gca.game.cars.Car;

import org.bukkit.Location;

public class VehicleSpawn {
	
	private Location loc;
	private Car car;
	private String owner;
	private boolean locked;
	
	/**
	 * Create a new vehicle spawn
	 * @param spawnLoc - The location of the spawn
	 * @param spawnType - The car type to be spawned
	 */
	public VehicleSpawn(Location spawnLoc, Car spawnCar, String spawnOwner, boolean spawnLocked){
		loc = spawnLoc;
		car = spawnCar;
		owner = spawnOwner;
		locked = spawnLocked;
	}
	
	/**
	 * Get the location of the spawn
	 * @return The location of the spawn
	 */
	public Location getLocation(){
		return loc;
	}
	
	/**
	 * Get the car type of the spawn
	 * @return The car type of the spawn
	 */
	public Car getCar(){
		return car;
	}
	
	/**
	 * Get the owner of the car
	 * @return The owner of the car
	 */
	public String getCarOwner(){
		return owner;
	}
	
	/**
	 * Get if the car should be locked or not
	 * @return True or false depending on if the car should be locked or not
	 */
	public boolean getCarLocked(){
		return locked;
	}
}
