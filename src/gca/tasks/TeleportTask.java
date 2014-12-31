package gca.tasks;

import gca.core.GPlayer;
import gca.core.Main;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportTask extends BukkitRunnable{
	
	Main main = Main.getInstance();
	
	private Player teleporter;
	private Player toTeleportTo;
	public TeleportTask(Player teleporter, Player toTeleportTo){
		this.teleporter = teleporter;
		this.toTeleportTo = toTeleportTo;
	}
	
	private int timer = 6;
	
	public void run(){
		if(timer > 0){
			timer--;
		}else{
			teleporter.teleport(toTeleportTo.getLocation());
			new GPlayer(teleporter).sendNotification("Friends", "You have teleported to " + ChatColor.GOLD + toTeleportTo.getName() + ChatColor.GRAY + "!");
			this.cancelTask();
		}
	}
	
	/**
	 * Cancel the task after performing necessary functions
	 */
	public void cancelTask(){
		if(main.teleporting.containsKey(teleporter.getName())){
			main.teleporting.remove(teleporter.getName());
		}
		this.cancel();
	}
	
	/**
	 * Get the player who is teleporting
	 * @return The player who is teleporting
	 */
	public Player getTeleporter(){
		return teleporter;
	}
	
	/**
	 * Get the player who is getting teleported to
	 * @return The player who is getting teleported to
	 */
	public Player getToTeleportTo(){
		return toTeleportTo;
	}
}
