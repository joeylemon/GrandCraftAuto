package com.grandcraftauto.game.garage;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.grandcraftauto.game.Creator;

public class GarageCreator extends Creator {
	
	private String gray = ChatColor.GRAY + "";
	private String gold = ChatColor.GOLD + "";
	
	private Garage garage;
	public GarageCreator(Player creator, int creatorGarageID){
		super(creator, 2);
		garage = new Garage(creatorGarageID);
	}
	
	public Garage getGarage(){
		return garage;
	}
	
	public void sendStep(int step){
		if(step == 1){
			creator.sendMessage(gold + "Step " + step + ": " + gray + "Set the garage location by right clicking.");
		}else if(step == 2){
			creator.sendMessage(gold + "Step " + step + ": " + gray + "Create car spawns by right clicking, and left click to go to the next step.");
		}
	}
	
	// Step 1
	public void setLocation(Location loc){
		garage.setLocation(loc);
	}
	
	// Step 2
	public void addCarSpawn(Location loc){
		garage.addCarSpawn(loc);
	}
}
