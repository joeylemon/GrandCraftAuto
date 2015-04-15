package com.grandcraftauto.game.missions.objectives;

import org.bukkit.Location;
import org.bukkit.Material;

import com.grandcraftauto.game.VillagerType;
import com.grandcraftauto.game.missions.Dialogue;
import com.grandcraftauto.game.missions.Objective;

public class PlaceBlockObjective extends Objective {

	private Location loc;
	private Material mat;
	private VillagerType toSpawn;
	private String name;
	private int amountToSpawn;
	private boolean explode;
	public PlaceBlockObjective(String description, Dialogue dialogue, boolean revertOnDeath, Location loc, Material mat, VillagerType toSpawn, String name, int amountToSpawn, boolean explode){
		super(description, dialogue, revertOnDeath);
		this.loc = loc;
		this.mat = mat;
		this.toSpawn = toSpawn;
		this.name = name;
		this.amountToSpawn = amountToSpawn;
		this.explode = explode;
	}
	
	/**
	 * Get the location where the block should be placed nearby
	 * @return The location where the block should be placed nearby
	 */
	public Location getLocation(){
		return loc;
	}
	
	/**
	 * Get the block material to be placed
	 * @return The block material to be placed
	 */
	public Material getBlock(){
		return mat;
	}
	
	/**
	 * Get the villager type to spawn upon placing the block
	 * @return The villager type to spawn upon placing the block
	 */
	public VillagerType getToSpawn(){
		return toSpawn;
	}
	
	/**
	 * Get the names of the villagers to spawn
	 * @return The names of the villagers to spawn
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Get the amount to spawn upon placing the block
	 * @return The amount to spawn upon placing the block
	 */
	public int getAmountToSpawn(){
		return amountToSpawn;
	}
	
	/**
	 * Check if the placed block should create explosion effects 30 seconds after placement
	 * @return True if it should explode after placement, false if not
	 */
	public boolean shouldExplode(){
		return explode;
	}

}
