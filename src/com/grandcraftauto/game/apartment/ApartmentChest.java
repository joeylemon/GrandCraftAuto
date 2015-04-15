package com.grandcraftauto.game.apartment;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;

import com.grandcraftauto.game.player.GPlayer;

public class ApartmentChest{
	
	private GPlayer gplayer;
	private Chest chest;
	private String path;
	
	/**
	 * Create a new apartment chest
	 * @param chestGPlayer - The owner of the chest
	 * @param chestChest - The chest
	 */
	public ApartmentChest(GPlayer chestGPlayer, Chest chestChest){
		gplayer = chestGPlayer;
		chest = chestChest;
		path = "apartment.chests." + chestChest.getLocation().getBlockX() + ">" + chestChest.getLocation().getBlockY() + ">" + chestChest.getLocation().getBlockZ();
	}
	
	/**
	 * Get the apartment chest's actual chest
	 * @return The actual chest
	 */
	public Chest getChest(){
		return chest;
	}
	
	/**
	 * Get the total items in the chest
	 * @return The total items in the chest
	 */
	public int getTotalItems(){
		return gplayer.getFile().getConfig().getInt(path + ".totalitems");
	}
	
	/**
	 * Set the total items in the chest
	 * @param total - The amount of items in the chest
	 */
	public void setTotalItems(int total){
		gplayer.getFile().setConfigValue(path + ".totalitems", total);
	}
	
	/**
	 * Set the chest's inventory
	 * @param inv - The inventory to set as the chest's inventory
	 */
	public void setInventory(Inventory inv){
		gplayer.getFile().setConfigValue(path + ".items", null);
		this.setTotalItems(inv.getSize() - 1);
		for(int x = 0; x <= (inv.getSize() - 1); x++){
			if(inv.getItem(x) != null){
				gplayer.getFile().setConfigValue(path + ".items." + x, inv.getItem(x));
			}
		}
	}
	
	/**
	 * Get the chest's inventory
	 * @return The chest's inventory
	 */
	public Inventory getInventory(){
		Inventory inv = Bukkit.createInventory(chest, chest.getInventory().getSize(), ChatColor.DARK_GRAY + "Apartment Chest");
		for(int x = 0; x <= this.getTotalItems(); x++){
			if(gplayer.getFile().getConfig().getItemStack(path + ".items." + x) != null){
				inv.setItem(x, gplayer.getFile().getConfig().getItemStack(path + ".items." + x));
			}
		}
		return inv;
	}
}
