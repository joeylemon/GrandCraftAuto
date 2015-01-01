package gca.game.missions;

import org.bukkit.inventory.ItemStack;

public class MoneyReward implements Reward{
	
	private int cash;
	
	/**
	 * Create a new money reward
	 * @param rewardCash - The amount of cash to be rewarded
	 */
	public MoneyReward(int rewardCash){
		cash = rewardCash;
	}

	/**
	 * Get the reward's itemstack
	 */
	public ItemStack getItemStack(){
		return null;
	}
	
	/**
	 * Get the reward's cash payout
	 */
	public int getCashReward(){
		return cash;
	}
	
	/**
	 * Get the class as a reward
	 * @return The class as a reward
	 */
	public Reward asReward(){
		return (Reward) this;
	}
}
