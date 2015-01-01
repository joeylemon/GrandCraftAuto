package gca.game.missions;

import org.bukkit.inventory.ItemStack;

public interface Reward {
	
	/**
	 * Get the itemstack of the reward
	 * @return The itemstack of the reward
	 */
	public ItemStack getItemStack();
	
	/**
	 * Get the cash reward of the reward
	 * @return The cash reward of the reward
	 */
	public int getCashReward();
}
