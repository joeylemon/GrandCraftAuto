package com.grandcraftauto.game.apartment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.grandcraftauto.core.Main;
import com.grandcraftauto.utils.Utils;

public class Apartment {
	
	private static Main main = Main.getInstance();
	private static List<Apartment> apartments = new ArrayList<Apartment>();
	
	private int id;
	
	public Apartment(int aptID){
		id = aptID;
	}
	
	/**
	 * Get the apartment's file
	 */
	public File getFile(){
		return new File(main.getDataFolder() + File.separator + "apartments", id + ".yml");
	}
	
	/**
	 * Get the apartment's config
	 */
	public FileConfiguration getConfig(){
		return YamlConfiguration.loadConfiguration(getFile());
	}
	
	/**
	 * Set a value in the apartment's config
	 * 
	 * @param key - The location of the value to set
	 * @param entry - The value to set
	 */
	public void setConfigValue(String key, Object entry){
		FileConfiguration fc = getConfig();
	    fc.set(key, entry);
	    try{
	      fc.save(getFile());
	    }catch (IOException e) {
	      e.printStackTrace();
	    }
	}
	
	/**
	 * Set a value in the apartment's config
	 * 
	 * @param keyAndEntry - The list of keys and entries to set
	 */
	public void setConfigValue(String... keyAndEntry){
		FileConfiguration fc = getConfig();
		for(String s : keyAndEntry){
			String split[] = s.split("\\|");
			if(Utils.isDouble(split[1]) == false){
			    fc.set(split[0], split[1]);
			}else{
			    fc.set(split[0], Double.parseDouble(split[1]));
			}
		    try{
		      fc.save(getFile());
		    }catch (IOException e) {
		      e.printStackTrace();
		    }
		}
	}
	
	/**
	 * Get the ID of the apartment
	 * @return The ID of the apartment
	 */
	public int getID(){
		return id;
	}
	
	/**
	 * Get the name of the apartment
	 * @return The name of the apartment
	 */
	public String getName(){
		return this.getConfig().getString("name");
	}
	
	/**
	 * Set the name of the apartment
	 * @param name - The name to set the apartment as
	 */
	public void setName(String name){
		this.setConfigValue("name", name);
	}
	
	/**
	 * Get where a player should spawn if they own this apartment
	 * @return The location of the apartment's spawn point
	 */
	public Location getSpawn(){
		double x,y,z;
		x = this.getConfig().getDouble("spawn.x");
		y = this.getConfig().getDouble("spawn.y");
		z = this.getConfig().getDouble("spawn.z");
		int yaw,pitch;
		yaw = this.getConfig().getInt("spawn.yaw");
		pitch = this.getConfig().getInt("spawn.pitch");
		return new Location(Utils.getGCAWorld(), x, y, z, yaw, pitch);
	}
	
	/**
	 * Set where a player should spawn if they own this apartment
	 * @param loc - The location to set as the apartment's spawn point
	 */
	public void setSpawn(Location loc){
		this.setConfigValue("spawn.x|" + loc.getX(), "spawn.y|" + loc.getY(), "spawn.z|" + loc.getZ(), "spawn.yaw|" + loc.getYaw(), "spawn.pitch|" + loc.getPitch());
	}
	
	/**
	 * Get where the apartment's purchase sign is
	 * @return The location of the apartment's purchase sign
	 */
	public Location getSignLocation(){
		double x,y,z;
		x = this.getConfig().getDouble("sign.x");
		y = this.getConfig().getDouble("sign.y");
		z = this.getConfig().getDouble("sign.z");
		return new Location(Utils.getGCAWorld(), x, y, z);
	}
	
	/**
	 * Set where the apartment's purchase sign is
	 * @param loc - The location to set as the apartment's purchase sign
	 */
	public void setSignLocation(Location loc){
		this.setConfigValue("sign.x|" + loc.getX(), "sign.y|" + loc.getY(), "sign.z|" + loc.getZ());
	}
	
	/**
	 * Get the price of the apartment
	 * @return The price of the apartment
	 */
	public int getPrice(){
		return this.getConfig().getInt("price");
	}
	
	/**
	 * Set the price of the apartment
	 * @param price - The price of the apartment to set
	 */
	public void setPrice(int price){
		this.setConfigValue("price", price);
	}
	
	/**
	 * Get the rent price of the apartment
	 * @return The rent price of the apartment
	 */
	public int getRent(){
		return this.getConfig().getInt("rent");
	}
	
	/**
	 * Set the rent price of the apartment
	 * @param rent - The rent price of the apartment to set
	 */
	public void setRent(int rent){
		this.setConfigValue("rent", rent);
	}
	
	/**
	 * Get the total chests of the apartment
	 * @return The total chests of the apartment
	 */
	public int getTotalChests(){
		return this.getConfig().getInt("totalchests");
	}
	
	/**
	 * Set the total chests of the apartment
	 * @param total - The amount of chests to set as the apartment's total
	 */
	public void setTotalChests(int total){
		this.setConfigValue("totalchests", total);
	}
	
	/**
	 * Get a list of the apartment's chest locations
	 * @return A list of locations where the apartment's chests are
	 */
	public List<Location> getChestLocations(){
		List<Location> chests = new ArrayList<Location>();
		for(int c = 1; c <= this.getTotalChests(); c++){
			double x,y,z;
			x = this.getConfig().getDouble("chests." + c + ".location.x");
			y = this.getConfig().getDouble("chests." + c + ".location.y");
			z = this.getConfig().getDouble("chests." + c + ".location.z");
			chests.add(new Location(Utils.getGCAWorld(), x, y, z));
		}
		return chests;
	}
	
	/**
	 * Add a chest to the apartment
	 * @param loc - The location of the chest to add to the apartment
	 */
	public void addChestLocation(Location loc){
		int chest = this.getTotalChests() + 1;
		this.setTotalChests(chest);
		this.setConfigValue("chests." + chest + ".location.x", loc.getX());
		this.setConfigValue("chests." + chest + ".location.y", loc.getY());
		this.setConfigValue("chests." + chest + ".location.z", loc.getZ());
	}
	
	/**
	 * Get the total doors of the apartment
	 * @return The total doors of the apartment
	 */
	public int getTotalDoors(){
		return this.getConfig().getInt("totaldoors");
	}
	
	/**
	 * Set the total doors of the apartment
	 * @param total - The amount of doors to set as the apartment's total
	 */
	public void setTotalDoors(int total){
		this.setConfigValue("totaldoors", total);
	}
	
	/**
	 * Get a list of the apartment's door locations
	 * @return A list of locations where the apartment's doors are
	 */
	public List<Location> getDoorLocations(){
		List<Location> doors = new ArrayList<Location>();
		for(int d = 1; d <= this.getTotalDoors(); d++){
			double x,y,z;
			x = this.getConfig().getDouble("doors." + d + ".location.x");
			y = this.getConfig().getDouble("doors." + d + ".location.y");
			z = this.getConfig().getDouble("doors." + d + ".location.z");
			doors.add(new Location(Utils.getGCAWorld(), x, y, z));
		}
		return doors;
	}
	
	/**
	 * Add a door to the apartment
	 * @param loc - The location of the door to add to the apartment
	 */
	public void addDoorLocation(Location loc){
		int door = this.getTotalDoors() + 1;
		this.setTotalDoors(door);
		this.setConfigValue("doors." + door + ".location.x", loc.getX());
		this.setConfigValue("doors." + door + ".location.y", loc.getY());
		this.setConfigValue("doors." + door + ".location.z", loc.getZ());
	}
	
	/**
	 * Get the total amount of apartments
	 * @return The total amount of apartments
	 */
	public static final int getTotalApartments(){
		File folder = new File(main.getDataFolder() + File.separator + "apartments");
		if(folder.exists() == true){
			try{
				return folder.listFiles().length;
			}catch (Exception ex){
				return 0;
			}
		}else{
			return 0;
		}
	}
	
	/**
	 * Initialize the apartment list for usage
	 */
	public static final void initializeList(){
		for(int x = 1; x <= getTotalApartments(); x++){
			apartments.add(new Apartment(x));
		}
	}
	
	/**
	 * Get the list of apartments
	 * @return The list of apartments
	 */
	public static final List<Apartment> list(){
		return apartments;
	}
}
