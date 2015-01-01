package com.grandcraftauto.game;

import org.bukkit.ChatColor;

public enum Rank {
	
	/*
	 * Donator Perks:
	 * 
	 * - All ranks get extra apartments
	 * - Instant five levels upon purchase
	 * - Special tag in chat
	 * - Access to certain weapons
	 * - Access to certain cars
	 * - Firework on spawn
	 * - Thug: 1.5x, Pimp: 2x, Godfather: 2.5x RP
	 * 
	 */
	
	DEFAULT(0, ChatColor.GREEN),
	THUG(1, ChatColor.BLUE),
	PIMP(2, ChatColor.BLUE),
	GODFATHER(3, ChatColor.BLUE),
	HELPER(-1, ChatColor.YELLOW),
	MODERATOR(-2, ChatColor.AQUA),
	ADMIN(-3, ChatColor.RED),
	OWNER(-4, ChatColor.DARK_RED);
	
	private int ladder;
	private ChatColor color;
	Rank(int ladder, ChatColor color){
		this.ladder = ladder;
		this.color = color;
	}
	
	/**
	 * Get the ladder position of the rank
	 * @return The ladder position of the rank
	 */
	public int getLadder(){
		return ladder;
	}
	
	/**
	 * Get the chat color of the rank
	 * @return The chat color of the rank
	 */
	public ChatColor getChatColor(){
		return color;
	}
	
	/**
	 * Check if the rank is a donator rank
	 * @return True or false depending on if the rank is a donator rank or not
	 */
	public boolean isDonator(){
		if(this == Rank.THUG || this == Rank.PIMP || this == Rank.GODFATHER){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Check if the rank is a staff rank
	 * @return True or false depending on if the rank is a staff rank or not
	 */
	public boolean isStaff(){
		if(this.getLadder() < 0){
			return true;
		}else{
			return false;
		}
	}
}
