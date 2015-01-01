package com.grandcraftauto.tasks;

import org.bukkit.Material;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.grandcraftauto.core.Main;
import com.grandcraftauto.game.player.GPlayer;
import com.grandcraftauto.utils.Utils;

public class LockpickTask extends BukkitRunnable{
	
	Main main = Main.getInstance();
	
	private Player player;
	private GPlayer gplayer;
	private Minecart cart;
	public LockpickTask(Player player, Minecart cart){
		this.player = player;
		this.gplayer = new GPlayer(player);
		this.cart = cart;
	}
	
	public void run(){
		if(player.getLocation().distance(cart.getLocation()) < 4){
			if(player.getInventory().contains(Material.TRIPWIRE_HOOK)){
				gplayer.removeMaterialFromInventory(Material.TRIPWIRE_HOOK, 1);
				int chance = Utils.randInt(1, 8);
				if(chance == 1){
					Utils.setMetadata(cart, "locked", false);
					gplayer.sendMessage("You have successfully lockpicked the vehicle!");
					gplayer.addMentalState(1);
					gplayer.addXP(6);
					this.cancelTask();
				}else{
					gplayer.addMentalState(0.2);
					if(player.getInventory().contains(Material.TRIPWIRE_HOOK)){
						gplayer.sendMessage("The lockpick broke! You attempt to lockpick the vehicle again.");
					}else{
						gplayer.sendMessage("The lockpick broke! You have no more lockpicks to use.");
						this.cancelTask();
					}
				}
			}else{
				this.cancelTask();
			}
		}else{
			gplayer.sendError("You must stay near the car to lockpick it!");
			this.cancelTask();
		}
	}
	
	public void cancelTask(){
		if(main.lockpicking.containsKey(player.getName())){
			main.lockpicking.remove(player.getName());
		}
		this.cancel();
	}
}
