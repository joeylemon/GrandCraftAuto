package com.grandcraftauto.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import com.grandcraftauto.core.Main;
import com.grandcraftauto.game.player.GPlayer;

public class FriendRequestTask extends BukkitRunnable{
	
	Main main = Main.getInstance();
	
	private GPlayer sender;
	private GPlayer receiver;
	public FriendRequestTask(GPlayer sender, GPlayer receiver){
		this.sender = sender;
		this.receiver = receiver;
	}
	
	private int timer = 60;
	
	public void run(){
		if(timer > 0){
			timer--;
		}else{
			if(main.friendRequest.containsKey(receiver.getName())){
				main.friendRequest.remove(receiver.getName());
			}
			receiver.sendMessage("The friend request has expired!");
			this.cancel();
		}
	}
	
	/**
	 * Get the task's request sender
	 * @return The task's request sender
	 */
	public GPlayer getSender(){
		return sender;
	}
	
	/**
	 * Get the task's request receiver
	 * @return The task's request receiver
	 */
	public GPlayer getReceiver(){
		return receiver;
	}
}
