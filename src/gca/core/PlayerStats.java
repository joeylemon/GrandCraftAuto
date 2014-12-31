package gca.core;

import gca.game.Skill;
import gca.game.cars.Car;

import java.util.HashMap;
import java.util.List;

public class PlayerStats {
	
	private GPlayer gplayer;
	public PlayerStats(GPlayer gplayer){
		this.gplayer = gplayer;
	}
	
	/**
	 * Get the player associated with the stats
	 * @return The player associated with the stats
	 */
	public GPlayer getPlayer(){
		return gplayer;
	}
	
	public int kills = -1;
	public int deaths = -1;
	public int jobwins = -1;
	public double mentalstate = -1;
	public double wallet = -1;
	public double bank = -1;
	public int level = -1;
	public double xp = -1;
	public int thugpoints = -1;
	public Car currentcar = null;
	public int crew = -1;
	public String crewrank = "unset";
	public List<String> friends = null;
	public HashMap<Skill, Integer> skillLevels = new HashMap<Skill, Integer>();
	
	/**
	 * Update the player's file with the latest stats
	 */
	public void insert(){
		for(Skill s : Skill.values()){
			if(skillLevels.containsKey(s) == true && skillLevels.get(s) != 0){
				gplayer.getFile().setConfigValue("skills." + s.toString().toLowerCase() + ".level", skillLevels.get(s));
				skillLevels.remove(s);
			}
		}
		if(kills != -1){
			gplayer.getFile().setConfigValue("stats.kills", kills);
		}
		if(deaths != -1){
			gplayer.getFile().setConfigValue("stats.deaths", deaths);
		}
		if(jobwins != -1){
			gplayer.getFile().setConfigValue("stats.jobwins", jobwins);
		}
		if(mentalstate != -1){
			gplayer.getFile().setConfigValue("stats.mentalstate", mentalstate);
		}
		if(wallet != -1){
			gplayer.getFile().setConfigValue("stats.wallet", wallet);
		}
		if(bank != -1){
			gplayer.getFile().setConfigValue("stats.bank", bank);
		}
		if(level != -1){
			gplayer.getFile().setConfigValue("stats.level", level);
		}
		if(xp != -1){
			gplayer.getFile().setConfigValue("stats.xp", xp);
		}
		if(thugpoints != -1){
			gplayer.getFile().setConfigValue("skills.thugpoints", thugpoints);
		}
		if(currentcar != null){
			gplayer.getFile().setConfigValue("cars.current", currentcar.getName());
		}
		if(crew != -1){
			gplayer.getFile().setConfigValue("crew.id", crew);
		}else if(crew == -2){
			gplayer.getFile().setConfigValue("crew", null);
		}
		if(crewrank != null){
			gplayer.getFile().setConfigValue("crew.rank", crewrank.toUpperCase());
		}
		if(friends != null){
			gplayer.getFile().setConfigValue("friends", friends);
		}
	}

}
