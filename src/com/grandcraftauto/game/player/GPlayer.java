package com.grandcraftauto.game.player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.confuser.barapi.BarAPI;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import com.grandcraftauto.core.Main;
import com.grandcraftauto.game.InvokedGroup;
import com.grandcraftauto.game.MentalState;
import com.grandcraftauto.game.Rank;
import com.grandcraftauto.game.Skill;
import com.grandcraftauto.game.VillagerType;
import com.grandcraftauto.game.apartment.Apartment;
import com.grandcraftauto.game.apartment.ApartmentChest;
import com.grandcraftauto.game.cars.Car;
import com.grandcraftauto.game.crew.Crew;
import com.grandcraftauto.game.crew.CrewMember;
import com.grandcraftauto.game.crew.CrewRank;
import com.grandcraftauto.game.inventories.InventoryHandler;
import com.grandcraftauto.game.inventories.Slot;
import com.grandcraftauto.game.jobs.FreeForAll;
import com.grandcraftauto.game.jobs.JobInstance;
import com.grandcraftauto.game.jobs.JobState;
import com.grandcraftauto.game.jobs.Race;
import com.grandcraftauto.game.jobs.TeamDeathmatch;
import com.grandcraftauto.game.jobs.ValueType;
import com.grandcraftauto.game.leaderboards.LeaderboardType;
import com.grandcraftauto.game.leaderboards.LeaderboardUtils;
import com.grandcraftauto.game.missions.Character;
import com.grandcraftauto.game.missions.Dialogue;
import com.grandcraftauto.game.missions.Mission;
import com.grandcraftauto.game.missions.MissionType;
import com.grandcraftauto.game.missions.Objective;
import com.grandcraftauto.game.missions.Reward;
import com.grandcraftauto.game.missions.SideMission;
import com.grandcraftauto.game.missions.SideMissionType;
import com.grandcraftauto.game.missions.objectives.ApproachObjective;
import com.grandcraftauto.game.missions.objectives.HoldUpObjective;
import com.grandcraftauto.game.missions.objectives.ObtainItemsObjective;
import com.grandcraftauto.game.pathfinding.AStar;
import com.grandcraftauto.game.pathfinding.PathingResult;
import com.grandcraftauto.game.pathfinding.Tile;
import com.grandcraftauto.game.pathfinding.AStar.InvalidPathException;
import com.grandcraftauto.game.weapons.Grenade;
import com.grandcraftauto.game.weapons.Gun;
import com.grandcraftauto.game.weapons.Shotgun;
import com.grandcraftauto.game.weapons.Weapon;
import com.grandcraftauto.tasks.InvokedTask;
import com.grandcraftauto.tasks.MessageTask;
import com.grandcraftauto.tasks.ShootTask;
import com.grandcraftauto.tasks.SpawnTask;
import com.grandcraftauto.utils.EffectUtils;
import com.grandcraftauto.utils.Help;
import com.grandcraftauto.utils.TextUtils;
import com.grandcraftauto.utils.TitleUtils;
import com.grandcraftauto.utils.Utils;

import ca.wacos.nametagedit.NametagAPI;

public class GPlayer implements CrewMember{
	
	Main main = Main.getInstance();
	private String gray = ChatColor.GRAY + "";
	private String gold = ChatColor.GOLD + "";
	
	PlayerFile playerFile;
	PlayerStats stats = null;
	
	Player player = null;
	String playerName = null;
	
	public GPlayer(Player p){
		player = p;
		playerName = p.getName();
		playerFile = new PlayerFile(p.getName());
	}
	
	public GPlayer(String pName){
		playerName = pName;
		playerFile = new PlayerFile(pName);
	}
	
	public GPlayer(File file){
		playerFile = new PlayerFile(file);
	}
	
	/**
	 * Get the player's file
	 */
	public PlayerFile getFile(){
		return playerFile;
	}
	
	/**
	 * Get the player's cached stats
	 * @return The player's cached stats
	 */
	public PlayerStats getStats(){
		if(main.stats.containsKey(player.getName()) == false){
			main.stats.put(player.getName(), new PlayerStats(this));
		}
		if(this.stats == null){
			this.stats = main.stats.get(player.getName());
		}
		return this.stats;
	}
	
	/**
	 * Send a message header to the player
	 * @param header - The header to be sent
	 */
	public void sendMessageHeader(String header){
		player.sendMessage(TextUtils.centerText(gray + "-=-(" + gold + TextUtils.getDoubleArrow() + gray + ")-=-" + "  " + gold + header + "  " + gray + "-=-(" + gold + TextUtils.getBackwardsDoubleArrow()
				+ gray + ")-=-"));
	}
	
	/**
	 * Send a message to the player
	 * @param message - The message to be sent
	 */
	public void sendMessage(String message){
		player.sendMessage(ChatColor.GOLD + TextUtils.getArrow() + gray + " " + message);
	}
	
	/**
	 * Send a notification to the player
	 * @param name - The name of the notification
	 * @param message - The message in the notification
	 */
	public void sendNotification(String name, String message){
		player.sendMessage(gold + ChatColor.ITALIC + name + " " + ChatColor.RESET + gold + TextUtils.getArrow() + gray + " " + message);
	}
	
	/**
	 * Send an error message to the player
	 * @param error - The error message to be sent
	 */
	public void sendError(String error){
		player.sendMessage(ChatColor.GOLD + TextUtils.getArrow() + ChatColor.DARK_RED + " " + error);
	}
	
	/**
	 * Send the command help page to the player
	 */
	public void sendCommandHelp(Help type, int page){
		if(type == Help.GENERAL){
			this.sendMessageHeader("Command Help");
			this.sendMessage(gold + "/mission " + gray + "- Get your current mission overview!");
			this.sendMessage(gold + "/mission cancel " + gray + "- Cancel your current mission!");
			this.sendMessage(gold + "/job " + gray + "- Manage your current job!");
			this.sendMessage(gold + "/stats [player]" + gray + "- Review your statistics!");
			this.sendMessage(gold + "/apartment " + gray + "- Get information about your apartment!");
			this.sendMessage(gold + "/crew " + gray + "- Get information about your crew!");
			this.sendMessage(gold + "/friends " + gray + "- Manage your friends!");
			this.sendMessage(gold + "/top " + gray + "- Retrieve a leaderboard!");
		}else if(type == Help.APARTMENT){
			this.sendMessageHeader("Apartment Help");
			this.sendMessage(gold + "/apartment list " + gray + "- Get the list of apartments you own!");
			this.sendMessage(gold + "/apartment set primary " + gray + "- Set your primary apartment!");
			this.sendMessage(gold + "/apartment pay rent " + gray + "- Pay your apartment rent!");
			this.sendMessage(gold + "/apartment allow crew " + gray + "- Allow crew into your apartment!");
		}else if(type == Help.CREW){
			this.sendMessageHeader("Crew Help");
			if(page > 2){
				page = 2;
			}
			if(page == 1){
				this.sendMessage(gold + "/crew list " + gray + "- Get the list of crews!");
				this.sendMessage(gold + "/crew info <name> " + gray + "- Get information of a crew!");
				this.sendMessage(gold + "/crew create <name> " + gray + "- Create a new crew!");
				this.sendMessage(gold + "/crew invite <player> " + gray + "- Invite a player to your crew!");
				this.sendMessage(gold + "/crew join <crew> " + gray + "- Join a crew!");
				this.sendMessage(gold + "/crew leave " + gray + "- Leave your crew!");
				this.sendMessage(gold + "/crew kick <player> " + gray + "- Kick a member from your crew!");
				player.sendMessage("");
				this.sendMessage("Type " + gold + "/crew help " + (page + 1) + gray + " for more commands!");
			}else if(page == 2){
				this.sendMessage(gold + "/crew chat " + gray + "- Toggle crew chat!");
				this.sendMessage(gold + "/crew colors  " + gray + "- Change the crew colors!");
				this.sendMessage(gold + "/crew promote <player> " + gray + "- Promote a player in your crew!");
				this.sendMessage(gold + "/crew demote <player> " + gray + "- Demote a player in your crew!");
				this.sendMessage(gold + "/crew rename <name> " + gray + "- Demote a player in your crew!");
				this.sendMessage(gold + "/crew setleader <player>  " + gray + "- Set the leader of the crew!");
				this.sendMessage(gold + "/crew disband  " + gray + "- Disband the crew!");
				//player.sendMessage("");
				//this.sendMessage("Type " + gold + "/crew help " + (page + 1) + gray + " for more commands!");
			}
		}else if(type == Help.FRIENDS){
			this.sendMessageHeader("Friends Help");
			this.sendMessage(gold + "/friends list " + gray + "- Retrieve your friends list!");
			this.sendMessage(gold + "/friends request <player> " + gray + "- Add a friend!");
			this.sendMessage(gold + "/friends remove <player> " + gray + "- Remove a friend!");
			this.sendMessage(gold + "/friends tp <player> " + gray + "- Teleport to a friend!");
		}else if(type == Help.JOB){
			this.sendMessageHeader("Job Help");
			this.sendMessage(gold + "/job quit " + gray + "- Quit your current job!");
		}else if(type == Help.TOP){
			this.sendMessageHeader("Leaderboards");
			for(LeaderboardType t : LeaderboardType.values()){
				this.sendMessage(gold + "/top " + t.toString().toLowerCase() + gray + " - Get the " + t.toString().toLowerCase() + " leaderboard!");
			}
		}
	}
	
	/**
	 * Send the player a leaderboard
	 * @param type - The leaderboard to send the player
	 */
	public void sendLeaderboard(LeaderboardType type, int page){
		LeaderboardUtils.sendLeaderboard(player, type, page);
	}
	
	/**
	 * Send a title to the player 
	 * @param title - The title to send
	 * @param subtitle - The subtitle to send
	 */
	public void sendTitle(String title, String subtitle){
		TitleUtils.sendTitle(player, 10, 50, 10, title, subtitle);
	}
	
	/**
	 * Send a subtitle to the player
	 * @param subtitle - The subtitle to send
	 * @param stay - How long the subtitle should stay
	 */
	public void sendSubtitle(String subtitle, int stay){
		TitleUtils.sendTitle(player, 10, stay, 10, "", subtitle);
	}
	
	/**
	 * Send a tab header and footer to the player
	 * @param title - The tab title
	 * @param subtitle - The tab subtitle
	 */
	public void sendTabTitle(String title, String subtitle){
		TitleUtils.sendTabTitle(player, title, subtitle);
	}
	
	/**
	 * Send the player a boss bar with the message and a percent filled
	 * @param message - The message to set the boss bar to
	 * @param percent - The percent to set the boss bar at
	 */
	@Deprecated
	public void sendBar(String message, float percent){
		if(BarAPI.hasBar(player) == true){
			BarAPI.removeBar(player);
		}
		BarAPI.setMessage(player, message, percent);
	}
	
	public void setBarHealth(float percent){
		if(BarAPI.hasBar(player) == true){
			BarAPI.setHealth(player, percent);
		}
	}
	
	/**
	 * Remove the player's boss bar if they have one
	 */
	public void removeBar(){
		if(BarAPI.hasBar(player) == true){
			BarAPI.removeBar(player);
		}
	}
	
	/**
	 * Set or unset the player's screen to zoomed
	 * @param set - Boolean to set or unset the player's screen
	 */
	public void setZoomed(boolean set){
		if(set == true){
			if(player.hasPotionEffect(PotionEffectType.SLOW)){
				player.removePotionEffect(PotionEffectType.SLOW);
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 4));
			main.zoomed.add(player.getName());
		}else{
			if(player.hasPotionEffect(PotionEffectType.SLOW)){
				player.removePotionEffect(PotionEffectType.SLOW);
			}
			if(main.zoomed.contains(player.getName())){
				main.zoomed.remove(player.getName());
			}
		}
	}
	
	/**
	 * Check if the player's screen is zoomed
	 * @return True if screen is zoomed, false if it's not
	 */
	public boolean isZoomed(){
		return main.zoomed.contains(player.getName());
	}
	
	/**
	 * Update the player's tab list name
	 */
	public void updateTabListName(){
		String name = player.getName();
		if(player.isOp() == true){
			name = gold + TextUtils.getStar() + " " + this.getMentalState().getColor() + name;
		}else{
			name = this.getMentalState().getColor() + name;
		}
		if(name.length() > 16){
			name = name.substring(0, 16);
		}
		player.setPlayerListName(name);
	}
	
	/**
	 * Refresh the player's scoreboard
	 */
	public void refreshScoreboard(){
		Scoreboard board = Utils.getScoreboardManager().getNewScoreboard();
		org.bukkit.scoreboard.Objective obj = board.registerNewObjective("board", "dummy");
		List<Score> scores = new ArrayList<Score>();
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		if(this.hasJob() == false || this.getJobInstance().getState() == JobState.WAITING){
			obj.setDisplayName(gray + "GCA");
			scores.add(obj.getScore(gold + TextUtils.arrow(0) + "Wallet"));
			scores.add(obj.getScore(ChatColor.LIGHT_PURPLE + gray + "$" + (int)this.getWalletBalance()));
			scores.add(obj.getScore(gold + TextUtils.arrow(0) + "Bank"));
			scores.add(obj.getScore(ChatColor.DARK_PURPLE + gray + "$" + (int)this.getBankBalance()));
			scores.add(obj.getScore(gold + TextUtils.arrow(0) + "State"));
			scores.add(obj.getScore(this.getMentalState().getColor() + WordUtils.capitalizeFully(this.getMentalState().toString())));
			scores.add(obj.getScore(""));
			scores.add(obj.getScore(gold + "Level: " + gray + this.getLevel()));
			scores.add(obj.getScore(gold + "KDR: " + gray + this.getKDR()));
		}else{
			JobInstance jobinstance = this.getJobInstance();
			if(jobinstance.getJob() instanceof Race){
				Race race = (Race) jobinstance.getJob();
				obj.setDisplayName(gold + "Race");
				scores.add(obj.getScore(gold + TextUtils.arrow(0) + "Laps"));
				scores.add(obj.getScore(gray + jobinstance.getPlayerValue(player, ValueType.LAPS) + "/" + gray + race.getLaps()));
				scores.add(obj.getScore(gold + TextUtils.arrow(0) + "Checkpoint"));
				scores.add(obj.getScore(gray + jobinstance.getPlayerValue(player, ValueType.CHECKPOINTS) + "/" + gray + race.getTotalCheckpoints()));
				scores.add(obj.getScore(gold + TextUtils.arrow(0) + "1" + TextUtils.getSuperScript() + " Place"));
				scores.add(obj.getScore(gray + jobinstance.getFirstPlace()));
			}else if(jobinstance.getJob() instanceof FreeForAll){
				FreeForAll ffa = (FreeForAll) jobinstance.getJob();
				obj.setDisplayName(gold + "Free for All");
				scores.add(obj.getScore(gold + TextUtils.arrow(0) + "Kills"));
				scores.add(obj.getScore(gray + jobinstance.getPlayerValue(player, ValueType.KILLS) + "/" + gray + ffa.getKillsRequired()));
				scores.add(obj.getScore(gold + TextUtils.arrow(0) + "1" + TextUtils.getSuperScript() + " Place"));
				scores.add(obj.getScore(gray + jobinstance.getFirstPlace()));
			}else if(jobinstance.getJob() instanceof TeamDeathmatch){
				TeamDeathmatch tdm = (TeamDeathmatch) jobinstance.getJob();
				obj.setDisplayName(gold + "Team Deathmatch");
				scores.add(obj.getScore(gold + TextUtils.arrow(0) + "Team"));
				scores.add(obj.getScore(jobinstance.getTeamName(jobinstance.getPlayerTeam(player))));
				scores.add(obj.getScore(gold + TextUtils.arrow(0) + "Kills"));
				scores.add(obj.getScore(gray + jobinstance.getTeamValue(jobinstance.getPlayerTeam(player), ValueType.KILLS) + "/" + gray + tdm.getKillsRequired()));
				scores.add(obj.getScore(gold + TextUtils.arrow(0) + "1" + TextUtils.getSuperScript() + " Place"));
				scores.add(obj.getScore(gray + jobinstance.getFirstPlace()));
			}
		}
		
		int order = scores.size();
		for(Score s : scores){
			s.setScore(order);
			order--;
		}
		player.setScoreboard(board);
	}
	
	/**
	 * Refresh the player's nametag
	 */
	public void refreshNametag(){
		if(this.hasJob() == false || this.getJobInstance().getState() == JobState.WAITING){
			NametagAPI.updateNametagHard(player.getName(), this.getMentalState().getColor() + "", "");
		}else{
			JobInstance jobinstance = this.getJobInstance();
			if(jobinstance.getJob() instanceof TeamDeathmatch){
				if(jobinstance.getPlayerTeam(player) == 1){
					NametagAPI.updateNametagHard(player.getName(), ChatColor.RED + "", "");
				}else if(jobinstance.getPlayerTeam(player) == 2){
					NametagAPI.updateNametagHard(player.getName(), ChatColor.BLUE + "", "");
				}
			}
		}
	}
	
	/**
	 * Send the player a message with a specified player's statistics
	 * @param player - The other player to collect the stats of and send to the player
	 */
	public void sendStatistics(Player player){
		GPlayer gplayer = new GPlayer(player);
		if(gplayer.playerFile.getFile().exists() == true){
			this.sendMessageHeader(player.getName() + "'s Stats");
			this.sendMessage(gold + "General:");
			this.player.sendMessage("  " + gold + TextUtils.getArrow() + gray + " Level: " + gold + gplayer.getLevel());
			if(gplayer.hasCrew() == true){
				this.player.sendMessage("  " + gold + TextUtils.getArrow() + gray + " Crew: " + gold + gplayer.getCrew().getName());
			}
			this.player.sendMessage("  " + gold + TextUtils.getArrow() + gray + " Total Job Wins: " + gold + gplayer.getJobWins());
			this.player.sendMessage("  " + gold + TextUtils.getArrow() + gray + " Playtime: " + gold + Utils.convertSeconds(gplayer.getPlaytime()));
			this.sendMessage(gold + "Kills/Deaths:");
			this.player.sendMessage("  " + gold + TextUtils.getArrow() + gray + " Kills: " + gold + gplayer.getKills());
			this.player.sendMessage("  " + gold + TextUtils.getArrow() + gray + " Deaths: " + gold + gplayer.getDeaths());
			this.player.sendMessage("  " + gold + TextUtils.getArrow() + gray + " KDR: " + gold + gplayer.getKDR());
		}else{
			this.sendError("That player does not have any statistics!");
		}
	}
	
	/**
	 * Send the player a message with a specified player's statistics
	 * @param player - The other player to collect the stats of and send to the player
	 */
	public void sendStatistics(String player){
		GPlayer gplayer = new GPlayer(player);
		if(gplayer.playerFile.getFile().exists() == true){
			this.sendMessageHeader(player + "'s Stats");
			this.sendMessage(gold + "General:");
			this.player.sendMessage("  " + gold + TextUtils.getArrow() + gray + " Level: " + gold + gplayer.getLevel());
			if(gplayer.hasCrew() == true){
				this.player.sendMessage("  " + gold + TextUtils.getArrow() + gray + " Crew: " + gold + gplayer.getCrew().getName());
			}
			this.player.sendMessage("  " + gold + TextUtils.getArrow() + gray + " Total Job Wins: " + gold + gplayer.getJobWins());
			this.player.sendMessage("  " + gold + TextUtils.getArrow() + gray + " Playtime: " + gold + Utils.convertSeconds(gplayer.getPlaytime()));
			this.sendMessage(gold + "Kills/Deaths:");
			this.player.sendMessage("  " + gold + TextUtils.getArrow() + gray + " Kills: " + gold + gplayer.getKills());
			this.player.sendMessage("  " + gold + TextUtils.getArrow() + gray + " Deaths: " + gold + gplayer.getDeaths());
			this.player.sendMessage("  " + gold + TextUtils.getArrow() + gray + " KDR: " + gold + gplayer.getKDR());
		}else{
			this.sendError("That player does not have any statistics!");
		}
	}
	
	/**
	 * Send the player a crew's information
	 * @param crew - The crew to send the player information of
	 */
	public void sendCrewInformation(Crew crew){
		this.sendMessageHeader(Utils.getChatColorFromWool(crew.getPrimaryColor()) + TextUtils.getStar() + " " + gold + crew.getName() + " " + Utils.getChatColorFromWool(crew.getSecondaryColor()) + 
				TextUtils.getStar());
		this.sendMessage(gold + "Leader:");
		player.sendMessage("  " + gold + TextUtils.getArrow() + gray + " " + crew.getLeader().getName());
		this.sendMessage(gold + "Members (" + crew.getMembers().size() + "):");
		for(CrewMember m : crew.getMembers()){
			player.sendMessage("  " + gold + TextUtils.getArrow() + gray + " " + m.getName() + " (" + WordUtils.capitalizeFully(m.getCrewRank().toString()) + ")");
		}
		this.sendMessage(gold + "Kills/Deaths:");
		player.sendMessage("  " + gold + TextUtils.getArrow() + gray + " Kills: " + gold + crew.getKills());
		player.sendMessage("  " + gold + TextUtils.getArrow() + gray + " Deaths: " + gold + crew.getDeaths());
		player.sendMessage("  " + gold + TextUtils.getArrow() + gray + " KDR: " + gold + crew.getKDR());
	}
	
	/**
	 * Send the player the crew list for a page
	 * @param page - The page to send a crew list of
	 */
	public void sendCrewList(int page){
		List<Crew> crews = Crew.list();
		if(crews.size() > 0){
			int sizePerPage=7;
			if(crews.size() < sizePerPage){
				sizePerPage = crews.size();
			}
			double totalpages = (Utils.roundUp(crews.size(), sizePerPage)) / sizePerPage;
			if(page > (int)totalpages){
				page = (int)totalpages;
			}
			this.sendMessageHeader("Crews - Page " + page + "/" + (int)totalpages);
			page--;
			
			int from = Math.max(0,page*sizePerPage);
			int to = Math.min(crews.size(),(page+1)*sizePerPage);
			
			List<Crew> result = crews.subList(from,to);
			for(Crew c : result){
				this.sendMessage(gold + c.getName() + gray + " (Members: " + c.getMembers().size() + ")");
			}
			player.sendMessage(" ");
			this.sendMessage("Type " + gold + "/crew info <crew> " + gray + "to see information about a crew!");
		}else{
			this.sendError("There are currently no crews!");
		}
	}
	
	/**
	 * Send the player their friends list
	 * @param page - The page to send
	 */
	public void sendFriendsList(int page){
		List<String> friends = this.getFriends();
		if(friends.size() > 0){
			int sizePerPage=7;
			if(friends.size() < sizePerPage){
				sizePerPage = friends.size();
			}
			double totalpages = (Utils.roundUp(friends.size(), sizePerPage)) / sizePerPage;
			if(page > (int)totalpages){
				page = (int)totalpages;
			}
			this.sendMessageHeader("Friends - Page " + page + "/" + (int)totalpages);
			page--;
			
			int from = Math.max(0,page*sizePerPage);
			int to = Math.min(friends.size(),(page+1)*sizePerPage);
			
			List<String> result = friends.subList(from,to);
			for(String s : result){
				String online = ChatColor.DARK_RED + "No";
				if(Utils.isOnline(s) == true){
					online = ChatColor.GREEN + "Yes";
				}
				this.sendMessage(gold + s + gray + " - Online: " + online);
			}
			if(result.size() >= 7){
				player.sendMessage(" ");
				this.sendMessage("Type " + gold + "/friends list <page> " + gray + "to go to the next page!");
			}
		}else{
			this.sendError("You currently have no friends!");
		}
	}
	
	/**
	 * Send the player his/her available missions
	 */
	public void sendAvailableMissions(){
		List<Character> givers = new ArrayList<Character>();
		List<Mission> availableMissions = new ArrayList<Mission>();
		for(Mission m : Mission.values()){
			if(m.getType() == MissionType.REGULAR){
				if(this.hasCompletedMission(m.getID()) == false){
					if(!givers.contains(m.getGiver())){
						if(this.getLevel() >= m.getMinimumLevel()){
							availableMissions.add(m);
							givers.add(m.getGiver());
						}
					}
				}
			}else if(m.getType() == MissionType.SIDE_MISSION){
				if(SideMission.getSideMission(SideMissionType.DAILY) != null && SideMission.getSideMission(SideMissionType.DAILY).getID() == m.getID()){
					if(this.canCompleteSideMission(SideMissionType.DAILY) == true){
						if(this.getLevel() >= m.getMinimumLevel()){
							availableMissions.add(m);
							givers.add(m.getGiver());
						}
					}
				}
			}
		}
		this.sendMessageHeader("Available Missions");
		if(availableMissions.size() > 0){
			for(Mission m : availableMissions){
				if(m.getType() == MissionType.REGULAR){
					this.sendMessage("\"" + m.getName() + "\"");
				}else if(m.getType() == MissionType.SIDE_MISSION){
					this.sendMessage("\"" + m.getName() + "\" - " + gold + "Daily Mission");
				}
				player.sendMessage(gold + "   " + TextUtils.getArrow() + " Mission Giver: " + gray + m.getGiver().getName());
				player.sendMessage(gold + "   " + TextUtils.getArrow() + " Minimum Level: " + gray + m.getMinimumLevel());
			}
		}else{
			this.sendMessage("You do not have any available missions at this time.");
		}
	}
	
	/**
	 * Send the player a message of some dialogue
	 * @param dialogue - The dialogue to send to the player
	 */
	public void sendDialogueMessage(Dialogue dialogue, boolean header){
		if(!main.messageTask.contains(player.getName())){
			if(header == true){
				this.sendMessageHeader("Mission Dialogue");
			}else{
				player.sendMessage(" ");
			}
			MessageTask task = new MessageTask(player, dialogue);
			task.runTaskTimer(main, 0, 100);
			main.messageTask.add(player.getName());
		}
	}
	
	/**
	 * Send a mission overview to the player
	 */
	public void sendMissionOverview(boolean command){
		this.sendMessage("\"" + this.getMission().getName() + "\"");
		this.sendMessage(ChatColor.ITALIC + this.getMission().getDescription());
		if(command == true){
			player.sendMessage(gold + "   " + TextUtils.getArrow() + " Current Objective: " + gray + this.getMission().getObjectives().get(this.getCurrentObjectiveID()).getDescription());
		}else{
			if(this.getMission().getObjectives().get(this.getCurrentObjectiveID()) instanceof ApproachObjective){
				this.sendDialogueMessage(this.getMission().getObjectives().get(this.getCurrentObjectiveID()).getDialogue(), false);
			}
		}
	}
	
	/**
	 * Send the player an objective message
	 * @param obj - The objective to send the player a message of
	 */
	public void sendObjectiveMessage(Objective obj){
		this.sendNotification("New Objective", obj.getDescription());
	}
	
	/**
	 * Get the player's rank
	 * @return The player's rank
	 */
	public Rank getRank(){
		if(this.getFile().getConfig().getString("rank") != null){
			return Rank.valueOf(this.getFile().getConfig().getString("rank").toUpperCase());
		}else{
			return null;
		}
	}
	
	/**
	 * Set the player's rank
	 * @param rank - The rank to set the player to
	 */
	public void setRank(Rank rank){
		this.getFile().setConfigValue("rank", rank.toString().toLowerCase());
	}
	
	/**
	 * Get the player's rank ladder position
	 * @return The player's rank ladder position
	 */
	public int getRankLadder(){
		return this.getFile().getConfig().getInt("rankLadder");
	}
	
	/**
	 * Set the player's rank ladder position
	 * @param ladder - The player's rank ladder position
	 */
	public void setRankLadder(int ladder){
		this.getFile().setConfigValue("rankLadder", ladder);
	}
	
	/**
	 * Get the player's symbol for their current level
	 * @return The player's level symbol
	 */
	public String getLevelSymbol(){
		return TextUtils.getSymbolForLevel(this.getLevel());
	}
	
	/**
	 * Remove the player's current potion effects
	 */
	public void removePotionEffects(){
		for(PotionEffect e : player.getActivePotionEffects()){
			player.removePotionEffect(e.getType());
		}
	}
	
	/**
	 * Fire the player's death
	 */
	public void death(){
		boolean fire = true;
		
		if(this.hasJob() == true && this.getJobInstance().getState() == JobState.STARTED){
			JobInstance jobinstance = this.getJobInstance();
			if(jobinstance.getJob() instanceof FreeForAll){
				FreeForAll ffa = (FreeForAll) jobinstance.getJob();
				player.teleport(ffa.getRandomStartingLocation());
			}else if(jobinstance.getJob() instanceof TeamDeathmatch){
				TeamDeathmatch tdm = (TeamDeathmatch) jobinstance.getJob();
				player.teleport(tdm.getRandomStartingLocation(jobinstance.getPlayerTeam(player)));
			}
		}else{
			if(this.getWalletBalance() > 5){
				Utils.dropMoney(player.getLocation(), (int)this.getWalletBalance());
				this.setWalletBalance(0);
			}
			player.teleport(Utils.getSpawnPoint());
		}
		player.setVelocity(new Vector(0.0D, 0.1D, 0.0D));
		
		if(fire == true){
			this.removeBar();
			this.setDeaths(this.getDeaths() + 1);
			this.refreshScoreboard();
			
			LivingEntity k = null;
			if(player.getKiller() != null){
				k = player.getKiller();
			}else if(main.playerkiller.containsKey(player.getName())){
				k = main.playerkiller.get(player.getName());
			}
			if(k != null){
				if(k instanceof Player){
					Player killer = (Player) k;
					if(!main.killCooldown.contains(killer.getName())){
						GPlayer gkiller = new GPlayer(killer);
						gkiller.setKills(gkiller.getKills() + 1);
						if(gkiller.hasJob() == true && gkiller.getJobInstance().getState() == JobState.STARTED){
							JobInstance jobinstance = gkiller.getJobInstance();
							if(gkiller.getJobInstance().getJob() instanceof FreeForAll){
								FreeForAll ffa = (FreeForAll) jobinstance.getJob();
								int kills = jobinstance.getPlayerValue(killer, ValueType.KILLS) + 1;
								if(kills >= ffa.getKillsRequired()){
									jobinstance.end(killer.getName());
								}else{
									jobinstance.setPlayerValue(killer, ValueType.KILLS, kills);
									jobinstance.refreshPlayerScoreboards();
									gkiller.sendNotification("Free for All", "You now have " + gold + kills + "/" + ffa.getKillsRequired() + gray + " kills!");
								}
							}else if(gkiller.getJobInstance().getJob() instanceof TeamDeathmatch){
								TeamDeathmatch tdm = (TeamDeathmatch) jobinstance.getJob();
								int team = jobinstance.getPlayerTeam(killer);
								int kills = jobinstance.getTeamValue(team, ValueType.KILLS) + 1;
								if(kills >= tdm.getKillsRequired()){
									jobinstance.end(jobinstance.getTeamName(team));
								}else{
									jobinstance.setTeamValue(team, ValueType.KILLS, kills);
									jobinstance.refreshPlayerScoreboards();
									gkiller.sendNotification("Team Deathmatch", "Your team now has " + gold + kills + "/" + tdm.getKillsRequired() + gray + " kills!");
								}
							}
						}else{
							Utils.broadcastMessage(gold + player.getName() + gray + " has been wasted by " + gold + killer.getName() + gray + "!");
							this.sendTitle(ChatColor.DARK_RED + "WASTED", ChatColor.GRAY + "You were killed by " + killer.getName() + ".");
							gkiller.addMentalState(10);
						}
						
						if(main.bounties.containsKey(player.getName())){
							int amount = main.bounties.get(player.getName());
							gkiller.setWalletBalance(gkiller.getWalletBalance() + amount);
							gkiller.sendMessage("You have collected " + gold + player.getName() + gray + "'s " + gold + "$" + amount + gray + " bounty!");
							main.bounties.remove(player.getName());
						}
						
						killer.playSound(killer.getEyeLocation(), Sound.SUCCESSFUL_HIT, 10, 1);
						
						double difference = gkiller.getMentalState().getDifference(this.getMentalState());
						if(difference > 1){
							int xpToRemove = (int)(difference * 4);
							if((gkiller.getXP() - xpToRemove) > 0){
								gkiller.removeXP(xpToRemove);
							}
							if(gkiller.getWalletBalance() >= 50){
								gkiller.setWalletBalance(gkiller.getWalletBalance() - 50);
							}else if(gkiller.getBankBalance() >= 50){
								gkiller.setBankBalance(gkiller.getBankBalance() - 50);
							}
							gkiller.sendError("You are losing money and RP due to your high mental state!");
						}else if(difference < 0){
							gkiller.addXP(Math.abs(difference * 4));
						}
						
						gkiller.refreshScoreboard();
						main.killCooldown.add(killer.getName());
					}
				}else{
					if(k.getCustomName() != null){
						Utils.broadcastMessage(gold + player.getName() + gray + " has been wasted by a " + gold + ChatColor.stripColor(k.getCustomName()) + gray + "!");
						this.sendTitle(ChatColor.DARK_RED + "WASTED", ChatColor.GRAY + "You were killed by a " + ChatColor.stripColor(k.getCustomName()) + ".");
					}else{
						Utils.broadcastMessage(gold + player.getName() + gray + " has been wasted by a " + gold + WordUtils.capitalizeFully(k.getType().toString().replaceAll("_", " ")) + gray + "!");
						this.sendTitle(ChatColor.DARK_RED + "WASTED", ChatColor.GRAY + "You were killed by a " + WordUtils.capitalizeFully(k.getType().toString().replaceAll("_", " ")));
					}
				}
			}else{
				Utils.broadcastMessage(gold + player.getName() + gray + " has been wasted" + gray + "!");
				this.sendTitle(ChatColor.DARK_RED + "WASTED", ChatColor.GRAY + "You have comitted suicide.");
			}
			
			if(main.invoked.containsKey(player.getName())){
				main.invoked.get(player.getName()).cancel();
				main.invoked.get(player.getName()).clearTask(false);
				main.invoked.remove(player.getName());
			}
			
			if(this.hasMission() == true){
				if(this.getObjective() instanceof ObtainItemsObjective){
					ObtainItemsObjective obj = (ObtainItemsObjective) this.getObjective();
					obj.setAmountObtained(player.getName(), 0);
					this.removeMaterialFromInventory(obj.getItemType(), this.getAmountOfMaterialInInventory(obj.getItemType()));
				}
			}
			
			this.removePotionEffects();
			if(player.hasMetadata("kidneyStolen") == true){
				player.removeMetadata("kidneyStolen", main);
			}
			player.setHealth(20);
			player.setFoodLevel(20);
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 1));
			if(main.playerkiller.containsKey(player.getName())){
				main.playerkiller.remove(player.getName());
			}
		}else{
			player.setHealth(20);
		}
	}
	
	/**
	 * Log the player out and perform necessary functions to clear them
	 */
	public void logout(){
		if(main.invoked.containsKey(player.getName())){
			main.invoked.get(player.getName()).cancel();
			main.invoked.get(player.getName()).clearTask(false);
			main.invoked.remove(player.getName());
		}
		
		if(main.car.containsKey(player.getName())){
			main.car.get(player.getName()).remove();
			main.car.remove(player.getName());
		}
		
		if(main.inGarage.containsKey(player.getName())){
			main.inGarage.get(player.getName()).clearInstance();
			main.inGarage.remove(player.getName());
		}
		
		if(main.reloading.containsKey(player.getName())){
			main.reloading.get(player.getName()).cancel();
			main.reloading.remove(player.getName());
		}
		
		if(main.pathEffect.containsKey(player.getName())){
			main.pathEffect.get(player.getName()).clearEffect();
		}
		
		if(main.playtime.containsKey(player.getName())){
			this.setPlaytime(main.playtime.get(player.getName()));
			main.playtime.remove(player.getName());
		}
		
		if(this.hasJob() == true){
			this.getJobInstance().removePlayer(player);
		}
		
		this.getStats().insert();
	}
	
	/**
	 * Spawn the player in at the appropriate location
	 */
	public void spawn(){
		Location spawnPoint = Utils.getSpawnPoint();
		if(this.hasApartment() == true){
			spawnPoint = this.getPrimaryApartment().getSpawn();
		}
		main.spawning.add(player.getName());
		SpawnTask task = new SpawnTask(player, spawnPoint);
		task.runTaskTimer(main, 40, 40);
		player.teleport(new Location(spawnPoint.getWorld(), spawnPoint.getX(), spawnPoint.getY() + 90, spawnPoint.getZ(), 0, 90));
		player.setAllowFlight(true);
		player.setFlying(true);
	}
	
	/**
	 * Remove all the armor from the player
	 */
	public void clearArmor(){
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);
	}
	
	/**
	 * Get the player's weapon in his/her hand
	 * @return The weapon in the player's hand
	 */
	public Weapon getWeaponInHand(){
		Weapon weapon = null;
		for(Weapon w : Weapon.list()){
			if(player.getItemInHand().getType() == w.getMaterial()){
				if(player.getItemInHand().hasItemMeta() == true && player.getItemInHand().getItemMeta().hasDisplayName() == true){
					if(ChatColor.stripColor(player.getItemInHand().getItemMeta().getDisplayName()).equalsIgnoreCase(w.getName())){
						weapon = w;
						break;
					}
				}
			}
		}
		return weapon;
	}
	
	/**
	 * Check if the player has a weapon in his/her hand
	 * @return True or false depending on if the player has a weapon in his/her hand or not
	 */
	public boolean hasWeaponInHand(){
		if(this.getWeaponInHand() != null){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Shoot the player's gun in their hand
	 */
	@SuppressWarnings("deprecation")
	public void shootGun(){
		if(!main.shootCooldown.contains(player.getName())){
			if(!main.reloading.containsKey(player.getName())){
				Weapon weapon = this.getWeaponInHand();
				
				boolean allowShot = true;
				
				if(player.getItemInHand().getDurability() >= weapon.getMaterial().getMaxDurability() && !(weapon instanceof Grenade)){
					allowShot = false;
				}
				
				if(allowShot == true){
					if(weapon instanceof Gun){
						main.shootCooldown.add(player.getName());
						Gun gun = (Gun) this.getWeaponInHand();
						ShootTask task = new ShootTask(player, gun);
						task.runTaskTimer(main, 0, gun.getFiringRate());
					}else if(weapon instanceof Shotgun){
						main.shootCooldown.add(player.getName());
						Shotgun shotgun = (Shotgun) this.getWeaponInHand();
						
						for(int x = 1; x <= shotgun.getRoundsPerShot(); x++){
							Utils.shootBullet(player);
						}
						
						int maxdura = shotgun.getMaterial().getMaxDurability();
						int durapershot = maxdura / shotgun.getClipSize();
						player.getItemInHand().setDurability((short) (player.getItemInHand().getDurability() + durapershot));
						
						main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
							public void run(){
								main.shootCooldown.remove(player.getName());
							}
						}, shotgun.getCooldown());
					}else if(weapon instanceof Grenade){
						final Grenade grenade = (Grenade) this.getWeaponInHand();
						main.grenadeID += 1;
						final Item item = Utils.getGCAWorld().dropItem(player.getEyeLocation(), Utils.renameItem(new ItemStack(player.getItemInHand().getType(), 1, player.getItemInHand().getData().getData()), 
								"" + main.grenadeID));
						item.setPickupDelay(1200);
						item.setVelocity(player.getEyeLocation().getDirection());
						if(player.getItemInHand().getAmount() > 1){
							player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
						}else{
							player.setItemInHand(null);
						}
						final List<Item> items = new ArrayList<Item>();
						main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
							public void run(){
								Material mat = item.getLocation().subtract(0, 1, 0).getBlock().getType();
								int itemID = 0;
								for(int x = 1; x <= 13; x++){
									itemID += 1;
									Item i = Utils.getGCAWorld().dropItem(item.getLocation(), Utils.renameItem(new ItemStack(mat), "" + itemID));
									i.setVelocity(new Vector(Utils.attemptSetAsNegative(Utils.getRandomDouble(0.5)), 0.7, Utils.attemptSetAsNegative(Utils.getRandomDouble(0.5))));
									i.setPickupDelay(1200);
									items.add(i);
								}
								EffectUtils.playExplodeEffect(item.getLocation());
								item.remove();
								for(Entity e : item.getNearbyEntities(grenade.getBlastRadius(), grenade.getBlastRadius(), grenade.getBlastRadius())){
									if(e instanceof LivingEntity){
										boolean allow = true;
										LivingEntity le = (LivingEntity) e;
										if(e instanceof Villager){
											Villager v = (Villager) e;
											attackedVillager(v);
										}
										EffectUtils.playBloodEffect(e);
										if(le instanceof Player){
											Player p = (Player) le;
											if(p.getName().equalsIgnoreCase(player.getName())){
												allow = false;
											}
										}
										if(allow == true){
											double damage = grenade.getDamage();
											double newhealth = le.getHealth() - damage;
											if(newhealth > 0){
												le.damage(damage);
											}else{
												if(le instanceof Player){
													Player p = (Player) le;
													if(!p.getName().contains("Cop")){
														GPlayer gp = new GPlayer(p);
														if(main.playerkiller.containsKey(p.getName())){
															main.playerkiller.remove(p.getName());
														}
														main.playerkiller.put(p.getName(), player);
														gp.death();
													}else{
														p.remove();
													}
												}else{
													le.setHealth(0);
												}
											}
										}
									}else if(e instanceof Minecart){
										Minecart cart = (Minecart) e;
										if(cart.getPassenger() != null){
											cart.getPassenger().eject();
										}
										cart.remove();
									}
								}
							}
						}, 80);
						main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
							public void run(){
								for(Item i : items){
									i.remove();
								}
								main.shootCooldown.remove(player.getName());
							}
						}, 110);
					}
				}
			}else{
				main.reloading.get(player.getName()).cancel();
				main.reloading.remove(player.getName());
			}
		}
	}
	
	/**
	 * Check if the player has a certain amount of material in their inventory
	 * @param material - The material to check
	 * @param amount - The amount of material to check
	 * @return True or false depending on if the player has the specified amount of material in their inventory or not
	 */
	public int getAmountOfMaterialInInventory(Material material){
		int items = 0;
		for(int x = 0; x <= 35; x++){
			if(player.getInventory().getItem(x) != null){
				ItemStack slot = player.getInventory().getItem(x);
				if(slot.getType() == material){
					items = items + slot.getAmount();
				}
			}
		}
		return items;
	}
	
	/**
	 * Remove a certain amount of material from the player's inventory
	 * @param material - The material to remove
	 * @param amount - The amount of the material to remove
	 */
	public void removeMaterialFromInventory(Material material, int amount){
		int itemsTaken = 0;
		for(int x = 0; x <= 35; x++){
			if(itemsTaken < amount){
				if(player.getInventory().getItem(x) != null){
					ItemStack slot = player.getInventory().getItem(x);
					if(slot.getType() == material){
						if(slot.getAmount() > amount){
							slot.setAmount(slot.getAmount() - amount);
							itemsTaken = itemsTaken + amount;
						}else if(slot.getAmount() == amount){
							player.getInventory().setItem(x, null);
							itemsTaken = itemsTaken + amount;
						}else if(slot.getAmount() < amount){
							int taken = slot.getAmount();
							player.getInventory().setItem(x, null);
							itemsTaken = itemsTaken + taken;
						}
					}
				}
			}else{
				break;
			}
		}
	}

	/**
	 * Set the player's destination
	 * @param destination - The name of the destination
	 * @param end - The location of the destination
	 */
	public void setDestination(String destination, Location end){
		if(this.hasDestination() == true){
			this.removeDestination();
		}
		Location start = player.getLocation().subtract(0, 1, 0);
		if(player.getVehicle() != null){
			start = player.getLocation();
		}
		if(end.getBlock().getType() == Material.AIR || end.getBlock() == null){
			end = end.subtract(0, 1, 0);
		}
		if(start.distance(end) < 200){
			try{
				AStar path = new AStar(start, end, 200);
				ArrayList<Tile> route = path.iterate();
				PathingResult result = path.getPathingResult();
				
				if(result == PathingResult.SUCCESS){
					Utils.createPathEffect(player, start, end, route);
					this.sendNotification("GPS", "You have set your destination to " + gold + destination + gray + "!");
				}else{
					this.sendNotification("GPS", "A valid path could not be found.");
				}
			}catch (InvalidPathException e){
				this.sendNotification("GPS", "A valid path could not be found.");
			}
		}else{
			this.sendNotification("GPS", "Destination is too far away to calculate a route.");
		}
	}
	
	/**
	 * Remove the player's destination
	 */
	public void removeDestination(){
		if(main.pathEffect.containsKey(player.getName())){
			main.pathEffect.get(player.getName()).clearEffect();
		}
	}
	
	/**
	 * Get the player's destination
	 * @return The player's destination
	 */
	public Location getDestination(){
		Location dest = null;
		if(main.pathEffect.containsKey(player.getName())){
			dest = main.pathEffect.get(player.getName()).getDestination();
		}
		return dest;
	}

	/**
	 * Check if the player has a destination
	 * @return True or false depending on if the player has a destination or not
	 */
	public boolean hasDestination(){
		if(main.pathEffect.containsKey(player.getName())){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * Check if the player is new
	 * @return True or false depending on if the player is new or not
	 */
	public boolean hasJoinedBefore(){
		return this.playerFile.getConfig().getBoolean("hasJoinedBefore");
	}

	/**
	 * Set if the player is new
	 * @param set - Set if the player is new
	 */
	public void setHasJoinedBefore(boolean set){
		this.playerFile.setConfigValue("hasJoinedBefore", set);
	}
	
	/**
	 * Get the player's total kills
	 * @return The player's total kills
	 */
	public int getKills(){
		if(player != null){
			if(this.getStats().kills == -1){
				this.getStats().kills = this.playerFile.getConfig().getInt("stats.kills");
			}
			return this.getStats().kills;
		}else{
			return playerFile.getConfig().getInt("stats.kills");
		}
	}
	
	/**
	 * Set the player's total kills
	 * @param kills - The total kills to set
	 */
	public void setKills(int kills){
		this.getStats().kills = kills;
	}
	
	/**
	 * Get the player's total deaths
	 * @return The player's total deaths
	 */
	public int getDeaths(){
		if(player != null){
			if(this.getStats().deaths == -1){
				this.getStats().deaths = this.playerFile.getConfig().getInt("stats.deaths");
			}
			return this.getStats().deaths;
		}else{
			return this.playerFile.getConfig().getInt("stats.deaths");
		}
	}
	
	/**
	 * Set the player's total deaths
	 * @param kills - The total deaths to set
	 */
	public void setDeaths(int deaths){
		this.getStats().deaths = deaths;
	}
	
	/**
	 * Get the player's KDR
	 * @return The player's KDR
	 */
	public double getKDR(){
		double kills = this.getKills();
		double deaths = this.getDeaths();
		try{
			double kdr = Utils.round((float)(kills/deaths), 2);
			if(kdr > 0){
				return kdr;
			}else{
				return 0;
			}
		}catch (Exception ex){
			return 0;
		}
	}
	
	/**
	 * Set the player's playtime in seconds
	 * @param playtime - The seconds to set the player's playtime to
	 */
	public void setPlaytime(int playtime){
		this.getFile().setConfigValue("stats.playtime", playtime);
	}
	
	/**
	 * Get the player's playtime in seconds
	 * @return The player's playtime in seconds
	 */
	public int getPlaytime(){
		if(playerName != null){
			if(main.playtime.containsKey(playerName) == true){
				return main.playtime.get(playerName);
			}else{
				return this.getFile().getConfig().getInt("stats.playtime");
			}
		}else{
			return this.getFile().getConfig().getInt("stats.playtime");
		}
	}
	
	/**
	 * Get the player's total job wins
	 * @return The player's total job wins
	 */
	public int getJobWins(){
		if(player != null){
			if(this.getStats().jobwins == -1){
				this.getStats().jobwins = this.playerFile.getConfig().getInt("stats.jobwins");
			}
			return this.getStats().jobwins;
		}else{
			return this.playerFile.getConfig().getInt("stats.jobwins");
		}
	}
	
	/**
	 * Set the player's total job wins
	 * @param kills - The total job wins to set
	 */
	public void setJobWins(int wins){
		this.getStats().jobwins = wins;
	}
	
	/**
	 * Invoke the specified villager type from the player
	 * @param type - The type of villager to invoke
	 * @return The task that has been created
	 */
	public InvokedTask setHasInvoked(VillagerType type, String gangName){
		if(!main.invoked.containsKey(player.getName())){
			InvokedTask task = new InvokedTask(player, new InvokedGroup(type, gangName));
			task.runTaskTimer(main, 0, 3);
			if(type == VillagerType.COP){
				task.setWantedLevel(1);
			}
			main.invoked.put(player.getName(), task);
			return task;
		}else{
			return null;
		}
	}
	
	/**
	 * Fire required methods after attacking a villager
	 * @param villager - The villager that was attacked
	 */
	public void attackedVillager(Villager villager){
		VillagerType type = VillagerType.fromProfession(villager.getProfession());
		if(type == VillagerType.REGULAR || type == VillagerType.PROSTITUTE){
			boolean copsNearby = false;
			for(Entity e : villager.getNearbyEntities(10, 10, 10)){
				if(e instanceof Villager){
					Villager v = (Villager) e;
					if(VillagerType.fromProfession(v.getProfession()) == VillagerType.COP){
						copsNearby = true;
						break;
					}
				}
			}
			if(copsNearby == true){
				type = VillagerType.COP;
			}
		}
		if(type != VillagerType.REGULAR){
			if(this.hasMission() == true && this.getObjective() instanceof HoldUpObjective && villager.getCustomName() != null && villager.getCustomName().contains("Drug")){
				return;
			}
			if(!main.invoked.containsKey(player.getName())){
				if(type == VillagerType.COP){
					this.setHasInvoked(type, null);
				}else if(type == VillagerType.GANG_MEMBER){
					this.setHasInvoked(type, ChatColor.stripColor(villager.getCustomName()));
				}
			}else{
				InvokedTask task = main.invoked.get(player.getName());
				task.setTimer(0);
				if(task.getVillagerType() == VillagerType.COP){
					if(task.getWantedLevel() < 5){
						task.setWantedLevel(task.getWantedLevel() + task.getWantedLevelToAdd());
					}
				}
			}
		}
	}
	
	/**
	 * Check if the player has any cops nearby
	 * @return True or false depending on if the player has cops nearby or not
	 */
	public boolean hasCopsNearby(){
		boolean has = false;
		for(Entity e : player.getNearbyEntities(10, 10, 10)){
			if(e instanceof Villager){
				Villager v = (Villager) e;
				if(VillagerType.fromProfession(v.getProfession()) == VillagerType.COP){
					has = true;
					break;
				}
			}
		}
		return has;
	}
	
	/**
	 * Set the player's mental state value
	 * @param state - The state to set the player to
	 */
	public void setMentalStateValue(double state){
		this.getStats().mentalstate = state;
	}
	
	/**
	 * Get the player's mental state value
	 * @return The player's mental state value
	 */
	public double getMentalStateValue(){
		if(this.getStats().mentalstate == -1){
			this.getStats().mentalstate = this.playerFile.getConfig().getDouble("stats.mentalstate");
		}
		return this.getStats().mentalstate;
	}
	
	/**
	 * Add mental state to the player
	 * @param state - The amount of state to add
	 */
	public void addMentalState(double state){
		MentalState oldState = this.getMentalState();
		if((this.getMentalStateValue() + state) < 100){
			this.setMentalStateValue(this.getMentalStateValue() + state);
			MentalState newState = this.getMentalState();
			if(oldState != newState){
				String stateName = gold + WordUtils.capitalizeFully(newState.toString());
				if(newState == MentalState.MANIAC || newState == MentalState.PSYCHOPATH){
					stateName = gray + "a " + gold + stateName;
				}
				this.sendNotification("Mental State", "You are now " + stateName + gray + "!");
				this.refreshNametag();
				this.updateTabListName();
				this.refreshScoreboard();
			}
		}else{
			this.setMentalStateValue(100);
		}
	}
	
	/**
	 * Remove mental state from the player
	 * @param state - The amount of state to remove
	 */
	public void removeMentalState(double state){
		if((this.getMentalStateValue() - state) > 0){
			this.setMentalStateValue(this.getMentalStateValue() - state);
		}else{
			this.setMentalStateValue(0);
		}
	}
	
	/**
	 * Get the player's mental state
	 * @return The player's mental state
	 */
	public MentalState getMentalState(){
		MentalState state = null;
		for(MentalState s : MentalState.values()){
			if(s.fallsInRange(this.getMentalStateValue())){
				state = s;
				break;
			}
		}
		return state;
	}
	
	/**
	 * Set the player's wallet balance
	 * @param balance - The balance to set the wallet at
	 */
	public void setWalletBalance(double balance){
		if(player != null){
			this.getStats().wallet = balance;
		}else{
			playerFile.setConfigValue("stats.wallet", balance);
		}
	}
	
	/**
	 * Get the player's wallet balance
	 * @return The player's wallet balance
	 */
	public double getWalletBalance(){
		if(player != null){
			if(this.getStats().wallet == -1){
				this.getStats().wallet = this.playerFile.getConfig().getDouble("stats.wallet");
			}
			return this.getStats().wallet;
		}else{
			return playerFile.getConfig().getDouble("stats.wallet");
		}
	}
	
	/**
	 * Set the player's bank balance
	 * @param balance - The balance to set the bank at
	 */
	public void setBankBalance(double balance){
		this.getStats().bank = balance;
	}
	
	/**
	 * Get the player's bank balance
	 * @return The player's bank balance
	 */
	public double getBankBalance(){
		if(this.getStats().bank == -1){
			this.getStats().bank = this.playerFile.getConfig().getDouble("stats.bank");
		}
		return this.getStats().bank;
	}
	
	/**
	 * Set the player's level
	 * @param level - The level to set the player as
	 */
	@SuppressWarnings("deprecation")
	public void setLevel(int level){
		if(player != null){
			this.getStats().level = level;
			player.setLevel(level);
		}else{
			if(Utils.isOnline(playerName)){
				Bukkit.getPlayer(playerName).setLevel(level);
			}
			playerFile.setConfigValue("stats.level", level);
		}
	}
	
	/**
	 * Get the player's level
	 * @return The player's level
	 */
	public int getLevel(){
		if(player != null){
			if(this.getStats().level == -1){
				this.getStats().level = this.playerFile.getConfig().getInt("stats.level");
			}
			return this.getStats().level;
		}else{
			return playerFile.getConfig().getInt("stats.level");
		}
	}
	
	/**
	 * Set the player's xp
	 * @param level - The xp to set the player at
	 */
	public void setXP(double xp){
		this.getStats().xp = xp;
		float percent = (float)((Utils.getPercent(this.getXP(), this.getXPToNextLevel())) * (0.01F));
		player.setExp(percent);
	}
	
	/**
	 * Get the player's xp
	 * @return The player's xp
	 */
	public double getXP(){
		if(this.getStats().xp == -1){
			this.getStats().xp = this.playerFile.getConfig().getDouble("stats.xp");
		}
		return this.getStats().xp;
	}
	
	/**
	 * Get the player's xp required to level up
	 * @return The xp required to level up
	 */
	public int getXPToNextLevel(){
		return Utils.getXPForLevel(this.getLevel());
	}
	
	/**
	 * Add xp to the player
	 * @param xp - The amount of xp to add
	 */
	public void addXP(double xp){
		double toAdd = xp;
		if(this.getRankLadder() == 1){
			toAdd = xp * 1.5;
		}else if(this.getRankLadder() == 2){
			toAdd = xp * 2;
		}else if(this.getRankLadder() == 3){
			toAdd = xp * 2.5;
		}
		this.setXP(this.getXP() + toAdd);
		if(this.getXP() >= this.getXPToNextLevel()){
			this.levelUp();
		}
	}
	
	/**
	 * Remove xp from the player
	 * @param xp - The amount of xp to remove
	 */
	public void removeXP(int xp){
		if((this.getXP() - xp) > 0){
			this.setXP(this.getXP() - xp);
		}
	}
	
	/**
	 * Level the player up
	 */
	public void levelUp(){
		if(this.getLevel() < 100){
			double leftoverXP = this.getXP() - this.getXPToNextLevel();
			int newlevel = (this.getLevel() + 1);
			this.setLevel(newlevel);
			this.setXP(0);
			if(leftoverXP > 0){
				this.addXP(leftoverXP);
			}
			EffectUtils.playGreenSparkleEffect(player.getEyeLocation());
			this.sendNotification("Level Up", "You are now level " + gold + newlevel + gray + "!");
			for(Car c : Car.values()){
				if(c.getMinimumLevel() == newlevel){
					player.sendMessage(gray + "   + Car Unlock: " + gold + c.getName());
				}
			}
			for(Mission m : Mission.values()){
				if(m.getMinimumLevel() == newlevel && m.getType() != MissionType.SIDE_MISSION){
					player.sendMessage(gray + "   + Mission Unlock: " + gold + "\"" + m.getName() + "\"");
				}
			}
			for(Weapon w : Weapon.list()){
				if(w.getMinimumLevel() == newlevel){
					player.sendMessage(gray + "   + Weapon Unlock: " + gold + w.getName());
				}
			}
			if(newlevel < 30){
				this.addThugPoints(1);
				player.sendMessage(gray + "   + Thug Points: " + gold + "1");
			}else if(newlevel >= 30){
				this.addThugPoints(2);
				player.sendMessage(gray + "   + Thug Points: " + gold + "2");
			}
			double symbol = ((double)newlevel) / 10.0;
			if(symbol % 1 == 0){
				player.sendMessage(gray + "   + Chat Symbol: " + gold + TextUtils.getSymbolForLevel(newlevel));
			}
			this.refreshScoreboard();
		}
	}
	
	/**
	 * Level the player up a certain amount of levels
	 */
	public void levelUp(int levels){
		if(this.getLevel() < 100){
			int newlevel = (this.getLevel() + levels);
			this.setLevel(newlevel);
			this.setXP(0);
			EffectUtils.playGreenSparkleEffect(player.getEyeLocation());
			this.sendNotification("Level Up", "You are now level " + gold + newlevel + gray + "!");
			for(Car c : Car.values()){
				if(c.getMinimumLevel() == newlevel){
					player.sendMessage(gray + "   + Car Unlock: " + gold + c.getName());
				}
			}
			for(Mission m : Mission.values()){
				if(m.getMinimumLevel() == newlevel && m.getType() != MissionType.SIDE_MISSION){
					player.sendMessage(gray + "   + Mission Unlock: " + gold + "\"" + m.getName() + "\"");
				}
			}
			for(Weapon w : Weapon.list()){
				if(w.getMinimumLevel() == newlevel){
					player.sendMessage(gray + "   + Weapon Unlock: " + gold + w.getName());
				}
			}
			if(newlevel < 30){
				this.addThugPoints(1);
				player.sendMessage(gray + "   + Thug Points: " + gold + "1");
			}else if(newlevel >= 30){
				this.addThugPoints(2);
				player.sendMessage(gray + "   + Thug Points: " + gold + "2");
			}
			double symbol = ((double)newlevel) / 10.0;
			if(symbol % 1 == 0){
				player.sendMessage(gray + "   + Chat Symbol: " + gold + TextUtils.getSymbolForLevel(newlevel));
			}
			this.refreshScoreboard();
		}
	}
	
	/**
	 * Get the player's skill level
	 * @param skill - The skill to get the level of
	 * @return The player's skill level
	 */
	public int getSkillLevel(Skill skill){
		if(this.getStats().skillLevels.containsKey(skill) == false){
			this.getStats().skillLevels.put(skill, this.playerFile.getConfig().getInt("skills." + skill.toString().toLowerCase() + ".level"));
		}
		return this.getStats().skillLevels.get(skill);
	}
	
	/**
	 * Set the player's skill level
	 * @param skill - The skill to set the level of
	 * @param level - The player's skill level
	 */
	public void setSkillLevel(Skill skill, int level){
		if(level <= 50){
			if(this.getStats().skillLevels.containsKey(skill) == true){
				this.getStats().skillLevels.remove(skill);
			}
			this.getStats().skillLevels.put(skill, level);
		}
	}
	
	/**
	 * Add levels to the player's skill
	 * @param skill - The skill to add levels to
	 * @param levels - The amount of levels to add to the player's skill
	 */
	public void addSkillLevels(Skill skill, int levels){
		this.setSkillLevel(skill, this.getSkillLevel(skill) + levels);
	}
	
	/**
	 * Get the player's skill points
	 * @return The player's skill points
	 */
	public int getThugPoints(){
		if(this.getStats().thugpoints == -1){
			this.getStats().thugpoints = this.playerFile.getConfig().getInt("skills.thugpoints");
		}
		return this.getStats().thugpoints;
	}
	
	/**
	 * Set the player's skill points
	 * @param kills - The player's skill points
	 */
	public void setThugPoints(int points){
		this.getStats().thugpoints = points;
	}
	
	/**
	 * Add skill points to the player
	 * @param points - The amount of skill points to add to the player
	 */
	public void addThugPoints(int points){
		this.setThugPoints(this.getThugPoints() + points);
	}
	
	/**
	 * Remove skill points from the player
	 * @param points - The amount of skill points to remove from the player
	 */
	public void removeThugPoints(int points){
		this.setThugPoints(this.getThugPoints() - points);
	}
	
	/**
	 * Check if the player has a mission
	 * @return True or false depending on if the player has a mission or not
	 */
	public boolean hasMission(){
		if(this.playerFile.getConfig().getInt("mission.id") > 0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Set the player's current mission
	 * @param id - The ID of the mission to set
	 */
	public void setMission(int id){
		this.playerFile.setConfigValue("mission.id", id);
	}
	
	/**
	 * Get the player's current mission ID
	 * @return The current mission ID
	 */
	public int getMissionID(){
		return this.playerFile.getConfig().getInt("mission.id");
	}
	
	/**
	 * Get the player's current mission
	 * @return The player's current mission
	 */
	public Mission getMission(){
		Mission mission = null;
		if(this.hasMission() == true && Mission.getMissionFromID(this.getMissionID()) != null){
			return Mission.getMissionFromID(this.getMissionID());
		}
		return mission;
	}
	
	/**
	 * Set the player's current mission as completed
	 */
	public void completeCurrentMission(){
		if(main.messageTask.contains(player.getName())){
			main.messageTask.remove(player.getName());
		}
		this.sendNotification("Mission Completed", "\"" + this.getMission().getName() + "\"");
		player.sendMessage(gray + "  + " + gold + this.getMission().getRPReward() + gray + " RP");
		if(this.getMission().getRewards() != null){
			for(Reward r : this.getMission().getRewards()){
				if(!(r instanceof Car)){
					if(r.getItemStack() != null){
						InventoryHandler.addItemToAppropriateSlot(player, r.getItemStack());
						player.sendMessage(gray + "  + " + gold + ChatColor.stripColor(r.getItemStack().getItemMeta().getDisplayName()));
					}else if(r.getCashReward() > 0){
						this.setWalletBalance(this.getWalletBalance() + r.getCashReward());
						player.sendMessage(gray + "  + " + gold + "$" + r.getCashReward());
					}
				}else{
					Car c = (Car) r;
					player.sendMessage(gray + "  + " + gold + c.getName());
					if(this.ownsCar(c) == false){
						this.giveCar(c);
					}
				}
			}
		}
		this.addXP(this.getMission().getRPReward());
		if(this.getMission().getType() == MissionType.REGULAR){
			List<Integer> completedMissions = this.playerFile.getConfig().getIntegerList("completedMissions");
			completedMissions.add(this.playerFile.getConfig().getInt("mission.id"));
			this.playerFile.setConfigValue("completedMissions", completedMissions);
		}else if(this.getMission().getType() == MissionType.SIDE_MISSION){
			this.setHasCompletedSideMission(SideMissionType.DAILY);
		}
		this.setMission(0);
		this.setCurrentObjectiveID(0);
		EffectUtils.playGreenSparkleEffect(player.getEyeLocation());
		this.refreshScoreboard();
	}
	
	/**
	 * Check if the player has completed a mission
	 * @param id - The mission
	 * @return True or false depending on if the player has completed the mission or not
	 */
	public boolean hasCompletedMission(int id){
		return this.playerFile.getConfig().getIntegerList("completedMissions").contains(id);
	}
	
	/**
	 * Set the last completion time of the side mission type
	 * @param type - The side mission type to set the last completion time of
	 */
	public void setHasCompletedSideMission(SideMissionType type){
		this.playerFile.setConfigValue("sidemissions." + type.toString().toLowerCase() + ".lastCompletion", System.currentTimeMillis() + type.getMillisecondsToAdd());
	}
	
	/**
	 * Get the last completion time of the side mission type
	 * @param type - The side mission type
	 * @return The last completion time in milliseconds
	 */
	public long getLastSideMissionCompletion(SideMissionType type){
		return this.playerFile.getConfig().getLong("sidemissions." + type.toString().toLowerCase() + ".lastCompletion");
	}
	
	/**
	 * Check if the player can complete a side mission type
	 * @param type - The side mission type
	 * @return True or false depending on if the player can complete the side mission or not
	 */
	public boolean canCompleteSideMission(SideMissionType type){
		if((System.currentTimeMillis() - getLastSideMissionCompletion(type)) > 0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Get the player's current objective ID
	 * @return The player's current objective ID
	 */
	public int getCurrentObjectiveID(){
		return this.playerFile.getConfig().getInt("mission.objective.id");
	}
	
	/**
	 * Set the player's current objective ID
	 * @param id - The ID to set the player's current objective to
	 */
	public void setCurrentObjectiveID(int id){
		this.playerFile.setConfigValue("mission.objective.id", id);
	}
	
	/**
	 * Get the player's current objective amount
	 * @return The player's current objective amount
	 */
	public int getCurrentObjectiveAmount(String obj){
		return this.playerFile.getConfig().getInt("mission.objective.amounts." + obj + ".amount");
	}
	
	/**
	 * Set the player's current objective amount
	 * @param id - The amount to set the player's current objective to
	 */
	public void setCurrentObjectiveAmount(String obj, int amount){
		this.playerFile.setConfigValue("mission.objective.amounts." + obj + ".amount", amount);
	}
	
	/**
	 * Get the player's current objective
	 * @return The player's current objective
	 */
	public Objective getObjective(){
		return this.getMission().getObjectives().get(this.getCurrentObjectiveID());
	}
	
	/**
	 * Advance the player's objective
	 */
	public void advanceObjective(){
		if(main.messageTask.contains(player.getName())){
			main.messageTask.remove(player.getName());
		}
		this.playerFile.setConfigValue("mission.objective.amounts", null);
		this.setCurrentObjectiveID(this.getCurrentObjectiveID() + 1);
		if(this.getCurrentObjectiveID() >= this.getMission().getObjectives().size()){
			/*
			 * Reward
			 */
			this.completeCurrentMission();
		}else{
			if(this.getObjective() instanceof ApproachObjective){
				ApproachObjective obj = (ApproachObjective) this.getObjective();
				if(obj.shouldAutoDialogue() == true){
					this.sendDialogueMessage(this.getMission().getObjectives().get(this.getCurrentObjectiveID()).getDialogue(), false);
				}else{
					this.sendObjectiveMessage(this.getObjective());
				}
			}else{
				this.sendObjectiveMessage(this.getObjective());
			}
		}
	}
	
	/**
	 * Put a new car into the player's garage
	 * @param car - The car to add to the garage
	 */
	public void giveCar(Car car){
		List<String> cars = this.playerFile.getConfig().getStringList("cars.garage");
		cars.add(car.getName().toLowerCase());
		this.playerFile.setConfigValue("cars.garage", cars);
		if(this.getCurrentCar() == null){
			this.setCurrentCar(car);
		}
	}
	
	/**
	 * Get the cars in the player's garage
	 * @return The list of cars the player has
	 */
	public List<Car> getCars(){
		List<Car> cars = new ArrayList<Car>();
		for(Car c : Car.values()){
			if(this.playerFile.getConfig().getStringList("cars.garage").contains(c.getName().toLowerCase())){
				cars.add(c);
			}
		}
		return cars;
	}
	
	public boolean hasACar(){
		if(this.getCars().size() > 0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Check if the player owns a car
	 * @param car - The car to check if the player owns
	 * @return True or false depending on if the player owns the car or not
	 */
	public boolean ownsCar(Car car){
		boolean owns = false;
		for(Car c : this.getCars()){
			if(c.getName().contains(car.getName())){
				owns = true;
				break;
			}
		}
		return owns;
	}
	
	/**
	 * Get the player's currently equipped car
	 * @return The player's current car
	 */
	public Car getCurrentCar(){
		if(this.getStats().currentcar == null){
			for(Car c : Car.values()){
				if(c.getName().equalsIgnoreCase(this.playerFile.getConfig().getString("cars.current"))){
					this.getStats().currentcar = c;
					break;
				}
			}
		}
		return this.getStats().currentcar;
	}
	
	/**
	 * Set the player's currently equipped car
	 * @param car - The car to set the player's current car to
	 */
	public void setCurrentCar(Car car){
		this.getStats().currentcar = car;
		player.getInventory().setItem(Slot.CAR.getSlot(), car.getItemStack(false));
	}
	
	/**
	 * Get the player's apartments
	 * @return The player's apartments
	 */
	public List<Apartment> getApartments(){
		List<Apartment> apts = new ArrayList<Apartment>();
		for(int x : this.playerFile.getConfig().getIntegerList("apartments")){
			apts.add(new Apartment(x));
		}
		return apts;
	}
	
	/**
	 * Add an apartment to the player
	 * @param apt - The apartment to add to the player
	 */
	public void addApartment(Apartment apt){
		List<Integer> apts = this.playerFile.getConfig().getIntegerList("apartments");
		apts.add(apt.getID());
		this.playerFile.setConfigValue("apartments", apts);
		this.setLastRentPayment(apt);
	}
	
	/**
	 * Removes an apartment to the player
	 * @param apt - The apartment to remove from the player
	 */
	public void removeApartment(Apartment apt){
		List<Integer> apts = this.playerFile.getConfig().getIntegerList("apartments");
		for(int i = 0; i <= (apts.size() - 1); i++){
			if(apts.get(i) == apt.getID()){
				apts.remove(i);
				break;
			}
		}
		this.playerFile.setConfigValue("apartments", apts);
	}
	
	/**
	 * Get the player's total amount of apartments
	 * @return The player's total amount of apartments
	 */
	public int getTotalApartments(){
		if(this.playerFile.getConfig().getIntegerList("apartments") != null){
			return this.playerFile.getConfig().getIntegerList("apartments").size();
		}else{
			return 0;
		}
	}
	
	/**
	 * Check if the player has an apartment
	 * @return True or false depending on if the player has an apartment or not
	 */
	public boolean hasApartment(){
		if(this.getApartments() != null && this.getApartments().size() > 0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Check if the player's crew has an apartment
	 * @return True or false depending on if the player's crew has an apartment or not
	 */
	public boolean crewHasApartment(){
		boolean has = false;
		if(this.hasCrew() == true){
			for(CrewMember m : this.getCrew().getMembers()){
				if(m.getPlayer().hasApartment() == true){
					has = true;
					break;
				}
			}
		}
		return has;
	}
	
	/**
	 * Get the player's crew's apartments they own
	 * @return The player's crew's apartments they own
	 */
	public List<Apartment> getCrewApartments(){
		List<Apartment> apartments = new ArrayList<Apartment>();
		for(CrewMember m : this.getCrew().getMembers()){
			if(m.getPlayer().hasApartment() == true){
				apartments.addAll(m.getPlayer().getApartments());
			}
		}
		return apartments;
	}
	
	/**
	 * Check if the player allows his/her crew into his/her apartment
	 * @return True or false depending on if the player allows his/her crew into his/her apartment or not
	 */
	public boolean allowsApartmentForCrew(){
		return this.playerFile.getConfig().getBoolean("apartment.allowCrew");
	}
	
	/**
	 * Set if the player allows his/her crew into his/her apartment
	 * @param set - The boolean to set
	 */
	public void setAllowsApartmentForCrew(boolean set){
		this.playerFile.setConfigValue("apartment.allowCrew", set);
	}
	
	/**
	 * Check if the player owns the apartment
	 * @param apt - The apartment to check ownership of
	 * @return True or false depending on if the player owns the apartment or not
	 */
	public boolean ownsApartment(Apartment apt){
		boolean owns = false;
		for(Apartment a : this.getApartments()){
			if(a.getID() == apt.getID()){
				owns = true;
				break;
			}
		}
		return owns;
	}
	
	/**
	 * Set the player's primary apartment
	 * @param apt - The apartment to set as the player's primary apartment
	 */
	public void setPrimaryApartment(Apartment apt){
		if(apt != null){
			this.playerFile.setConfigValue("apartment.primary", apt.getID());
		}else{
			this.playerFile.setConfigValue("apartment.primary", null);
		}
	}
	
	/**
	 * Get the player's primary apartment
	 * @return The player's primary apartment
	 */
	public Apartment getPrimaryApartment(){
		if(this.playerFile.getConfig().getInt("apartment.primary") >= 1){
			return new Apartment(this.playerFile.getConfig().getInt("apartment.primary"));
		}else{
			return this.getApartments().get(0);
		}
	}
	
	/**
	 * Get the player's maximum apartments he/she can buy
	 * @return The player's maximum apartments he/she can buy
	 */
	public int getMaximumApartments(){
		return this.playerFile.getConfig().getInt("apartment.maxCanBuy");
	}
	
	/**
	 * Set the player's maximum apartments he/she can buy
	 * @param kills - The maximum amount of apartments the player can buy
	 */
	public void setMaximumApartments(int max){
		this.playerFile.setConfigValue("apartment.maxCanBuy", max);
	}
	
	/**
	 * Get a chest's inventory from the player's file
	 * @param chest - The chest to get the inventory of
	 * @return The inventory of the chest according to the player's file
	 */
	public Inventory getChestInventory(Chest chest){
		return new ApartmentChest(this, chest).getInventory();
	}
	
	/**
	 * Update a chest's inventory
	 * @param chest - The chest to update the inventory of
	 * @param inv - The inventory to set the chest as
	 */
	public void updateChestInventory(Chest chest, Inventory inv){
		new ApartmentChest(this, chest).setInventory(inv);
	}
	
	/**
	 * Set the last rent payment time
	 */
	public void setLastRentPayment(Apartment apt){
		this.playerFile.setConfigValue("apartment." + apt.getID() + ".lastRentPayment", System.currentTimeMillis() + 604800000);
		this.setWarnedOfRent(apt, false);
		this.playerFile.setConfigValue("apartment." + apt.getID() + ".lastRentWarning", null);
	}
	
	/**
	 * Get the last rent payment time
	 * @return The last rent payment time
	 */
	public long getLastRentPayment(Apartment apt){
		return this.playerFile.getConfig().getLong("apartment." + apt.getID() + ".lastRentPayment");
	}
	
	/**
	 * Check if the player has to pay rent
	 * @return True or false depending on if the player has to pay rent or not
	 */
	public boolean hasToPayRent(Apartment apt){
		if((System.currentTimeMillis() - this.getLastRentPayment(apt)) > 0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Set if the player has been warned of rent
	 * @param set - The boolean of if the player has been warned
	 */
	public void setWarnedOfRent(Apartment apt, boolean set){
		this.playerFile.setConfigValue("apartment." + apt.getID() + ".warnedOfRent", set);
	}
	
	/**
	 * Get if the player has been warned of rent
	 * @return True or false depending on if the player has been warned or not
	 */
	public boolean getWarnedOfRent(Apartment apt){
		return this.playerFile.getConfig().getBoolean("apartment." + apt.getID() + ".warnedOfRent");
	}
	
	/**
	 * Set the last rent warning time
	 */
	public void setLastRentWarning(Apartment apt){
		this.playerFile.setConfigValue("apartment." + apt.getID() + ".lastRentWarning", System.currentTimeMillis() + 86400000);
	}
	
	/**
	 * Get the last rent warning time
	 * @return The last rent payment time
	 */
	public long getLastRentWarning(Apartment apt){
		return this.playerFile.getConfig().getLong("apartment." + apt.getID() + ".lastRentWarning");
	}
	
	/**
	 * Check if the player's apartment can be foreclosed
	 * @return True or false depending on if the player's apartment can be foreclosed or not
	 */
	public boolean canForecloseApartment(Apartment apt){
		if((System.currentTimeMillis() - this.getLastRentWarning(apt)) > 0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * "Foreclose" the player's apartment and remove it from their ownership
	 * @param apt - The apartment to foreclose
	 */
	public void forecloseApartment(Apartment apt){
		if(this.getPrimaryApartment().getID() == apt.getID()){
			if(this.getApartments().size() > 0){
				for(Apartment a : this.getApartments()){
					this.setPrimaryApartment(a);
					break;
				}
			}else{
				this.setPrimaryApartment(null);
			}
		}
		this.removeApartment(apt);
		this.playerFile.setConfigValue("apartment." + apt.getID(), null);
		this.sendMessage("You no longer own " + gold + apt.getName() + gray + " due to you not paying rent. Your items are still safe in storage, but you must re-purchase the apartment to retrieve them.");
	}
	
	/**
	 * Get the player's crew
	 * @return The player's crew
	 */
	public Crew getCrew(){
		if(player != null){
			if(this.getStats().crew == -1){
				this.getStats().crew = this.playerFile.getConfig().getInt("crew.id");
			}
			if(this.getStats().crew > 0){
				return new Crew(this.getStats().crew);
			}else{
				return null;
			}
		}else{
			if(this.playerFile.getConfig().getInt("crew.id") > 0){
				return new Crew(this.playerFile.getConfig().getInt("crew.id"));
			}else{
				return null;
			}
		}
	}
	
	/**
	 * Set the player's crew
	 * @param crew - The crew to set as the player's crew
	 */
	public void setCrew(Crew crew){
		if(crew != null){
			this.getStats().crew = crew.getID();
		}else{
			this.getStats().crew = -2;
		}
	}
	
	/**
	 * Check if the player has a crew
	 * @return True or false depending on if the player has a crew or not
	 */
	public boolean hasCrew(){
		if(this.getCrew() != null){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Get the player's crew rank
	 */
	public CrewRank getCrewRank(){
		if(this.getStats().crewrank.equalsIgnoreCase("unset")){
			this.getStats().crewrank = this.playerFile.getConfig().getString("crew.rank");
		}
		if(this.getStats().crewrank != null){
			return CrewRank.valueOf(this.getStats().crewrank.toUpperCase());
		}else{
			return null;
		}
	}
	
	/**
	 * Set the player's crew rank
	 * @param rank - The rank to set as the player's crew rank
	 */
	public void setRank(CrewRank rank){
		this.getStats().crewrank = rank.toString().toUpperCase();
	}
	
	/**
	 * Set the player's job
	 * @param job - The player's job
	 */
	public void setJobInstance(JobInstance job){
		main.playerJobs.put(player.getName(), job);
	}
	
	/**
	 * Quit the job the player is in
	 */
	public void quitJobInstance(){
		if(main.playerJobs.containsKey(player.getName())){
			main.playerJobs.remove(player.getName());
		}
	}
	
	/**
	 * Get the player's job
	 * @return The player's job
	 */
	public JobInstance getJobInstance(){
		JobInstance job = null;
		if(main.playerJobs.containsKey(player.getName())){
			job = main.playerJobs.get(player.getName());
		}
		return job;
	}
	
	/**
	 * Check if the player has a job
	 * @return True or false depending on if the player has a job or not
	 */
	public boolean hasJob(){
		if(this.getJobInstance() != null){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Get the player's friends list
	 * @return The player's list of friends
	 */
	public List<String> getFriends(){
		if(this.getStats().friends == null){
			this.getStats().friends = this.playerFile.getConfig().getStringList("friends");
		}
		return this.getStats().friends;
	}
	
	/**
	 * Add a friend to the player's friend list
	 * @param friend - The player to add to the friends list
	 */
	public void addFriend(String friend){
		if(this.getStats().friends == null){
			this.getStats().friends = this.playerFile.getConfig().getStringList("friends");
		}
		this.getStats().friends.add(friend);
	}
	
	/**
	 * Remove a friend from the player's friend list
	 * @param friend - The player to remove from the friends list
	 */
	public void removeFriend(String friend){
		if(this.getStats().friends == null){
			this.getStats().friends = this.playerFile.getConfig().getStringList("friends");
		}
		if(this.getStats().friends.contains(friend)){
			this.getStats().friends.remove(friend);
		}
	}
	
	/**
	 * Check if someone is the player's friend
	 * @param friend - The friend to check
	 */
	public boolean isFriend(String friend){
		if(this.getFriends().contains(friend) == true){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Get the player's name
	 */
	public String getName(){
		if(player != null){
			return player.getName();
		}else{
			return this.playerFile.getConfig().getString("name");
		}
	}
	
	/**
	 * Set the player's name
	 * @param name - The name to set
	 */
	public void setName(String name){
		this.playerFile.setConfigValue("name", name);
	}
	
	/**
	 * Get the GPlayer instance of the player
	 */
	public GPlayer getPlayer(){
		return this;
	}
}
