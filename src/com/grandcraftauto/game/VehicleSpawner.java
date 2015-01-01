package com.grandcraftauto.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Minecart;
import org.bukkit.inventory.ItemStack;

import com.grandcraftauto.core.Main;
import com.grandcraftauto.game.cars.Car;
import com.grandcraftauto.utils.Utils;

public class VehicleSpawner {
	
static Main main = Main.getInstance();
	
	/**
	 * Spawn the vehicles at the saved locations
	 */
	public static final void spawnVehicles(){
		for(VehicleSpawn s : getLocationsToSpawn()){
			Minecart cart = (Minecart) Utils.getGCAWorld().spawnEntity(s.getLocation().add(0.5, 2, 0.5), EntityType.MINECART);
			if(s.getCar() != null){
				Utils.setMetadata(cart, "car", s.getCar().getName());
			}
			if(s.getCarOwner() != null){
				Utils.setMetadata(cart, "owner", s.getCarOwner());
			}
			Utils.setMetadata(cart, "locked", s.getCarLocked());
		}
	}
	
	/**
	 * Remove all vehicles spawned by the item spawner
	 */
	public static final void removeVehicles(){
		for(VehicleSpawn s : getLocationsToSpawn()){
			Item i = Utils.getGCAWorld().dropItem(s.getLocation(), new ItemStack(Material.CLAY_BALL));
			i.setPickupDelay(1000);
			for(Entity e : i.getNearbyEntities(2, 2, 2)){
				if(e instanceof Minecart){
					e.remove();
				}
			}
			i.remove();
		}
	}
	
	/**
	 * Refresh the vehicles
	 */
	public static final void refreshVehicles(){
		removeVehicles();
		spawnVehicles();
	}
	
	/**
	 * Get the list of locations to spawn vehicles at the given ID
	 * @param spawnID - The id of the spawn
	 * @return The list of locations
	 */
	private static List<VehicleSpawn> getLocationsToSpawn(){
		List<VehicleSpawn> spawns = new ArrayList<VehicleSpawn>();
		for(int i = 1; i <= main.getConfig().getInt("vehiclespawns.total"); i++){
			double x,y,z;
			x = main.getConfig().getInt("vehiclespawns.spawns." + i + ".x");
			y = main.getConfig().getInt("vehiclespawns.spawns." + i + ".y");
			z = main.getConfig().getInt("vehiclespawns.spawns." + i + ".z");
			Location loc = new Location(Utils.getGCAWorld(), x, y, z);
			Car car = null;
			if(main.getConfig().getString("vehiclespawns.spawns." + i + ".cartype") != null){
				car = Car.valueOf(main.getConfig().getString("vehiclespawns.spawns." + i + ".cartype").toUpperCase());
			}
			String carOwner = null;
			if(main.getConfig().getString("vehiclespawns.spawns." + i + ".carOwner") != null){
				carOwner = main.getConfig().getString("vehiclespawns.spawns." + i + ".carOwner");
			}
			boolean carLocked = main.getConfig().getBoolean("vehiclespawns.spawns." + i + ".carLocked");
			spawns.add(new VehicleSpawn(loc, car, carOwner, carLocked));
		}
		return spawns;
	}
	
	/**
	 * Add a location to spawn a vehicle at the given ID
	 * @param spawnID - The spawn ID to add a spawn point to
	 * @param loc - The location of where the vehicle will spawn
	 */
	public static final void addLocationToSpawn(Location loc, Car car, String owner, boolean locked){
		int spawnNumber = main.getConfig().getInt("vehiclespawns.total") + 1;
		main.getConfig().set("vehiclespawns.total", spawnNumber);
		main.getConfig().set("vehiclespawns.spawns." + spawnNumber + ".x", loc.getX());
		main.getConfig().set("vehiclespawns.spawns." + spawnNumber + ".y", loc.getY());
		main.getConfig().set("vehiclespawns.spawns." + spawnNumber + ".z", loc.getZ());
		if(car != null){
			main.getConfig().set("vehiclespawns.spawns." + spawnNumber + ".cartype", car.toString().toLowerCase());
		}else{
			main.getConfig().set("vehiclespawns.spawns." + spawnNumber + ".cartype", null);
		}
		main.getConfig().set("vehiclespawns.spawns." + spawnNumber + ".carOwner", owner);
		main.getConfig().set("vehiclespawns.spawns." + spawnNumber + ".carLocked", locked);
		main.saveConfig();
	}

}
