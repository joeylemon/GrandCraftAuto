package com.grandcraftauto.game.garage;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.grandcraftauto.core.Main;
import com.grandcraftauto.utils.Utils;

public class Garage {
	
	static Main main = Main.getInstance();
	
	private int id;
	public Garage(int garageID){
		id = garageID;
	}
	
	/**
	 * Get the ID of the garage
	 * @return The ID of the garage
	 */
	public int getID(){
		return id;
	}
	
	/**
	 * Get the center location of the garage
	 * @return The center location of the garage
	 */
	public Location getLocation(){
		double x,y,z;
		x = main.getConfig().getDouble("garages." + id + ".location.x");
		y = main.getConfig().getDouble("garages." + id + ".location.y");
		z = main.getConfig().getDouble("garages." + id + ".location.z");
		return new Location(Utils.getGCAWorld(), x, y, z);
	}
	
	/**
	 * Set the center location of the garage
	 * @param loc - The location to set for the garage
	 */
	public void setLocation(Location loc){
		main.getConfig().set("garages." + id + ".location.x", loc.getX());
		main.getConfig().set("garages." + id + ".location.y", loc.getY());
		main.getConfig().set("garages." + id + ".location.z", loc.getZ());
		main.saveConfig();
	}
	
	/**
	 * Get the total amount of car spawns
	 * @return The total amount of car spawns
	 */
	public int getTotalCarSpawns(){
		return main.getConfig().getInt("garages." + id + ".totalcarspawns");
	}
	
	/**
	 * Set the total amount of car spawns
	 * @param total - The new total amount of car spawns
	 */
	public void setTotalCarSpawns(int total){
		main.getConfig().set("garages." + id + ".totalcarspawns", total);
		main.saveConfig();
	}
	
	/**
	 * Get the list of car spawns
	 * @return A list of locations where cars should spawn
	 */
	public List<Location> getCarSpawns(){
		List<Location> spawns = new ArrayList<Location>();
		for(int i = 1; i <= this.getTotalCarSpawns(); i++){
			double x,y,z;
			x = main.getConfig().getDouble("garages." + id + ".carspawns." + i + ".x");
			y = main.getConfig().getDouble("garages." + id + ".carspawns." + i + ".y");
			z = main.getConfig().getDouble("garages." + id + ".carspawns." + i + ".z");
			spawns.add(new Location(Utils.getGCAWorld(), x, y, z));
		}
		return spawns;
	}
	
	/**
	 * Add a car spawn to the garage
	 * @param loc - The location of the car spawn to add
	 */
	public void addCarSpawn(Location loc){
		int spawn = this.getTotalCarSpawns() + 1;
		this.setTotalCarSpawns(spawn);
		main.getConfig().set("garages." + id + ".carspawns." + spawn + ".x", loc.getX());
		main.getConfig().set("garages." + id + ".carspawns." + spawn + ".y", loc.getY());
		main.getConfig().set("garages." + id + ".carspawns." + spawn + ".z", loc.getZ());
		main.saveConfig();
	}
	
	/**
	 * Get the total amount of garages
	 * @return The total amount of garages
	 */
	public static final int getTotalGarages(){
		return main.getConfig().getInt("garages.totalgarages");
	}
	
	/**
	 * Set the total amount of garages
	 * @param total - The new total amount of garages
	 */
	public static final void setTotalGarages(int total){
		main.getConfig().set("garages.totalgarages", total);
		main.saveConfig();
	}
	
	/**
	 * Get the garage list
	 * @return The garage list
	 */
	public static final List<Garage> list(){
		List<Garage> garages = new ArrayList<Garage>();
		for(int x = 1; x <= getTotalGarages(); x++){
			garages.add(new Garage(x));
		}
		return garages;
	}
}
