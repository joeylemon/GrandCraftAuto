package gca.game;

import gca.core.Main;
import gca.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.util.Vector;

public class ItemSpawner {
	
	static Main main = Main.getInstance();
	
	private static List<Item> items = new ArrayList<Item>();
	
	/**
	 * Spawn the items at the saved locations
	 */
	public static final void spawnItems(){
		/*
		 * Cobra Drugs
		 */
		for(Location l : getLocationsToSpawn(1)){
			Item item = Utils.getGCAWorld().dropItem(l.add(0.5, 2, 0.5), Utils.getCocaineItem(Utils.randInt(1, 3)));
			item.setVelocity(new Vector(0.0D, 0.1D, 0.0D));
			items.add(item);
		}
	}
	
	/**
	 * Remove all items spawned by the ItemSpawner
	 */
	public static final void removeItems(){
		for(Item i : items){
			i.remove();
		}
	}
	
	/**
	 * Refresh the items
	 */
	public static final void refreshItems(){
		removeItems();
		spawnItems();
	}
	
	/**
	 * Get the list of locations to spawn items at the given ID
	 * @param spawnID - The id of the spawn
	 * @return The list of locations
	 */
	private static List<Location> getLocationsToSpawn(int spawnID){
		List<Location> locations = new ArrayList<Location>();
		for(int i = 1; i <= main.getConfig().getInt("itemspawns." + spawnID + ".total"); i++){
			double x,y,z;
			x = main.getConfig().getInt("itemspawns." + spawnID + ".spawns." + i + ".x");
			y = main.getConfig().getInt("itemspawns." + spawnID + ".spawns." + i + ".y");
			z = main.getConfig().getInt("itemspawns." + spawnID + ".spawns." + i + ".z");
			locations.add(new Location(Utils.getGCAWorld(), x, y, z));
		}
		return locations;
	}
	
	/**
	 * Add a location to spawn an item at the given ID
	 * @param spawnID - The spawn ID to add a spawn point to
	 * @param loc - The location of where the item will spawn
	 */
	public static final void addLocationToSpawn(int spawnID, Location loc){
		int spawnNumber = main.getConfig().getInt("itemspawns." + spawnID + ".total") + 1;
		main.getConfig().set("itemspawns." + spawnID + ".total", spawnNumber);
		main.getConfig().set("itemspawns." + spawnID + ".spawns." + spawnNumber + ".x", loc.getX());
		main.getConfig().set("itemspawns." + spawnID + ".spawns." + spawnNumber + ".y", loc.getY());
		main.getConfig().set("itemspawns." + spawnID + ".spawns." + spawnNumber + ".z", loc.getZ());
		main.saveConfig();
	}
}
