package gca.game.garage;

import java.util.List;

import org.bukkit.entity.Entity;

import com.gmail.filoghost.holograms.api.Hologram;

public class GarageInstance {
	
	private Garage garage;
	private List<Entity> cars;
	private List<Hologram> holograms;
	
	/**
	 * Create an instance of a garage
	 * @param instanceGarage - The garage
	 * @param instanceCars - The list of cars in the instance
	 * @param instanceCars - The list of holograms in the instance
	 */
	public GarageInstance(Garage instanceGarage, List<Entity> instanceCars, List<Hologram> instanceHolograms){
		garage = instanceGarage;
		cars = instanceCars;
		holograms = instanceHolograms;
	}
	
	/**
	 * Get the instance's garage
	 * @return The instance's garage
	 */
	public Garage getGarage(){
		return garage;
	}
	
	/**
	 * Get the instance's list of cars
	 * @return The instance's list of cars
	 */
	public List<Entity> getCars(){
		return cars;
	}
	
	/**
	 * Get the instance's list of holograms
	 * @return The instance's list of holograms
	 */
	public List<Hologram> getHolograms(){
		return holograms;
	}
	
	/**
	 * Clear everything in the instance
	 */
	public void clearInstance(){
		for(Entity e : this.getCars()){
			e.remove();
		}
		for(Hologram h : this.getHolograms()){
			h.delete();
		}
	}
}
