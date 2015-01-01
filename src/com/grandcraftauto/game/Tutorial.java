package com.grandcraftauto.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.grandcraftauto.core.Main;
import com.grandcraftauto.game.player.GPlayer;
import com.grandcraftauto.tasks.MessageTask;
import com.grandcraftauto.utils.Utils;

public class Tutorial {
	
	static Main main = Main.getInstance();
	
	private Player player;
	private GPlayer gplayer;
	private int step = 0;
	
	/**
	 * Create a new tutorial instance
	 * @param player - The player to put in the new instance
	 */
	public Tutorial(Player player){
		this.player = player;
		this.gplayer = new GPlayer(player);
	}
	
	/**
	 * Get the player in the tutorial
	 * @return The player in the tutorial
	 */
	public Player getPlayer(){
		return player;
	}
	
	/**
	 * Get the step the tutorial is on
	 * @return The step the tutorial is on
	 */
	public int getStep(){
		return step;
	}
	
	/**
	 * Advance the step the tutorial is on
	 */
	public void advanceStep(){
		if((step + 1) < getTotalSteps()){
			step++;
			player.setAllowFlight(true);
			player.setFlying(true);
			player.teleport(getStepLocation(step));
			gplayer.sendMessageHeader("Tutorial");
			MessageTask task = new MessageTask(player, getStepDialogue(step));
			task.runTaskTimer(main, 0, 150);
			if(main.messageTask.contains(player.getName())){
				main.messageTask.remove(player.getName());
			}
			main.messageTask.add(player.getName());
		}else{
			if(main.tutorial.containsKey(player.getName())){
				main.tutorial.remove(player.getName());
			}
			gplayer.spawn();
			player.sendMessage(ChatColor.GOLD + "* " + ChatColor.GRAY + "It is recommended you speak to Lamar for your first mission.");
		}
	}
	
	public static final List<String> getStepDialogue(int step){
		List<String> dialogue = new ArrayList<String>();
		if(step == 1){
			dialogue = Arrays.asList("Welcome to Los Craftos, %n!", 
					"This city is full of things to do, and it is all available to you seamlessly.",
					"You can complete missions, rob banks with your crew, compete in races against other players, and much more.",
					"The first thing you are going to want to do when you enter the city is complete a mission.");
		}else if(step == 2){
			dialogue = Arrays.asList("Missions can be received from multiple NPCs throughout the city, such as Lamar or Michael.", 
					"When you accept a mission, you are given a set of objectives in which you must follow to be rewarded with RP.");
		}else if(step == 3){
			dialogue = Arrays.asList("RP, or Reputation Points, is what you use to level up.", 
					"As you level up, you will gain access to more items, vehicles, and missions.");
		}else if(step == 4){
			dialogue = Arrays.asList("Purchase different items from the multiple stores throughout the city.", 
					"You can buy guns from AmmuNation, or some new cars from the Car Dealership.",
					"Money is earned through completing missions or jobs, leveling up, or mugging citizens off of the streets.");
		}else if(step == 5){
			dialogue = Arrays.asList("Weapons and cars carry unique attributes that enhance their abilities.",
					"From long range to inaccuracy, each weapon has its pros and cons.",
					"Cars also have speed and armor which give you a reason to buy a new car when you unlock one.",
					"Both weapons and cars are unlocked at certain levels, and are a one-time purchase.");
		}else if(step == 6){
			dialogue = Arrays.asList("In Grand Craft Auto, you never drop your items when you die.",
					"Therefore, buying new items proves to be a more rewarding experience in the end.",
					"You must be informed, however, that you do drop all the money in your wallet upon death.",
					"This is why you must deposit all your money into the bank every oppurtunity you get.");
		}else if(step == 7){
			dialogue = Arrays.asList("Killing other players is not the main objective, however it is possible.",
					"If you kill too many players, your mental state will rise. Players can see your mental state and can know to avoid you.",
					"When you kill other players with a mental state lower than yours, you will begin to lose money and RP.",
					"However, killing players with a mental state higher than yours will reward you with RP.");
		}else if(step == 8){
			dialogue = Arrays.asList("Well %n, I hope you now know the fundamentals of what this city is about.",
					"While your are slightly prepared, you still have much to learn out on the streets.",
					"Go on. It is now your turn to become immersed in the rich city of Los Craftos.");
		}
		return dialogue;
	}
	
	public static final void setStepLocation(int step, Location loc){
		main.getConfig().set("tutorial.steps." + step + ".x", loc.getX());
		main.getConfig().set("tutorial.steps." + step + ".y", loc.getY());
		main.getConfig().set("tutorial.steps." + step + ".z", loc.getZ());
		main.getConfig().set("tutorial.steps." + step + ".yaw", loc.getYaw());
		main.getConfig().set("tutorial.steps." + step + ".pitch", loc.getPitch());
		main.saveConfig();
	}
	
	public static final Location getStepLocation(int step){
		double x,y,z;
		x = main.getConfig().getDouble("tutorial.steps." + step + ".x");
		y = main.getConfig().getDouble("tutorial.steps." + step + ".y");
		z = main.getConfig().getDouble("tutorial.steps." + step + ".z");
		int yaw,pitch;
		yaw = main.getConfig().getInt("tutorial.steps." + step + ".yaw");
		pitch = main.getConfig().getInt("tutorial.steps." + step + ".pitch");
		return new Location(Utils.getGCAWorld(), x, y, z, yaw, pitch);
	}
	
	public static final int getTotalSteps(){
		return 9;
	}
}
