package com.grandcraftauto.game.missions;

import org.bukkit.inventory.ItemStack;

public class ItemReward implements Reward {
	
	private ItemStack item;
	public ItemReward(ItemStack item){
		this.item = item;
	}

	/**
	 * Get the reward's itemstack
	 */
	public ItemStack getItemStack(){
		return item;
	}

	/**
	 * Get the reward's cash payout
	 */
	public int getCashReward(){
		return 0;
	}
	
	/**
	 * Get the class as a reward
	 * @return The class as a reward
	 */
	public Reward asReward(){
		return (Reward) this;
	}

}
