package com.grandcraftauto.game.jobs;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.grandcraftauto.game.Creator;
import com.grandcraftauto.game.cars.Car;

public class RaceCreator extends Creator {
	
	private Race race;
	public RaceCreator(Player creator, Race race){
		super(creator, 5);
		this.race = race;
	}
	
	public Race getRace(){
		return race;
	}
	
	public void sendStep(int step){
		if(step == 1){
			creator.sendMessage(gold + "Step " + step + ": " + gray + "Create starting locations by right clicking, and left click to go to the next step.");
		}else if(step == 2){
			creator.sendMessage(gold + "Step " + step + ": " + gray + "Create checkpoints by right clicking, and left click to go to the next step.");
		}else if(step == 3){
			creator.sendMessage(gold + "Step " + step + ": " + gray + "Set the amount of laps by typing it into chat.");
		}else if(step == 4){
			creator.sendMessage(gold + "Step " + step + ": " + gray + "Set the car of the race by typing it into chat.");
		}else if(step == 5){
			creator.sendMessage(gold + "Step " + step + ": " + gray + "Set the name of the race by typing it into chat.");
		}
	}
	
	public void addStartingLocation(Location loc){
		race.addStartingLocation(loc);
	}
	
	public void addCheckpoint(Location loc){
		race.addCheckpoint(loc);
	}
	
	public void setLaps(int value){
		race.setLaps(value);
	}
	
	public void setCar(Car car){
		race.setCar(car);
	}
	
	public void setName(String name){
		race.setName(name);
	}
}
