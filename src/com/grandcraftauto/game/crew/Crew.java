package com.grandcraftauto.game.crew;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.grandcraftauto.core.Main;
import com.grandcraftauto.game.player.GPlayer;
import com.grandcraftauto.utils.Utils;

public class Crew {
	
	static Main main = Main.getInstance();
	private String gray = ChatColor.GRAY + "";
	private String gold = ChatColor.GOLD + "";
	private String italic = ChatColor.ITALIC + "";
	
	private int id;
	
	/**
	 * Create a new crew instance
	 * @param id - The ID of the crew
	 */
	public Crew(int id){
		this.id = id;
	}
	
	/**
	 * Get the crew's file
	 */
	public File getFile(){
		return new File(main.getDataFolder() + File.separator + "crews", id + ".yml");
	}
	
	/**
	 * Get the crew's config
	 */
	public FileConfiguration getConfig(){
		return YamlConfiguration.loadConfiguration(getFile());
	}
	
	/**
	 * Set a value in the crew's config
	 * 
	 * @param key - The location of the value to set
	 * @param entry - The value to set
	 */
	public void setConfigValue(String key, Object entry){
		FileConfiguration fc = getConfig();
	    fc.set(key, entry);
	    try{
	      fc.save(getFile());
	    }catch (IOException e) {
	      e.printStackTrace();
	    }
	}
	
	/**
	 * Get the ID of the crew
	 * @return The ID of the crew
	 */
	public int getID(){
		return id;
	}
	
	/**
	 * Get the gang's name
	 * @return The gang's name
	 */
	public String getName(){
		return this.getConfig().getString("name");
	}
	
	/**
	 * Set the gang's name
	 * @param name - The gang's name
	 */
	public void setName(String name){
		this.setConfigValue("name", name);
	}
	
	/**
	 * Get the leader of the crew
	 * @return The leader of the crew
	 */
	public CrewMember getLeader(){
		CrewMember leader = null;
		for(CrewMember m : this.getMembers()){
			if(m.getCrewRank() == CrewRank.LEADER){
				leader = m;
				break;
			}
		}
		return leader;
	}
	
	/**
	 * Get the list of members in the crew
	 * @return The list of members in the crew
	 */
	public List<CrewMember> getMembers(){
		List<CrewMember> members = new ArrayList<CrewMember>();
		for(String s : this.getConfig().getStringList("members")){
			members.add(new GPlayer(s));
		}
		return members;
	}
	
	/**
	 * Add a player to the crew
	 * @param player - The player to add to the crew
	 */
	public void addMember(String player){
		List<String> members = this.getConfig().getStringList("members");
		members.add(player);
		this.setConfigValue("members", members);
		GPlayer gplayer = new GPlayer(player);
		gplayer.setCrew(this);
		gplayer.setRank(CrewRank.MEMBER);
	}
	
	/**
	 * Remove a player from the crew
	 * @param player - The player to remove from the crew
	 */
	public void removeMember(String player){
		List<String> members = this.getConfig().getStringList("members");
		members.remove(player);
		this.setConfigValue("members", members);
		GPlayer gplayer = new GPlayer(player);
		gplayer.setCrew(null);
	}
	
	/**
	 * Disband the crew
	 */
	public void disband(){
		for(CrewMember m : this.getMembers()){
			m.setCrew(null);
		}
		this.getFile().delete();
	}
	
	/**
	 * Get the crew's total kills
	 * @return The crew's total kills
	 */
	public int getKills(){
		int kills = 0;
		for(CrewMember m : this.getMembers()){
			kills += m.getPlayer().getKills();
		}
		return kills;
	}
	
	/**
	 * Get the crew's total deaths
	 * @return The crew's total deaths
	 */
	public int getDeaths(){
		int deaths = 0;
		for(CrewMember m : this.getMembers()){
			deaths += m.getPlayer().getDeaths();
		}
		return deaths;
	}
	
	/**
	 * Get the crew's kdr
	 * @return The crew's kdr
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
	 * Set the primary color of the crew
	 * @param color - The color data
	 */
	public void setPrimaryColor(int color){
		this.setConfigValue("colors.primary", color);
	}
	
	/**
	 * Get the primary color of the crew
	 * @return The crew's primary color
	 */
	public int getPrimaryColor(){
		return this.getConfig().getInt("colors.primary");
	}
	
	/**
	 * Set the secondary color of the crew
	 * @param color - The color data
	 */
	public void setSecondaryColor(int color){
		this.setConfigValue("colors.secondary", color);
	}
	
	/**
	 * Get the secondary color of the crew
	 * @return The crew's secondary color
	 */
	public int getSecondaryColor(){
		return this.getConfig().getInt("colors.secondary");
	}
	
	/**
	 * Broadcast a message to all online members
	 * @param message - The message to broadcast
	 */
	public void broadcastMessage(String message){
		for(Player p : Bukkit.getOnlinePlayers()){
			GPlayer gp = new GPlayer(p);
			if(gp.hasCrew() == true){
				if(gp.getCrew().getID() == this.getID()){
					gp.sendNotification("Crew", message);
				}
			}
		}
	}
	
	/**
	 * Broadcast a raw message to all online members
	 * @param message - The raw message to broadcast
	 */
	public void broadcastRawMessage(String message){
		for(Player p : Bukkit.getOnlinePlayers()){
			GPlayer gp = new GPlayer(p);
			if(gp.hasCrew() == true){
				if(gp.getCrew().getID() == this.getID()){
					p.sendMessage(message);
				}
			}
		}
	}
	
	/**
	 * Put the crew armor on the player
	 * @param player - The player to put the crew armor on
	 */
	public void giveCrewArmor(Player player){
		player.getInventory().setChestplate(Utils.renameItem(Utils.dyeArmor(new ItemStack(Material.LEATHER_CHESTPLATE), Utils.getColorFromWool(this.getPrimaryColor())), 
				gold + this.getName().toUpperCase() + " REPRESENT!", gray + italic + "Change crew colors with " + gold + italic + "/crew colors" + gray + italic + "!"));
		player.getInventory().setLeggings(Utils.renameItem(Utils.dyeArmor(new ItemStack(Material.LEATHER_LEGGINGS), Utils.getColorFromWool(this.getSecondaryColor())), 
				gold + this.getName().toUpperCase() + " REPRESENT!", gray + italic + "Change crew colors with " + gold + italic + "/crew colors" + gray + italic + "!"));
		player.getInventory().setBoots(Utils.renameItem(Utils.dyeArmor(new ItemStack(Material.LEATHER_BOOTS), Utils.getColorFromWool(this.getSecondaryColor())), 
				gold + this.getName().toUpperCase() + " REPRESENT!", gray + italic + "Change crew colors with " + gold + italic + "/crew colors" + gray + italic + "!"));
	}
	
	/**
	 * Update the crew members' armor
	 */
	public void updateCrewArmor(){
		for(Player p : Bukkit.getOnlinePlayers()){
			GPlayer gp = new GPlayer(p);
			if(gp.hasCrew() == true){
				if(gp.getCrew().getID() == this.getID()){
					this.giveCrewArmor(p);
				}
			}
		}
	}
	
	/**
	 * Update the crew members' armor
	 */
	public void removeCrewArmor(){
		for(Player p : Bukkit.getOnlinePlayers()){
			GPlayer gp = new GPlayer(p);
			if(gp.hasCrew() == true){
				if(gp.getCrew().getID() == this.getID()){
					gp.clearArmor();
				}
			}
		}
	}
	
	/**
	 * Get the total amount of crews
	 * @return The total amount of crews
	 */
	public static final int getTotalCrews(){
		File folder = new File(main.getDataFolder() + File.separator + "crews");
		if(folder.exists() == true){
			try{
				return folder.listFiles().length;
			}catch (Exception ex){
				return 0;
			}
		}else{
			return 0;
		}
	}
	
	/**
	 * Check if a crew has the given name
	 * @param name - The name to check
	 * @return True or false depending on if there is a gang by that name or not
	 */
	public static final boolean doesCrewExist(String name){
		boolean taken = false;
		for(Crew c : list()){
			if(c.getName().equalsIgnoreCase(name)){
				taken = true;
				break;
			}
		}
		return taken;
	}
	
	/**
	 * Get the list of crews
	 * @return The list of crews
	 */
	public static final List<Crew> list(){
		List<Crew> crews = new ArrayList<Crew>();
		for(int x = 1; x <= getTotalCrews(); x++){
			File f = new File(main.getDataFolder() + File.separator + "crews" + File.separator + x + ".yml");
			if(f.exists() == true){
				crews.add(new Crew(x));
			}
		}
		return crews;
	}
	
	/**
	 * Get a crew from its name
	 * @param name - The name of the crew to get
	 * @return The crew object
	 */
	public static final Crew getCrew(String name){
		Crew crew = null;
		for(Crew c : list()){
			if(c.getName().equalsIgnoreCase(name)){
				crew = c;
				break;
			}
		}
		return crew;
	}
	
	/**
	 * Get the crew colors inventory
	 * @param page - The page of the inventory to get
	 * @return The inventory of that page
	 */
	public static final Inventory getColorsInventory(int page){
		Inventory inv = null;
		if(page == 1){
			inv = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "Which color to change?");
			inv.setItem(3, Utils.renameItem(new ItemStack(Material.LEATHER_CHESTPLATE), ChatColor.YELLOW + "Change Primary Color"));
			inv.setItem(5, Utils.renameItem(new ItemStack(Material.LEATHER_BOOTS), ChatColor.YELLOW + "Change Secondary Color"));
		}else if(page == 2){
			inv = Bukkit.createInventory(null, 18, ChatColor.DARK_GRAY + "Select the primary color!");
			for(int x = 0; x <= 8; x++){
				inv.setItem(x, Utils.renameItem(new ItemStack(Material.WOOL, 1, (short)x), Utils.getColorNameFromWool(x)));
			}
			for(int x = 10; x <= 16; x++){
				inv.setItem(x, Utils.renameItem(new ItemStack(Material.WOOL, 1, (short)(x - 1)), Utils.getColorNameFromWool(x - 1)));
			}
		}else if(page == 3){
			inv = Bukkit.createInventory(null, 18, ChatColor.DARK_GRAY + "Select the secondary color!");
			for(int x = 0; x <= 8; x++){
				inv.setItem(x, Utils.renameItem(new ItemStack(Material.WOOL, 1, (short)x), Utils.getColorNameFromWool(x)));
			}
			for(int x = 10; x <= 16; x++){
				inv.setItem(x, Utils.renameItem(new ItemStack(Material.WOOL, 1, (short)(x - 1)), Utils.getColorNameFromWool(x - 1)));
			}
		}
		return inv;
	}
	
	/**
	 * Get the cost of creating a crew
	 * @return The cost of creating a crew
	 */
	public static final int getCrewCreationCost(){
		return 1000;
	}
}
