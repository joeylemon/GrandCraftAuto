package com.grandcraftauto.game.jobs;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.grandcraftauto.game.Creator;

public class FreeForAllCreator extends Creator {
	
	private String gray = ChatColor.GRAY + "";
	private String gold = ChatColor.GOLD + "";
	
	private FreeForAll ffa;
	public FreeForAllCreator(Player creator, FreeForAll ffa){
		super(creator, 3);
		this.ffa = ffa;
	}
	
	public FreeForAll getFreeForAll(){
		return ffa;
	}
	
	public void sendStep(int step){
		if(step == 1){
			creator.sendMessage(gold + "Step " + step + ": " + gray + "Create starting locations by right clicking, and left click to go to the next step.");
		}else if(step == 2){
			creator.sendMessage(gold + "Step " + step + ": " + gray + "Set the amount of kills required by typing it into chat.");
		}else if(step == 3){
			creator.sendMessage(gold + "Step " + step + ": " + gray + "Set the name by typing it into chat.");
		}
	}
	
	public void addStartingLocation(Location loc){
		ffa.addStartingLocation(loc);
	}
	
	public void setKillsRequired(int value){
		ffa.setKillsRequired(value);
	}
	
	public void setName(String name){
		ffa.setName(name);
	}
}
