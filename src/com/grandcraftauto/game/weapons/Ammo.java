package com.grandcraftauto.game.weapons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.grandcraftauto.game.missions.Reward;
import com.grandcraftauto.utils.TextUtils;
import com.grandcraftauto.utils.Utils;

public enum Ammo implements Reward {
	
	_45ACP(new ItemStack(Material.INK_SACK, 1, (short) 3), ".45 ACP", 25, Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Primarily used in handguns.")),
	
	_9MM(new ItemStack(Material.INK_SACK), "9mm", 60, Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Big bullets make big damage.")),
	
	_22(new ItemStack(Material.SEEDS), ".22", 15, Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Small, inexpensive, and agile.")),
	
	_556(new ItemStack(Material.MELON_SEEDS), "5.56", 45, Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Primarily used in assault rifles.")),
	
	_762(new ItemStack(Material.CLAY_BALL), "7.62", 75, Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "Able to be fired fast, and with power.")),
	
	_12_GAUGE(new ItemStack(Material.PUMPKIN_SEEDS), "12 Gauge", 90, Arrays.asList(ChatColor.GRAY + "" + ChatColor.ITALIC + "High in density, high in damage."));
	
	private static String gray = ChatColor.GRAY + "";
	private static String gold = ChatColor.GOLD + "";
	
	private ItemStack item;
	private String realName;
	private int cost;
	private List<String> lore;
	Ammo(ItemStack item, String realName, int cost, List<String> lore){
		this.item = item;
		this.realName = realName;
		this.cost = cost;
		this.lore = lore;
	}
	
	/**
	 * Get the material of the ammo
	 * @return The material of the ammo
	 */
	public ItemStack getItem(){
		return item;
	}
	
	/**
	 * Get the real name of the ammo
	 * @return The real name of the ammo
	 */
	public String getName(){
		return realName;
	}
	
	/**
	 * Get the cost of the ammo
	 * @return The cost of the ammo
	 */
	public int getCost(){
		return cost;
	}
	
	/**
	 * Get the lore of the ammo
	 * @return The lore of the ammo
	 */
	public List<String> getLore(){
		return lore;
	}
	
	/**
	 * Get the item stack of the ammo
	 * @return The item stack of the ammo
	 */
	@SuppressWarnings("deprecation")
	public ItemStack getItemStack(boolean showPrice){
		if(showPrice == false){
			return Utils.renameItem(new ItemStack(this.getItem().getType(), 16, this.getItem().getData().getData()), ChatColor.AQUA + this.getName() + " Ammunition", this.getLore());
		}else{
			List<String> lore = new ArrayList<String>();
			lore.addAll(this.getLore());
			lore.add(gold + TextUtils.getArrow() + ChatColor.YELLOW + " Price: " + gray + "$" + this.getCost());
			return Utils.renameItem(new ItemStack(this.getItem().getType(), 16, this.getItem().getData().getData()), ChatColor.AQUA + this.getName() + " Ammunition", lore);
		}
	}

	/**
	 * Get the reward itemstack
	 */
	public ItemStack getItemStack(){
		return this.getItemStack(false);
	}
	
	/**
	 * Get the reward's cash payout
	 */
	public int getCashReward(){
		return 0;
	}
}
