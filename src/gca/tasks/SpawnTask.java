package gca.tasks;

import gca.core.GPlayer;
import gca.core.Main;
import gca.game.apartment.Apartment;
import gca.utils.EffectUtils;
import gca.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SpawnTask extends BukkitRunnable{
	
	Main main = Main.getInstance();
	private String gray = ChatColor.GRAY + "";
	private String gold = ChatColor.GOLD + "";
	
	Player player;
	GPlayer gplayer;
	Location dest;
	public SpawnTask(Player p, Location d){
		player = p;
		gplayer = new GPlayer(p);
		dest = d;
	}
	
	public void run(){
		if(player != null && player.isOnline() == true){
			if(player.getLocation().distance(dest) > 45){
				Location newLoc = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() - 30, player.getLocation().getZ(), 0, 90);
				player.teleport(newLoc);
			}else{
				player.setFlying(false);
				player.setAllowFlight(false);
				if(Main.getInstance().spawning.contains(player.getName())){
					Main.getInstance().spawning.remove(player.getName());
				}
				player.teleport(dest);
				gplayer.sendMessage("Welcome to the " + gold + "Grand Craft Auto " + gray + "beta testing!");
				gplayer.sendMessage("You are currently playing on " + gold + "GCA v" + main.getDescription().getVersion() + gray + "!");
				if(gplayer.hasApartment() == true){
					List<Apartment> rent = new ArrayList<Apartment>();
					for(Apartment a : gplayer.getApartments()){
						if(gplayer.hasToPayRent(a) == true){
							rent.add(a);
						}
					}
					if(rent.size() > 0){
						int total = 0;
						gplayer.sendMessage("You must pay rent for the following apartments:");
						for(Apartment a : rent){
							if(gplayer.getWarnedOfRent(a) == false){
								gplayer.setWarnedOfRent(a, true);
								gplayer.setLastRentWarning(a);
							}
							if(gplayer.canForecloseApartment(a) == false){
								int hours = (int) ((gplayer.getLastRentWarning(a) - System.currentTimeMillis()) / 3600000);
								String s = "s";
								if(hours == 1){
									s = "";
								}
								total += a.getRent();
								player.sendMessage("  " + gray + "- " + gold + a.getName() + gray + ": " + gold + "$" + a.getRent() + gray + " (" + hours + " hour" + s + ")");
							}else{
								gplayer.forecloseApartment(a);
							}
						}
						gplayer.sendMessage("Grand Total: " + gold + "$" + total + gray + ". Pay with " + gold + "/apartment pay rent" + gray + ".");
					}
				}
				if(gplayer.getRankLadder() >= 1){
					Utils.launchFirework(player.getEyeLocation());
				}
				this.cancel();
			}
			EffectUtils.playSpawnSound(player);
		}else{
			if(Main.getInstance().spawning.contains(player.getName())){
				Main.getInstance().spawning.remove(player.getName());
			}
			this.cancel();
		}
	}
}
