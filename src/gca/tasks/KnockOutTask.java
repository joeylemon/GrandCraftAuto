package gca.tasks;

import gca.core.Main;
import gca.game.player.GPlayer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class KnockOutTask extends BukkitRunnable {
	
	private Main main = Main.getInstance();
	private String gray = ChatColor.GRAY + "";
	private String gold = ChatColor.GOLD + "";
	
	private Player player;
	private GPlayer gplayer;
	private Player target;
	private GPlayer gtarget;
	public KnockOutTask(Player player, Player target){
		this.player = player;
		this.gplayer = new GPlayer(player);
		this.target = target;
		this.gtarget = new GPlayer(target);
	}
	
	private int click = 0;
	private int last = 0;
	
	public void run(){
		if(last > 1){
			if(main.knockingout.containsKey(player.getName())){
				main.knockingout.remove(player.getName());
			}
			this.cancel();
			gtarget.sendMessage(gold + player.getName() + gray + " failed to knock you out!");
			gplayer.sendError("You didn't hold the rag long enough!");
		}
		last++;
	}
	
	/**
	 * Add a click to the task
	 */
	public void addClick(){
		click++;
		last = 0;
		if(click >= 20){
			ItemStack item = player.getItemInHand();
			if(item.getAmount() > 1){
				item.setAmount(item.getAmount() - 1);
			}else{
				player.setItemInHand(null);
			}
			main.knockout.add(target.getName());
			target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 400, 3));
			target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 400, 3));
			target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 400, 10));
			
			gtarget.sendMessage("You have been knocked out!");
			gtarget.sendSubtitle(ChatColor.DARK_RED + "You are unconscious.", 370);
			gplayer.sendMessage("You have knocked " + gold + target.getName() + gray + " out!");
			gplayer.addMentalState(10);
			
			main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
				public void run(){
					main.knockout.remove(target.getName());
					if(target.hasMetadata("kidneyStolen") == true){
						gtarget.sendMessage(ChatColor.ITALIC + "You wake up with your kidney missing.");
					}
				}
			}, 370);
			
			if(main.knockingout.containsKey(player.getName())){
				main.knockingout.remove(player.getName());
			}
			this.cancel();
		}
	}
	
}
