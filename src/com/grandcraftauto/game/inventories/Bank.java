package com.grandcraftauto.game.inventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.grandcraftauto.game.player.GPlayer;
import com.grandcraftauto.utils.TextUtils;
import com.grandcraftauto.utils.Utils;

public final class Bank {
	
	/**
	 * Get the bank inventory
	 * @return The bank inventory
	 */
	public static final Inventory getInventory(GPlayer gplayer, BankInventory type){
		Inventory inv = null;
		String arrow = ChatColor.GOLD + TextUtils.getArrow() + " ";
		if(type == BankInventory.WITHDRAW_DEPOSIT){
			inv = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "Withdraw or Deposit?");
			inv.setItem(3, Utils.renameItem(new ItemStack(Material.PAPER), ChatColor.YELLOW + "Withdraw", arrow + "Wallet: " + ChatColor.GRAY + "$" + (int)gplayer.getWalletBalance(), 
					arrow + "Bank: " + ChatColor.GRAY + "$" + (int)gplayer.getBankBalance()));
			inv.setItem(5, Utils.renameItem(new ItemStack(Material.PAPER), ChatColor.YELLOW + "Deposit", arrow + "Wallet: " + ChatColor.GRAY + "$" + (int)gplayer.getWalletBalance(), 
					arrow + "Bank: " + ChatColor.GRAY + "$" + (int)gplayer.getBankBalance()));
		}else{
			if(type == BankInventory.WITHDRAW){
				inv = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "Withdraw Cash");
			}else if(type == BankInventory.DEPOSIT){
				inv = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "Deposit Cash");
			}
			inv.setItem(0, Utils.renameItem(new ItemStack(Material.PAPER), ChatColor.YELLOW + TextUtils.getBackwardsArrow() + " Go Back"));
			int amount = 50;
			for(int x = 1; x <= 8; x++){
				inv.setItem(x, Utils.renameItem(new ItemStack(Material.GOLD_NUGGET), ChatColor.GOLD + "$" + amount));
				amount += 50;
			}
		}
		return inv;
	}

}
