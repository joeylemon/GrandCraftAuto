package com.grandcraftauto.game.inventories;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.grandcraftauto.core.Main;
import com.grandcraftauto.utils.Utils;

public final class Bounties {
	
	static Main main = Main.getInstance();
	
	/**
	 * Gets the bounty inventory
	 * @return The bounty inventory
	 */
	public static final Inventory getInventory(Player player){
		List<Player> players = new ArrayList<Player>();
		for(Player p : Utils.getGCAWorld().getPlayers()){
			if(Utils.isNPC(p) == false){
				players.add(p);
			}
		}
		Inventory inv = Bukkit.createInventory(null, Utils.getRoundedInventorySize(players.size()), ChatColor.DARK_GRAY + "Place a bounty!");
		int slot = 0;
		for(Player p : players){
			if(!p.getName().equalsIgnoreCase(player.getName())){
				int currentBounty = 0;
				if(main.bounties.containsKey(p.getName())){
					currentBounty = main.bounties.get(p.getName());
				}
				inv.setItem(slot, Utils.renameItem(new ItemStack(Material.SKULL_ITEM, 1, (short) 3), ChatColor.GRAY + p.getName(), 
						ChatColor.GOLD + "Current Bounty: " + ChatColor.GRAY + "$" + currentBounty));
				slot++;
			}
		}
		return inv;
	}
	
	/**
	 * Get the setting inventory
	 * @param toSet - The player getting a bounty put on their head
	 * @return The setting inventory
	 */
	public static final Inventory getSetInventory(String toSet){
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "Bounty - " + toSet);
		int amount = 50;
		for(int x = 0; x <= 8; x++){
			inv.setItem(x, Utils.renameItem(new ItemStack(Material.GOLD_NUGGET), ChatColor.GOLD + "$" + amount));
			amount += 50;
		}
		return inv;
	}
	
	/**
	 * Get the itemstack to be put into the player's inventory
	 * @return The itemstack to be put into the player's inventory
	 */
	public static final ItemStack getItem(){
		return Utils.renameItem(new ItemStack(Material.PAPER, 1, (short) 3), ChatColor.YELLOW + "Bounty Placer");
	}
}
