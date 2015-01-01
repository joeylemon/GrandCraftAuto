package com.grandcraftauto.game;

import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.grandcraftauto.game.player.GPlayer;
import com.grandcraftauto.utils.TextUtils;
import com.grandcraftauto.utils.Utils;

public enum Skill {
	
	DRIVING(Material.MINECART, "Increases vehicle speed."),
	HEALTH(Material.GOLDEN_APPLE, "Quickens automatic health regeneration."),
	SHOOTING(Material.IRON_HOE, "Increases gun accuracy and damage."),
	STAMINA(Material.FEATHER, "Increases walking and running speeds."),
	STRENGTH(Material.STICK, "Increases melee damage.");
	
	private static String gold = ChatColor.GOLD + "";
	private static String gray = ChatColor.GRAY + "";
	private static String italic = ChatColor.ITALIC + "";
	
	private Material icon;
	private String desc;
	Skill(Material icon, String desc){
		this.icon = icon;
		this.desc = desc;
	}
	
	/**
	 * Get this skill's icon
	 * @return This skill's icon
	 */
	public Material getIcon(){
		return icon;
	}
	
	/**
	 * Get this skill's description
	 * @return This skill's description
	 */
	public String getDescription(){
		return desc;
	}
	
	/**
	 * Get how much a skill is improving a player's ability
	 * @param level - The level of the skill
	 * @return The percent that the skill is improving
	 */
	public static final int getPercentIncrease(int level){
		return (int) Utils.getPercent(level, 50);
	}
	
	/**
	 * Get the skill inventory for a player
	 * @param player - The player to get the inventory for
	 * @return The skill inventory for the player
	 */
	public static final Inventory getInventory(Player player){
		GPlayer gplayer = new GPlayer(player);
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "Thug Points Available: " + gplayer.getThugPoints());
		String arrow = TextUtils.arrow(0);
		
		int slot = 0;
		for(Skill s : values()){
			int level = gplayer.getSkillLevel(s);
			inv.setItem(slot, Utils.renameItem(new ItemStack(s.getIcon(), level), ChatColor.YELLOW + WordUtils.capitalizeFully(s.toString()), 
					gray + italic + s.getDescription(),
					arrow + gold + "Current Progress: " + gray + Utils.asBars(getPercentIncrease(level)) + "    ",
					" ",
					ChatColor.GREEN + "Click to upgrade this skill."));
			slot += 2;
		}
		
		return inv;
	}

}
