package com.grandcraftauto.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.grandcraftauto.utils.Utils;

public class ServerStopTask extends BukkitRunnable {
	
	private int runtime = 0;
	private int time = 10;
	
	public void run(){
		int timeleft = (time - runtime);
		if(timeleft > 0){
			runtime++;
			Utils.broadcastMessage("Server is restarting in " + ChatColor.GOLD + timeleft + ChatColor.GRAY + " seconds.");
		}else{
			this.cancel();
			for(Player p : Bukkit.getOnlinePlayers()){
				p.kickPlayer(ChatColor.GOLD + "Server is restarting.");
			}
			Bukkit.getServer().shutdown();
		}
	}

}
