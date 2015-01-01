package com.grandcraftauto.game.districts;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.grandcraftauto.game.Creator;

public class DistrictCreator extends Creator {

	public DistrictCreator(Player creator){
		super(creator, 3);
	}
	
	public void sendStep(int step){
		if(step == 1){
			creator.sendMessage(gold + "Step " + step + ": " + gray + "Create the first position by right clicking a block, and left click to go to the next step.");
		}else if(step == 2){
			creator.sendMessage(gold + "Step " + step + ": " + gray + "Create the second position by right clicking a block, and left click to go to the next step.");
		}else if(step == 3){
			creator.sendMessage(gold + "Step " + step + ": " + gray + "Set the name of the district by typing it into the chat.");
		}
	}
	
	Location loc1 = null;
	public void setFirstPosition(Location loc){
		loc1 = loc;
	}
	
	Location loc2 = null;
	public void setSecondPosition(Location loc){
		loc2 = loc;
	}
	
	String name = null;
	public void setName(String name){
		this.name = name;
	}
	
	public void finish(){
		DistrictUtils.addDistrict(name, loc1, loc2);
	}
}
