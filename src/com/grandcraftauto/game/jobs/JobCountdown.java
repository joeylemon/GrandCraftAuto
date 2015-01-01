package com.grandcraftauto.game.jobs;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import com.grandcraftauto.core.Main;

public class JobCountdown extends BukkitRunnable {
	
	Main main = Main.getInstance();
	
	private JobInstance job;
	private String name;
	private int countdown;
	private boolean waiting;
	public JobCountdown(JobInstance job, int countdown, boolean waiting){
		this.job = job;
		this.name = job.getJob().getType().toLowerCase();
		this.countdown = countdown;
		this.waiting = waiting;
	}
	
	private int runtime = 0;
	
	public void run(){
		// pre-game
		if(waiting == false){
			if(runtime < countdown){
				job.broadcastMessage("The " + name + " is starting in " + ChatColor.GOLD + (countdown - runtime) + ChatColor.GRAY + " seconds!");
				runtime++;
			}else{
				job.setState(JobState.STARTED);
				job.broadcastMessage("The " + name + " has started!");
				this.cancel();
			}
		// waiting
		}else if(waiting == true){
			if(runtime < countdown){
				runtime++;
			}else{
				int size = job.getPlayers().size();
				if(size > 0){
					if(size >= job.getJob().getMinimumPlayers()){
						job.start();
						this.cancel();
					}else{
						runtime = 0;
					}
				}else{
					if(main.jobs.contains(job)){
						main.jobs.remove(job);
					}
					this.cancel();
				}
			}
		}
	}
}
