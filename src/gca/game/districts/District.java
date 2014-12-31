package gca.game.districts;

import gca.core.Main;
import gca.utils.Utils;

import org.bukkit.Location;

public class District {
	
	Main main = Main.getInstance();
	
	private int id;
	public District(int id){
		this.id = id;
	}
	
	/**
	 * Get the ID of the district
	 * @return The ID of the district
	 */
	public int getID(){
		return id;
	}
	
	/**
	 * Get the name of the district
	 * @return The name of the district
	 */
	public String getName(){
		return main.getConfig().getString("districts." + id + ".name");
	}
	
	/**
	 * Get the first position of the district
	 * @return The first position of the district
	 */
	public Location getFirstPosition(){
		double x,y,z;
		x = main.getConfig().getDouble("districts." + id + ".firstPos.x");
		y = main.getConfig().getDouble("districts." + id + ".firstPos.y");
		z = main.getConfig().getDouble("districts." + id + ".firstPos.z");
		return new Location(Utils.getGCAWorld(), x, y, z);
	}
	
	/**
	 * Get the second position of the district
	 * @return The second position of the district
	 */
	public Location getSecondPosition(){
		double x,y,z;
		x = main.getConfig().getDouble("districts." + id + ".secondPos.x");
		y = main.getConfig().getDouble("districts." + id + ".secondPos.y");
		z = main.getConfig().getDouble("districts." + id + ".secondPos.z");
		return new Location(Utils.getGCAWorld(), x, y, z);
	}
	
	/**
	 * Get the highest X value of the district
	 * @return The highest X value of the district
	 */
	public double getHighestX(){
		double highest = 0;
		Location loc1 = this.getFirstPosition();
		Location loc2 = this.getSecondPosition();
		if(loc1.getX() > loc2.getX()){
			highest = loc1.getX();
		}else if(loc2.getX() > loc1.getX()){
			highest = loc2.getX();
		}
		return highest;
	}
	
	/**
	 * Get the lowest X value of the district
	 * @return The lowest X value of the district
	 */
	public double getLowestX(){
		double lowest = 0;
		Location loc1 = this.getFirstPosition();
		Location loc2 = this.getSecondPosition();
		if(loc1.getX() < loc2.getX()){
			lowest = loc1.getX();
		}else if(loc2.getX() < loc1.getX()){
			lowest = loc2.getX();
		}
		return lowest;
	}
	
	/**
	 * Get the highest Z value of the district
	 * @return The highest Z value of the district
	 */
	public double getHighestZ(){
		double highest = 0;
		Location loc1 = this.getFirstPosition();
		Location loc2 = this.getSecondPosition();
		if(loc1.getZ() > loc2.getZ()){
			highest = loc1.getZ();
		}else if(loc2.getZ() > loc1.getZ()){
			highest = loc2.getZ();
		}
		return highest;
	}
	
	/**
	 * Get the lowest Z value of the district
	 * @return The lowest Z value of the district
	 */
	public double getLowestZ(){
		double lowest = 0;
		Location loc1 = this.getFirstPosition();
		Location loc2 = this.getSecondPosition();
		if(loc1.getZ() < loc2.getZ()){
			lowest = loc1.getZ();
		}else if(loc2.getZ() < loc1.getZ()){
			lowest = loc2.getZ();
		}
		return lowest;
	}
}
