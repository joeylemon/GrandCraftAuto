package com.grandcraftauto.tasks;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.grandcraftauto.core.Main;
import com.grandcraftauto.game.inventories.InventoryHandler;
import com.grandcraftauto.game.missions.objectives.StealKidneyObjective;
import com.grandcraftauto.game.player.GPlayer;
import com.grandcraftauto.utils.Utils;

public class KidneyHarvestTask extends BukkitRunnable {
	
	private Main main = Main.getInstance();
	private String gray = ChatColor.GRAY + "";
	private String gold = ChatColor.GOLD + "";
	
	private Player player;
	private GPlayer gplayer;
	private Player target;
	public KidneyHarvestTask(Player player, Player target){
		this.player = player;
		this.gplayer = new GPlayer(player);
		this.target = target;
	}
	
	private int total = 10;
	private int runtime = 0;
	
	public void run(){
		if(player.isDead() == false && player.isValid() == true && target.isDead() == false && target.isValid() == true){
			if(main.knockout.contains(target.getName())){
				if(player.getLocation().distance(target.getLocation()) < 3){
					if((total - runtime) > 0){
						runtime++;
					}else{
						InventoryHandler.addItemToAppropriateSlot(player, Utils.getKidneyItem());
						Utils.setMetadata(target, "kidneyStolen", true);
						gplayer.sendMessage("You have stolen " + gold + target.getName() + "'s " + gray + "kidney!");
						gplayer.addMentalState(20);
						if(main.harvesting.contains(target.getName())){
							main.harvesting.remove(target.getName());
						}
						main.harvestedKidney.add(player.getName());
						main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
							public void run(){
								main.harvestedKidney.remove(player.getName());
							}
						}, 8000);
						if(gplayer.hasMission() == true){
							if(gplayer.getObjective() instanceof StealKidneyObjective){
								StealKidneyObjective obj = (StealKidneyObjective) gplayer.getObjective();
								obj.setAmountStolen(player.getName(), obj.getAmountStolen(player.getName()) + 1);
								if(obj.getAmountStolen(player.getName()) >= obj.getAmountToSteal()){
									gplayer.advanceObjective();
								}
							}
						}
						this.cancel();
					}
				}else{
					if(main.harvesting.contains(target.getName())){
						main.harvesting.remove(target.getName());
					}
					gplayer.sendError("You moved too far away while cutting out the kidney!");
					this.cancel();
				}
			}else{
				if(main.harvesting.contains(target.getName())){
					main.harvesting.remove(target.getName());
				}
				gplayer.sendError("The player woke up before you could finish!");
				this.cancel();
			}
		}else{
			if(main.harvesting.contains(target.getName())){
				main.harvesting.remove(target.getName());
			}
			this.cancel();
		}
	}
}
