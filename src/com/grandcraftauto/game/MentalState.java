package com.grandcraftauto.game;

import org.bukkit.ChatColor;

public enum MentalState {
	
	NORMAL(1, 0, 19, ChatColor.GRAY),
	UNSTABLE(2, 20, 39, ChatColor.YELLOW),
	UNHINGED(3, 40, 59, ChatColor.GOLD),
	MANIAC(4, 60, 79, ChatColor.RED),
	PSYCHOPATH(5, 80, 100, ChatColor.DARK_RED);
	
	private int level;
	private double minstate;
	private double maxstate;
	private ChatColor color;
	MentalState(int level, double minstate, double maxstate, ChatColor color){
		this.level = level;
		this.minstate = minstate;
		this.maxstate = maxstate;
		this.color = color;
	}
	
	/**
	 * Get the state level
	 * @return The state level
	 */
	public int getLevel(){
		return level;
	}
	
	/**
	 * Get the minimum state value
	 * @return The minimum state value
	 */
	public double getMinimumState(){
		return minstate;
	}
	
	/**
	 * Get the maximum state value
	 * @return The maximum state value
	 */
	public double getMaximumState(){
		return maxstate;
	}
	
	/**
	 * Get the state's color
	 * @return The state's color
	 */
	public ChatColor getColor(){
		return color;
	}
	
	/**
	 * Check if a value falls into this state
	 * @param state - The value to check
	 * @return True or false depending on if the value falls in the range or not
	 */
	public boolean fallsInRange(double state){
		if(state >= this.getMinimumState() && state <= this.getMaximumState()){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Get the difference between this state and another state
	 * @param state - The other state to get the difference with
	 * @return The difference between this state and another state
	 */
	public double getDifference(MentalState state){
		return this.getLevel() - state.getLevel();
	}
}
