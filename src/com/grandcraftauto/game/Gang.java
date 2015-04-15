package com.grandcraftauto.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.grandcraftauto.core.Main;
import com.grandcraftauto.utils.ObjectType;
import com.grandcraftauto.utils.Utils;

public enum Gang {
	
	COBRA("Cobra", VillagerType.GANG_MEMBER, ChatColor.GOLD + "Cobra Member", 7),
	HOODLUM("Hoodlum", VillagerType.GANG_MEMBER, ChatColor.GOLD + "Hoodlum", 9),
	KIDNAPPER("Kidnapper", VillagerType.GANG_MEMBER, ChatColor.RED + "Kidnapper", 1),
	DRUGDEALER("DrugDealer", VillagerType.GANG_MEMBER, ChatColor.RED + "Drug Dealer", 1),
	SNITCH("Snitch", VillagerType.GANG_MEMBER, ChatColor.RED + "Snitch", 1),
	THUG("Thug", VillagerType.GANG_MEMBER, ChatColor.RED + "Thug", 3),
	JUDGE("Judge", VillagerType.GANG_MEMBER, ChatColor.RED + "Judge", 1),
	BODYGUARD("Bodyguard", VillagerType.GANG_MEMBER, ChatColor.RED + "Bodyguard", 6),
	PROSTITUTE("Prostitute", VillagerType.PROSTITUTE, ChatColor.LIGHT_PURPLE + "Prostitute", 4);
	
	Main main = Main.getInstance();
	static List<Gang> gangs = new ArrayList<Gang>();
	
	public List<Integer> members = new ArrayList<Integer>();
	private String name;
	private VillagerType type;
	private String tag;
	private int maxAlive;
	
	/**
	 * Create a gang
	 * @param gangName - The name of the gang
	 * @param gangHideoutLoc - The location of the gang's hideout
	 */
	Gang(String name, VillagerType type, String tag, int maxAlive){
		this.name = name;
		this.type = type;
		this.tag = tag;
		this.maxAlive = maxAlive;
	}
	
	/**
	 * Get the gang's name
	 * @return The gang's name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Get the gang's villager type
	 * @return The gang's villager type
	 */
	public VillagerType getVillagerType(){
		return type;
	}
	
	/**
	 * Get the gang's tag
	 * @return The gang's tag
	 */
	public String getTag(){
		return tag;
	}
	
	/**
	 * Return the max amount of members to be alive at a time
	 * @return The max amount of members to be alive at a time
	 */
	public int getMaxAliveMembers(){
		return maxAlive;
	}
	
	/**
	 * Get the gang's total hideout locations
	 * @return The gang's total hideout locations
	 */
	public int getTotalHideoutLocations(){
		return main.getConfig().getInt("gangs." + this.getName() + ".hideouts.total");
	}
	
	/**
	 * Set the gang's total hideout locations
	 * @param total - The gang's total hideout locations
	 */
	public void setTotalHideoutLocations(int total){
		main.getConfig().set("gangs." + this.getName() + ".hideouts.total", total);
		main.saveConfig();
	}
	
	/**
	 * Get the gang's hideout location
	 * @return The gang's hideout location
	 */
	public Location getHideoutLocation(int id){
		double x,y,z;
		x = main.getConfig().getDouble("gangs." + this.getName() + ".hideouts." + id + ".x");
		y = main.getConfig().getDouble("gangs." + this.getName() + ".hideouts." + id + ".y");
		z = main.getConfig().getDouble("gangs." + this.getName() + ".hideouts." + id + ".z");
		return new Location(Utils.getGCAWorld(), x, y, z);
	}
	
	/**
	 * Set the gang's hideout location
	 * @param loc - The location to set the gang's hideout to
	 */
	public void setHideoutLocation(int id, Location loc){
		main.getConfig().set("gangs." + this.getName() + ".hideouts." + id + ".x", loc.getX());
		main.getConfig().set("gangs." + this.getName() + ".hideouts." + id + ".y", loc.getY());
		main.getConfig().set("gangs." + this.getName() + ".hideouts." + id + ".z", loc.getZ());
		main.saveConfig();
	}
	
	/**
	 * Get the list of members that are still alive
	 * @return The list of members that are still alive
	 */
	public List<LivingEntity> getAliveMembers(int hideout){
		List<LivingEntity> alive = new ArrayList<LivingEntity>();
		List<Integer> toRemove = new ArrayList<Integer>();
		for(Entity e : Utils.getGCAWorld().getEntities()){
			if(e instanceof LivingEntity){
				LivingEntity entity = (LivingEntity) e;
				for(int i : members){
					if(entity.getEntityId() == i){
						if(entity.hasMetadata("hideout") == true && (int)Utils.getMetadata(entity, "hideout", ObjectType.INT) == hideout){
							if(entity.isDead() == false && entity.isValid() == true && entity.getLocation().distance(this.getHideoutLocation(hideout)) < 13){
								alive.add(entity);
							}else{
								if(this.getVillagerType() == VillagerType.PROSTITUTE){
									if(entity.hasMetadata("prostitute") == false){
										entity.remove();
										toRemove.add(i);
									}
								}else{
									entity.remove();
									toRemove.add(i);
								}
							}
						}
					}
				}
			}
		}
		for(int i : toRemove){
			members.remove(new Integer(i));
		}
		return alive;
	}
}
