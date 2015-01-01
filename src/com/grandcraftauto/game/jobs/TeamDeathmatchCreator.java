package com.grandcraftauto.game.jobs;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.grandcraftauto.game.Creator;

public class TeamDeathmatchCreator extends Creator {
	
	private String gray = ChatColor.GRAY + "";
	private String gold = ChatColor.GOLD + "";
	
	private TeamDeathmatch tdm;
	public TeamDeathmatchCreator(Player creator, TeamDeathmatch tdm){
		super(creator, 4);
		this.tdm = tdm;
	}
	
	public TeamDeathmatch getTeamDeathmatch(){
		return tdm;
	}
	
	public void sendStep(int step){
		if(step == 1){
			creator.sendMessage(gold + "Step " + step + ": " + gray + "Create Team 1 starting locations by right clicking, and left click to go to the next step.");
		}else if(step == 2){
			creator.sendMessage(gold + "Step " + step + ": " + gray + "Create Team 2 starting locations by right clicking, and left click to go to the next step.");
		}else if(step == 3){
			creator.sendMessage(gold + "Step " + step + ": " + gray + "Set the amount of kills required by typing it into chat.");
		}else if(step == 4){
			creator.sendMessage(gold + "Step " + step + ": " + gray + "Set the name by typing it into chat.");
		}
	}
	
	public void addStartingLocation(int team, Location loc){
		tdm.addStartingLocation(team, loc);
	}
	
	public void setKillsRequired(int value){
		tdm.setKillsRequired(value);
	}
	
	public void setName(String name){
		tdm.setName(name);
	}

}
