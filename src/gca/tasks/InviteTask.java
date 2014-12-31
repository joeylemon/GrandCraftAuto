package gca.tasks;

import gca.core.GPlayer;
import gca.core.Main;

import org.bukkit.scheduler.BukkitRunnable;

public class InviteTask extends BukkitRunnable{
	
	private Main main = Main.getInstance();
	
	private GPlayer sender;
	private GPlayer receiver;
	public InviteTask(GPlayer sender, GPlayer receiver){
		this.sender = sender;
		this.receiver = receiver;
	}
	
	private int timer = 60;
	
	public void run(){
		if(timer > 0){
			timer--;
		}else{
			if(main.invited.containsKey(receiver.getName())){
				main.invited.remove(receiver.getName());
			}
			receiver.sendMessage("The crew invitation has expired!");
			this.cancel();
		}
	}
	
	/**
	 * Get the task's invitation sender
	 * @return The task's invitation sender
	 */
	public GPlayer getSender(){
		return sender;
	}
	
	/**
	 * Get the task's invitation receiver
	 * @return The task's invitation receiver
	 */
	public GPlayer getReceiver(){
		return receiver;
	}
}
