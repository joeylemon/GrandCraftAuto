package gca.tasks;

import gca.core.GPlayer;
import gca.core.Main;
import gca.game.missions.objectives.HoldUpObjective;
import gca.game.missions.objectives.RobStationObjective;
import gca.utils.Utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RobTask extends BukkitRunnable{
	
	Main main = Main.getInstance();
	
	private Player robber;
	private GPlayer gplayer;
	private LivingEntity clerk;
	private String clerkName;
	private int period;
	private boolean holdUpObj = false;
	public RobTask(Player robber, LivingEntity clerk, String clerkName, int period){
		this.robber = robber;
		this.gplayer = new GPlayer(robber);
		this.clerk = clerk;
		this.clerkName = clerkName;
		this.period = period;
		if(gplayer.hasMission() == true && gplayer.getObjective() instanceof HoldUpObjective){
			this.holdUpObj = true;
		}
	}
	
	private int runtime = 0;
	
	public void run(){
		if(runtime < period){
			runtime++;
		}else{
			GPlayer gplayer = new GPlayer(robber);
			
			if(holdUpObj == false){
				main.robCooldown.add(robber.getName());
				main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
					public void run(){
						if(main.robCooldown.contains(robber.getName())){
							main.robCooldown.remove(robber.getName());
						}
					}
				}, 2400);
			}
			
			if(robber.getLocation().distance(clerk.getLocation()) < 12){
				if(clerk.isDead() == false && clerk.isValid() == true){
					gplayer.sendMessage(ChatColor.GOLD + ChatColor.stripColor(clerkName) + ": " + ChatColor.GRAY + "There you go! Please don't hurt me!");
					
					if(holdUpObj == true){
						HoldUpObjective obj = (HoldUpObjective) gplayer.getObjective();
						Item item = Utils.getGCAWorld().dropItem(clerk.getEyeLocation(), obj.itemToGet());
						item.setVelocity(Utils.getDifferentialVector(item.getLocation(), robber.getEyeLocation()).multiply(0.15));
						gplayer.advanceObjective();
					}else{
						Item money = Utils.dropMoney(clerk.getEyeLocation(), 20, 45);
						money.setVelocity(Utils.getDifferentialVector(money.getLocation(), robber.getEyeLocation()).multiply(0.15));
						if(gplayer.hasMission() == true){
							if(gplayer.getObjective() instanceof RobStationObjective){
								gplayer.advanceObjective();
							}
						}
					}
					
					this.cancelTask();
				}else{
					gplayer.sendError("You can't rob a dead person!");
					this.cancelTask();
				}
			}else{
				gplayer.sendError("You traveled too far away to rob!");
				this.cancelTask();
			}
		}
	}
	
	/**
	 * Cancel the task
	 */
	public void cancelTask(){
		if(main.robbing.containsKey(robber.getName())){
			main.robbing.remove(robber.getName());
		}
		this.cancel();
	}
}
