package com.grandcraftauto.game.jobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;

import com.grandcraftauto.core.Main;
import com.grandcraftauto.game.player.GPlayer;
import com.grandcraftauto.utils.Utils;

public class JobInstance {
	
	Main main = Main.getInstance();
	private String gray = ChatColor.GRAY + "";
	private String gold = ChatColor.GOLD + "";
	
	private List<String> players = new ArrayList<String>();
	private HashMap<String, Integer> values = new HashMap<String, Integer>();
	private HashMap<String, Location> locations = new HashMap<String, Location>();
	private JobState state;
	
	private Job job;
	public JobInstance(Job job){
		this.job = job;
		this.state = JobState.WAITING;
		JobCountdown task = new JobCountdown(this, 15, true);
		task.runTaskTimer(main, 0, 20);
	}
	
	/**
	 * Get the job of the instance
	 * @return The job of the instance
	 */
	public Job getJob(){
		return job;
	}
	
	/**
	 * Add a player to the instance
	 * @param player - The player to add to the instance
	 */
	public void addPlayer(Player player){
		if(!players.contains(player.getName())){
			players.add(player.getName());
		}
		new GPlayer(player).setJobInstance(this);
		this.broadcastMessage(gold + player.getName() + gray + " has joined the job!");
	}
	
	/**
	 * Remove a player from the instance
	 * @param player - The player to remove from the instance
	 */
	public void removePlayer(Player player){
		if(players.contains(player.getName())){
			players.remove(player.getName());
		}
		if(players.size() == 0){
			if(main.jobs.contains(this)){
				main.jobs.remove(this);
			}
		}else if(players.size() == 1){
			if(main.jobs.contains(this)){
				main.jobs.remove(this);
			}
			for(Player p : this.getPlayers()){
				this.removePlayer(p);
			}
		}
		GPlayer gplayer = new GPlayer(player);
		gplayer.quitJobInstance();
		gplayer.refreshScoreboard();
		gplayer.refreshNametag();
		if(this.getState() == JobState.PREGAME || this.getState() == JobState.STARTED){
			if(player.isInsideVehicle() == true){
				player.getVehicle().remove();
			}
			if(this.locations.containsKey(player.getName())){
				player.teleport(this.locations.get(player.getName()));
				this.locations.remove(player.getName());
			}
		}else{
			this.broadcastMessage(gold + player.getName() + gray + " has left the job!");
		}
	}
	
	/**
	 * Get the players in the instance
	 * @return The players in the instance
	 */
	@SuppressWarnings("deprecation")
	public List<Player> getPlayers(){
		List<Player> players = new ArrayList<Player>();
		for(String s : this.players){
			if(Utils.isOnline(s) == true){
				players.add(Bukkit.getPlayer(s));
			}else{
				this.players.remove(s);
			}
		}
		return players;
	}
	
	/**
	 * Refresh all of the players' scoreboards
	 */
	public void refreshPlayerScoreboards(){
		for(Player p : this.getPlayers()){
			GPlayer gp = new GPlayer(p);
			gp.refreshScoreboard();
		}
	}
	
	/**
	 * Broadcast a message to all the players in the instance
	 * @param message - The message to broadcast
	 */
	public void broadcastMessage(String message){
		for(Player p : this.getPlayers()){
			GPlayer gp = new GPlayer(p);
			gp.sendMessage(message);
		}
	}
	
	/**
	 * Get the state of the instance
	 * @return The state of the instance
	 */
	public JobState getState(){
		return state;
	}
	
	/**
	 * Set the state of the instance
	 * @param state - The state of the instance
	 */
	public void setState(JobState state){
		this.state = state;
	}
	
	/**
	 * Set the value of the player
	 * @param player - The player
	 * @param type - The value type
	 * @param value - The value
	 */
	public void setPlayerValue(Player player, ValueType type, int value){
		if(this.values.containsKey(player.getName() + "|" + type.toString())){
			this.values.remove(player.getName());
		}
		this.values.put(player.getName() + "|" + type.toString(), value);
	}
	
	/**
	 * Get the value of the player
	 * @param player - The player
	 * @param type - The value type
	 * @return The value
	 */
	public int getPlayerValue(Player player, ValueType type){
		int value = 0;
		if(this.values.containsKey(player.getName() + "|" + type.toString())){
			value = this.values.get(player.getName() + "|" + type.toString());
		}
		return value;
	}
	
	/**
	 * Set the value of the team
	 * @param team - The team
	 * @param type - The value type
	 * @param value - The value
	 */
	public void setTeamValue(int team, ValueType type, int value){
		if(this.values.containsKey(team + "|" + type.toString())){
			this.values.remove(team);
		}
		this.values.put(team + "|" + type.toString(), value);
	}
	
	/**
	 * Get the value of a team
	 * @param player - The team
	 * @param type - The value type
	 * @return The value
	 */
	public int getTeamValue(int team, ValueType type){
		int value = 0;
		if(this.values.containsKey(team + "|" + type.toString())){
			value = this.values.get(team + "|" + type.toString());
		}
		return value;
	}
	
	/**
	 * Get a player's team
	 * @param player - The player
	 * @return The team
	 */
	public int getPlayerTeam(Player player){
		return this.getPlayerValue(player, ValueType.TEAM);
	}
	
	/**
	 * Get the name of a team
	 * @param team - The team
	 * @return The name
	 */
	public String getTeamName(int team){
		String name = "";
		if(team == 1){
			name = ChatColor.RED + "Red";
		}else if(team == 2){
			name = ChatColor.BLUE + "Blue";
		}
		return name;
	}
	
	/**
	 * Get the first place player
	 * @return The first place player
	 */
	public String getFirstPlace(){
		String firstPlace = "None";
		if(job instanceof Race){
			int mostLaps = 0;
			int mostCheckpoints = 0;
			for(Player p : this.getPlayers()){
				int laps = this.getPlayerValue(p, ValueType.LAPS);
				int checkpoints = this.getPlayerValue(p, ValueType.CHECKPOINTS);
				if(laps > mostLaps){
					mostLaps = laps;
					mostCheckpoints = checkpoints;
					firstPlace = p.getName();
				}else if(laps == mostLaps && checkpoints > mostCheckpoints){
					mostLaps = laps;
					mostCheckpoints = checkpoints;
					firstPlace = p.getName();
				}
			}
		}else if(job instanceof FreeForAll){
			int mostKills = 0;
			for(Player p : this.getPlayers()){
				int kills = this.getPlayerValue(p, ValueType.KILLS);
				if(kills > mostKills){
					mostKills = kills;
					firstPlace = p.getName();
				}
			}
		}else if(job instanceof TeamDeathmatch){
			int mostKills = 0;
			for(int x = 1; x <= 2; x++){
				int kills = this.getTeamValue(x, ValueType.KILLS);
				if(kills > mostKills){
					mostKills = kills;
					firstPlace = this.getTeamName(x);
				}
			}
		}
		if(firstPlace.length() > 14){
			firstPlace = firstPlace.substring(0, 14);
		}
		return firstPlace;
	}
	
	/**
	 * Start the instance
	 */
	@SuppressWarnings("deprecation")
	public void start(){
		if(job instanceof Race){
			Race race = (Race) job;
			int pos = 1;
			this.setState(JobState.PREGAME);
			for(Player p : this.getPlayers()){
				GPlayer gp = new GPlayer(p);
				this.locations.put(p.getName(), p.getLocation());
				Location loc = race.getStartingLocation(pos);
				p.teleport(loc);
				Minecart cart = (Minecart) Utils.getGCAWorld().spawnEntity(race.getStartingLocation(pos).add(0, 2.5, 0), EntityType.MINECART);
				Utils.setMetadata(cart, "car", race.getCar().getName());
				cart.setPassenger(p);
				pos++;
				p.sendBlockChange(race.getCheckpoint(1).add(0, 1, 0), 124, (byte) 0);
				gp.refreshScoreboard();
			}
			JobCountdown task = new JobCountdown(this, 10, false);
			task.runTaskTimer(main, 0, 20);
		}else if(job instanceof FreeForAll){
			FreeForAll ffa = (FreeForAll) job;
			List<Integer> startingPoints = new ArrayList<Integer>();
			this.setState(JobState.PREGAME);
			for(Player p : this.getPlayers()){
				GPlayer gp = new GPlayer(p);
				p.setHealth(20);
				p.setFoodLevel(20);
				this.locations.put(p.getName(), p.getLocation());
				int pos = Utils.randInt(1, ffa.getTotalStartingLocations());
				while(startingPoints.contains(pos)){
					pos = Utils.randInt(1, ffa.getTotalStartingLocations());
				}
				Location loc = ffa.getStartingLocation(pos);
				p.teleport(loc.add(0, 0.5, 0));
				gp.refreshScoreboard();
			}
			JobCountdown task = new JobCountdown(this, 10, false);
			task.runTaskTimer(main, 0, 20);
		}else if(job instanceof TeamDeathmatch){
			TeamDeathmatch tdm = (TeamDeathmatch) job;
			List<Integer> startingPoints = new ArrayList<Integer>();
			this.setState(JobState.PREGAME);
			for(Player p : this.getPlayers()){
				GPlayer gp = new GPlayer(p);
				p.setHealth(20);
				p.setFoodLevel(20);
				this.locations.put(p.getName(), p.getLocation());
				int team1 = this.getTeamValue(1, ValueType.PLAYERS);
				int team2 = this.getTeamValue(2, ValueType.PLAYERS);
				int team = 1;
				if(team1 <= team2){
					this.setPlayerValue(p, ValueType.TEAM, 1);
					this.setTeamValue(1, ValueType.PLAYERS, team1 + 1);
					team = 1;
				}else{
					this.setPlayerValue(p, ValueType.TEAM, 2);
					this.setTeamValue(1, ValueType.PLAYERS, team2 + 1);
					team = 2;
				}
				int pos = Utils.randInt(1, tdm.getTotalStartingLocations(team));
				while(startingPoints.contains(pos)){
					pos = Utils.randInt(1, tdm.getTotalStartingLocations(team));
				}
				Location loc = tdm.getStartingLocation(team, pos);
				p.teleport(loc.add(0, 0.5, 0));
				gp.refreshScoreboard();
				gp.refreshNametag();
			}
			JobCountdown task = new JobCountdown(this, 10, false);
			task.runTaskTimer(main, 0, 20);
		}
	}
	
	/**
	 * End the instance
	 */
	public void end(String winner){
		if(job instanceof Race){
			Race race = (Race) job;
			int playersSize = this.getPlayers().size();
			int totalCheckpoints = race.getTotalCheckpoints();
			int winnerRPReward = (int) ((playersSize * 5) * (totalCheckpoints * 0.25));
			int winnerCashReward = (int) ((playersSize * 6) * (totalCheckpoints * 0.5));
			int loserRPReward = (int) ((playersSize * 2) * (totalCheckpoints * 0.1));
			int loserCashReward = (int) ((playersSize * 3) * (totalCheckpoints * 0.2));
			for(Player p : this.getPlayers()){
				GPlayer gp = new GPlayer(p);
				gp.sendNotification("Race", gold + winner + gray + " has won the race!");
				if(p.getName().equalsIgnoreCase(winner)){
					gp.addXP(winnerRPReward);
					gp.setWalletBalance(gp.getWalletBalance() + winnerCashReward);
					gp.setJobWins(gp.getJobWins() + 1);
					p.sendMessage("  " + gold + "+ " + gray + winnerRPReward + " RP");
					p.sendMessage("  " + gold + "+ " + gray + "$" + winnerCashReward);
				}else{
					gp.addXP(loserRPReward);
					gp.setWalletBalance(gp.getWalletBalance() + loserCashReward);
					p.sendMessage("  " + gold + "+ " + gray + loserRPReward + " RP");
					p.sendMessage("  " + gold + "+ " + gray + "$" + loserCashReward);
				}
				this.removePlayer(p);
				gp.refreshScoreboard();
			}
		}else if(job instanceof FreeForAll){
			FreeForAll ffa = (FreeForAll) job;
			int playersSize = this.getPlayers().size();
			int winnerRPReward = (int)((playersSize * 3.5) + (ffa.getKillsRequired() * 0.8));
			int winnerCashReward = (int)((playersSize * 10) + (ffa.getKillsRequired() * 6));
			int loserRPReward = (int)((playersSize * 1) + (ffa.getKillsRequired() * 0.5));
			int loserCashReward = (int)((playersSize * 4) + (ffa.getKillsRequired() * 2));
			for(Player p : this.getPlayers()){
				GPlayer gp = new GPlayer(p);
				gp.sendNotification("Free for All", gold + winner + gray + " has won the match!");
				if(p.getName().equalsIgnoreCase(winner)){
					gp.addXP(winnerRPReward);
					gp.setWalletBalance(gp.getWalletBalance() + winnerCashReward);
					gp.setJobWins(gp.getJobWins() + 1);
					p.sendMessage("  " + gold + "+ " + gray + winnerRPReward + " RP");
					p.sendMessage("  " + gold + "+ " + gray + "$" + winnerCashReward);
				}else{
					gp.addXP(loserRPReward);
					gp.setWalletBalance(gp.getWalletBalance() + loserCashReward);
					p.sendMessage("  " + gold + "+ " + gray + loserRPReward + " RP");
					p.sendMessage("  " + gold + "+ " + gray + "$" + loserCashReward);
				}
				this.removePlayer(p);
				gp.refreshScoreboard();
			}
		}else if(job instanceof TeamDeathmatch){
			TeamDeathmatch tdm = (TeamDeathmatch) job;
			int winnerTeam = 1;
			if(winner.contains("Blue")){
				winnerTeam = 2;
			}
			int playersSize = this.getPlayers().size();
			int winnerRPReward = (int)((playersSize * 3.5) + (tdm.getKillsRequired() * 0.8));
			int winnerCashReward = (int)((playersSize * 10) + (tdm.getKillsRequired() * 6));
			int loserRPReward = (int)((playersSize * 1) + (tdm.getKillsRequired() * 0.5));
			int loserCashReward = (int)((playersSize * 4) + (tdm.getKillsRequired() * 2));
			for(Player p : this.getPlayers()){
				GPlayer gp = new GPlayer(p);
				gp.sendNotification("Team Deathmatch", gold + ChatColor.stripColor(winner) + " Team" + gray + " has won the match!");
				if(this.getPlayerTeam(p) == winnerTeam){
					gp.addXP(winnerRPReward);
					gp.setWalletBalance(gp.getWalletBalance() + winnerCashReward);
					gp.setJobWins(gp.getJobWins() + 1);
					p.sendMessage("  " + gold + "+ " + gray + winnerRPReward + " RP");
					p.sendMessage("  " + gold + "+ " + gray + "$" + winnerCashReward);
				}else{
					gp.addXP(loserRPReward);
					gp.setWalletBalance(gp.getWalletBalance() + loserCashReward);
					p.sendMessage("  " + gold + "+ " + gray + loserRPReward + " RP");
					p.sendMessage("  " + gold + "+ " + gray + "$" + loserCashReward);
				}
				this.removePlayer(p);
				gp.refreshScoreboard();
				gp.refreshNametag();
			}
		}
		
		if(main.jobs.contains(this)){
			main.jobs.remove(this);
		}
	}
}
