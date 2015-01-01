package gca.utils;

import org.bukkit.ChatColor;

/**
 * A universal class used throughout my plugins for text utilities
 * @author JjPwN1
 */
public class TextUtils {
	
	/**
	 * Get a string that will be centered in a player's chat
	 * @param text - The text to be centered
	 * @return The centered string
	 */
	public static final String centerText(String text) {
	    int maxWidth = 80, spaces = (int) Math.round((maxWidth-1.4*ChatColor.stripColor(text).length())/2);
	    String complete = "";
	    for(int i = 1; i <= spaces; i++){
	    	complete = complete + " ";
	    }
	    complete = complete + text;
	    return complete;
	}
	
	/**
	 * Get the ▶  UTF character
	 * @return The ▶  UTF character
	 */
	public static final String getArrow(){
		return "▶";
	}
	
	/**
	 * Get the arrow UTF character with a specified amount of spaces in front of it
	 * @return The arrow UTF character with the specified amount of spaces in front of it
	 */
	public static final String arrow(int spaces){
		String space = "";
		for(int x = 1; x <= spaces; x++){
			space = space + " ";
		}
		return space + ChatColor.GOLD + getArrow() + " ";
	}
	
	/**
	 * Get the ◀  UTF character
	 * @return The ◀  UTF character
	 */
	public static final String getBackwardsArrow(){
		return "◀";
	}
	
	/**
	 * Get the ✯  UTF character
	 * @return The ✯  UTF character
	 */
	public static final String getStar(){
		return "✯";
	}
	
	/**
	 * Get the » UTF character
	 * @return The » UTF character
	 */
	public static final String getDoubleArrow(){
		return "»";
	}
	
	/**
	 * Get the « UTF character
	 * @return The « UTF character
	 */
	public static final String getBackwardsDoubleArrow(){
		return "«";
	}
	
	/**
	 * Get the ￨ UTF character
	 * @return The ￨ UTF character
	 */
	public static final String getBar(){
		return "￨";
	}
	
	/**
	 * Get the • UTF character
	 * @return The • UTF character
	 */
	public static final String getSeperator(){
		return "•";
	}
	
	/**
	 * Get the ˢᵗ UTF character
	 * @return The ˢᵗ UTF character
	 */
	public static final String getSuperScript(){
		return "ˢᵗ";
	}
	
	/**
	 * Get the symbol for a level
	 * @param level - The level to get the symbol for
	 * @return The symbol for the level
	 */
	public static final String getSymbolForLevel(int level){
		String symbol = "";
		if(level >= 10 && level < 20){
			symbol = "✲";
		}else if(level >= 20 && level < 30){
			symbol = "�?�";
		}else if(level >= 30 && level < 40){
			symbol = "�?�";
		}else if(level >= 40 && level < 50){
			symbol = "✺";
		}else if(level >= 50 && level < 60){
			symbol = "✦";
		}else if(level >= 60 && level < 70){
			symbol = "✯";
		}else if(level >= 70 && level < 80){
			symbol = "✤";
		}else if(level >= 80 && level < 90){
			symbol = "�?�";
		}else if(level >= 90){
			symbol = "✢";
		}
		return symbol;
	}
}
