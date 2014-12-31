package gca.tasks;

import gca.core.GPlayer;
import gca.core.Main;
import gca.game.inventories.InventoryHandler;
import gca.utils.Utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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
